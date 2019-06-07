package com.kidsoncoffee.monsters;

import com.google.inject.Inject;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class MonsterBuilderGenerator {

  private final Elements elementUtils;

  private final Types typeUtils;

  private final Filer filer;

  private final MonsterLimbRetriever limbRetriever;

  @Inject
  public MonsterBuilderGenerator(
      final Types typeUtils,
      final Elements elementUtils,
      final Filer filer,
      final MonsterLimbRetriever limbRetriever) {
    this.elementUtils = elementUtils;
    this.typeUtils = typeUtils;
    this.filer = filer;
    this.limbRetriever = limbRetriever;
  }

  public void generate(
      final Element monsterElement,
      final Optional<? extends TypeMirror> monsterSetup,
      final ClassName schemaClassName) {
    final TypeMirror monsterSetupType = monsterSetup.get();

    final Element monsterTargetElement =
        this.typeUtils.asElement(
            this.typeUtils.capture(monsterSetupType).accept(new MonsterSetupTypeVisitor(), null));

    final ClassName monsterClassName =
        ClassName.get(
            this.elementUtils.getPackageOf(monsterElement).getSimpleName().toString(),
            format("%sMonsterBuilder", monsterTargetElement.getSimpleName()));

    final List<MethodSpec> methods =
        this.limbRetriever.retrieve(monsterTargetElement).stream()
            .map(
                e -> {
                  final String methodName = e.getSimpleName().toString();

                  final ParameterSpec parameter =
                      ParameterSpec.builder(
                              TypeName.get(((ExecutableElement) e).getReturnType()), methodName)
                          .addModifiers(Modifier.FINAL)
                          .build();

                  return MethodSpec.methodBuilder(methodName)
                      .addModifiers(Modifier.PUBLIC)
                      .addParameter(parameter)
                      .addStatement(
                          "this.setValue($T.$N, $N)",
                          schemaClassName,
                          createEnumConstantName(e),
                          parameter)
                      .addStatement("return this")
                      .returns(monsterClassName);
                })
            .map(MethodSpec.Builder::build)
            .collect(Collectors.toList());

    final TypeSpec typeSpec =
        TypeSpec.classBuilder(monsterClassName)
            .superclass(
                ParameterizedTypeName.get(
                    ClassName.get(MonsterBuilder.class),
                    TypeName.get(monsterTargetElement.asType())))
            .addModifiers(Modifier.PUBLIC)
            .addMethod(
                MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addStatement("super($T.class)", monsterElement)
                    .build())
            .addMethod(
                MethodSpec.methodBuilder(monsterTargetElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .addStatement("return new $T()", monsterClassName)
                    .returns(monsterClassName)
                    .build())
            .addMethods(methods)
            .build();
    try {
      JavaFile.builder(
              this.elementUtils.getPackageOf(monsterElement).getSimpleName().toString(), typeSpec)
          .build()
          .writeTo(this.filer);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private String createEnumConstantName(Element e) {
    return e.getSimpleName().toString().toUpperCase();
  }
}
