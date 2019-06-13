package com.github.kidsoncoffee.monsters.processor;

import com.github.kidsoncoffee.monsters.Monster;
import com.github.kidsoncoffee.monsters.MonsterBuilder;
import com.github.kidsoncoffee.monsters.MonsterLimbRetriever;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.asList;

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
            this.elementUtils.getPackageOf(monsterElement).toString(),
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

    final AnnotationValueVisitor<Object, Object> annotationValueVisitor =
        new AnnotationValueVisitor<Object, Object>() {
          @Override
          public Object visit(AnnotationValue av, Object o) {
            return null;
          }

          @Override
          public Object visit(AnnotationValue av) {
            return null;
          }

          @Override
          public Object visitBoolean(boolean b, Object o) {
            return null;
          }

          @Override
          public Object visitByte(byte b, Object o) {
            return null;
          }

          @Override
          public Object visitChar(char c, Object o) {
            return null;
          }

          @Override
          public Object visitDouble(double d, Object o) {
            return null;
          }

          @Override
          public Object visitFloat(float f, Object o) {
            return null;
          }

          @Override
          public Object visitInt(int i, Object o) {
            return null;
          }

          @Override
          public Object visitLong(long i, Object o) {
            return null;
          }

          @Override
          public Object visitShort(short s, Object o) {
            return null;
          }

          @Override
          public Object visitString(String s, Object o) {
            return null;
          }

          @Override
          public Object visitType(TypeMirror t, Object o) {
            return null;
          }

          @Override
          public Object visitEnumConstant(VariableElement c, Object o) {
            return null;
          }

          @Override
          public Object visitAnnotation(AnnotationMirror a, Object o) {
            return null;
          }

          @Override
          public Object visitArray(List<? extends AnnotationValue> vals, Object o) {
            return vals.stream()
                .map(e -> ((AnnotationValue) e).getValue())
                .collect(Collectors.toList());
          }

          @Override
          public Object visitUnknown(AnnotationValue av, Object o) {
            return null;
          }
        };
    final TypeMirror monsterType =
        this.elementUtils.getTypeElement(Monster.class.getCanonicalName()).asType();
    final List<Object> defaultGenerators =
        monsterElement.getAnnotationMirrors().stream()
            .filter(e -> this.typeUtils.isSameType(monsterType, e.getAnnotationType()))
            .flatMap(e -> e.getElementValues().entrySet().stream())
            .filter(e -> e.getKey().getSimpleName().toString().equals("defaultGenerators"))
            .map(Map.Entry::getValue)
            .map(e -> e.accept(annotationValueVisitor, null))
            .map(List.class::cast)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    final String constructorExpression =
        String.format(
            "super($T.class, asList($T.values()), asList(%s))",
            defaultGenerators.stream().map(d -> "$T.class").collect(Collectors.joining(", ")));
    final List<List> constructorParameters =
        ImmutableList.of(asList(monsterElement, schemaClassName), defaultGenerators);

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
                    .addStatement(
                        constructorExpression,
                        constructorParameters.stream().flatMap(Collection::stream).toArray())
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
      JavaFile.builder(this.elementUtils.getPackageOf(monsterElement).toString(), typeSpec)
          .addStaticImport(Arrays.class, "asList")
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
