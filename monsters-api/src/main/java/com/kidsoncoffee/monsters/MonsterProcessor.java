package com.kidsoncoffee.monsters;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.SourceVersion.RELEASE_8;

/**
 * @author fernando.chovich
 * @since 1.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(RELEASE_8)
public class MonsterProcessor extends AbstractProcessor {

  private Types typeUtils;
  private Filer filer;
  private Elements elementUtils;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    this.filer = processingEnv.getFiler();
    this.elementUtils = processingEnv.getElementUtils();
    this.typeUtils = processingEnv.getTypeUtils();
  }

  @Override
  public boolean process(
      final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    final Set<? extends Element> monsterElements = roundEnv.getElementsAnnotatedWith(Monster.class);

    for (final Element monsterElement : monsterElements) {
      final Optional<? extends TypeMirror> monsterSetup =
          this.typeUtils.directSupertypes(monsterElement.asType()).stream()
              .filter(this::isMonsterSetupClass)
              .findFirst();

      if (monsterSetup.isPresent()) {
        final ClassName schemaClassName = this.createMonsterSchema(monsterElement, monsterSetup);
        this.createMonsterBuilder(monsterElement, monsterSetup, schemaClassName);
        return true;
      }
    }

    return false;
  }

  private ClassName createMonsterSchema(
      final Element monsterElement, final Optional<? extends TypeMirror> monsterSetup) {
    final TypeMirror monsterSetupType = monsterSetup.get();

    final Element monsterTargetElement =
        this.typeUtils.asElement(
            this.typeUtils.capture(monsterSetupType).accept(new MonsterSetupTypeVisitor(), null));

    final ClassName schemaClassName =
        ClassName.get(
            this.elementUtils.getPackageOf(monsterElement).getSimpleName().toString(),
            format("%sMonsterSchema", monsterTargetElement.getSimpleName()));
    final TypeSpec.Builder schemaEnumBuilder = TypeSpec.enumBuilder(schemaClassName);

    final ParameterizedTypeName parametersType =
        ParameterizedTypeName.get(List.class, String.class);

    final FieldSpec memberField =
        FieldSpec.builder(MonsterMember.Schema.class, "member", Modifier.FINAL).build();
    schemaEnumBuilder.addField(memberField);

    schemaEnumBuilder.addMethod(
        MethodSpec.methodBuilder("getMember")
            .addModifiers(Modifier.PUBLIC)
            .returns(MonsterMember.Schema.class)
            .addStatement("return this.$N", memberField)
            .build());

    final ParameterSpec nameParameter =
        ParameterSpec.builder(String.class, "name", Modifier.FINAL).build();
    final ParameterSpec parametersParameter =
        ParameterSpec.builder(parametersType, "parameters", Modifier.FINAL).build();
    final ParameterSpec isFieldParameter =
        ParameterSpec.builder(boolean.class, "isField", Modifier.FINAL).build();
    final ParameterSpec isMethodParameter =
        ParameterSpec.builder(boolean.class, "isMethod", Modifier.FINAL).build();
    final ParameterSpec isPublicParameter =
        ParameterSpec.builder(boolean.class, "isPublic", Modifier.FINAL).build();
    final ParameterSpec isPackagePrivateParameter =
        ParameterSpec.builder(boolean.class, "isPackagePrivate", Modifier.FINAL).build();

    schemaEnumBuilder.addMethod(
        MethodSpec.constructorBuilder()
            .addParameter(nameParameter)
            .addParameter(parametersParameter)
            .addParameter(isFieldParameter)
            .addParameter(isMethodParameter)
            .addParameter(isPublicParameter)
            .addParameter(isPackagePrivateParameter)
            .addStatement(
                "this.$N = new $T($N, $N, $N, $N, $N, $N)",
                memberField,
                ImmutableMonsterMemberSchema.class,
                nameParameter,
                parametersParameter,
                isFieldParameter,
                isMethodParameter,
                isPublicParameter,
                isPackagePrivateParameter)
            .build());

    this.retrieveMonsterAccessibles(monsterTargetElement)
        .map(
            e ->
                Pair.of(
                    createEnumConstantName(e),
                    TypeSpec.anonymousClassBuilder(
                            "$S, asList($L), $L, $L, $L, $L",
                            e.getSimpleName().toString(),
                            Stream.empty().map(String.class::cast).collect(joining(", ")),
                            e.getKind().equals(ElementKind.FIELD),
                            e.getKind().equals(ElementKind.METHOD),
                            e.getModifiers().contains(Modifier.PUBLIC),
                            !e.getModifiers().contains(Modifier.PUBLIC)
                                && !e.getModifiers().contains(Modifier.PRIVATE)
                                && !e.getModifiers().contains(Modifier.PROTECTED))
                        .build()))
        .forEach(c -> schemaEnumBuilder.addEnumConstant(c.getLeft(), c.getRight()));

    try {
      final TypeSpec schemaEnum = schemaEnumBuilder.build();
      JavaFile.builder(
              this.elementUtils.getPackageOf(monsterElement).getSimpleName().toString(), schemaEnum)
          .addStaticImport(Arrays.class, "asList")
          .build()
          .writeTo(this.filer);
      return schemaClassName;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private String createEnumConstantName(Element e) {
    return e.getSimpleName().toString().toUpperCase();
  }

  private void createMonsterBuilder(
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
        this.retrieveMonsterAccessibles(monsterTargetElement)
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
                          "this.setValue($T.$N.getMember(), $N)",
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

  private Stream<? extends Element> retrieveMonsterAccessibles(Element monsterTargetElement) {
    return monsterTargetElement.getEnclosedElements().stream()
        .filter(e -> e.getKind().equals(ElementKind.METHOD) || e.getKind().isField())
        .filter(e -> !e.getModifiers().contains(Modifier.PRIVATE))
        .filter(e -> !e.getModifiers().contains(Modifier.PROTECTED));
  }

  private boolean isMonsterSetupClass(TypeMirror superType) {
    final TypeMirror elementType = this.typeUtils.asElement(superType).asType();
    final String elementTypeErasure = this.typeUtils.erasure(elementType).toString();

    return elementTypeErasure.equals(Monster.Setup.class.getCanonicalName());
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return singleton(Monster.class.getCanonicalName());
  }
}
