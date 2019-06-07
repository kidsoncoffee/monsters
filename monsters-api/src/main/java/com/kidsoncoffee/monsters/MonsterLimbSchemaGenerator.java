package com.kidsoncoffee.monsters;

import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class MonsterLimbSchemaGenerator {

  private final Types typeUtils;

  private final Elements elementUtils;

  private final Filer filer;

  private final MonsterLimbRetriever limbRetriever;

  @Inject
  public MonsterLimbSchemaGenerator(
      final Types typeUtils,
      final Elements elementUtils,
      final Filer filer,
      final MonsterLimbRetriever limbRetriever) {
    this.typeUtils = typeUtils;
    this.elementUtils = elementUtils;
    this.filer = filer;
    this.limbRetriever = limbRetriever;
  }

  public Optional<ClassName> generate(
      final Element monsterElement, final Optional<? extends TypeMirror> monsterSetup) {
    final TypeMirror monsterSetupType = monsterSetup.get();

    final Element monsterTargetElement =
        this.typeUtils.asElement(
            this.typeUtils.capture(monsterSetupType).accept(new MonsterSetupTypeVisitor(), null));

    final ClassName schemaClassName =
        ClassName.get(
            this.elementUtils.getPackageOf(monsterElement).getSimpleName().toString(),
            format("%sMonsterSchema", monsterTargetElement.getSimpleName()));

    final TypeSpec.Builder schemaEnumBuilder =
        TypeSpec.enumBuilder(schemaClassName).addSuperinterface(MonsterMember.Schema.class);

    final ParameterizedTypeName parametersType =
        ParameterizedTypeName.get(List.class, String.class);

    final FieldSpec nameField = FieldSpec.builder(String.class, "name", Modifier.FINAL).build();
    final FieldSpec parametersField =
        FieldSpec.builder(parametersType, "parameters", Modifier.FINAL).build();

    schemaEnumBuilder.addField(nameField);
    schemaEnumBuilder.addField(parametersField);

    final ParameterSpec nameParameter =
        ParameterSpec.builder(String.class, "name", Modifier.FINAL).build();
    final ParameterSpec parametersParameter =
        ParameterSpec.builder(parametersType, "parameters", Modifier.FINAL).build();

    schemaEnumBuilder.addMethod(
        MethodSpec.methodBuilder("getName")
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addStatement("return this.$N", nameField)
            .build());

    schemaEnumBuilder.addMethod(
        MethodSpec.methodBuilder("getParameters")
            .addModifiers(Modifier.PUBLIC)
            .returns(parametersType)
            .addStatement("return this.$N", parametersField)
            .build());

    schemaEnumBuilder.addMethod(
        MethodSpec.constructorBuilder()
            .addParameter(nameParameter)
            .addParameter(parametersParameter)
            .addStatement("this.$N = $N", nameField, nameParameter)
            .addStatement("this.$N = $N", parametersField, parametersParameter)
            .build());

    final List<? extends Element> monsterAccessibles =
        this.limbRetriever.retrieve(monsterTargetElement);

    if (monsterAccessibles.isEmpty()) {
      return Optional.empty();
    }

    monsterAccessibles.stream()
        .map(
            e ->
                Pair.of(
                    createEnumConstantName(e),
                    TypeSpec.anonymousClassBuilder(
                            "$S, asList($L)",
                            e.getSimpleName().toString(),
                            Stream.empty().map(String.class::cast).collect(joining(", ")))
                        .build()))
        .forEach(c -> schemaEnumBuilder.addEnumConstant(c.getLeft(), c.getRight()));

    try {
      final TypeSpec schemaEnum = schemaEnumBuilder.build();
      JavaFile.builder(
              this.elementUtils.getPackageOf(monsterElement).getSimpleName().toString(), schemaEnum)
          .addStaticImport(Arrays.class, "asList")
          .build()
          .writeTo(this.filer);
      return Optional.of(schemaClassName);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private String createEnumConstantName(Element e) {
    return e.getSimpleName().toString().toUpperCase();
  }
}
