package com.kidsoncoffee.monsters;

import com.google.auto.service.AutoService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;
import static javax.lang.model.SourceVersion.RELEASE_8;

/**
 * @author fernando.chovich
 * @since 1.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(RELEASE_8)
public class MonsterProcessor extends AbstractProcessor {

  @Inject private Types typeUtils;

  @Inject private MonsterLimbSchemaGenerator limbSchemaGenerator;

  @Inject private MonsterBuilderGenerator builderGenerator;

  @Override
  public synchronized void init(final ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    final MonsterProcessorModule module = new MonsterProcessorModule(processingEnv);
    Guice.createInjector(module).injectMembers(this);
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
        final Optional<ClassName> schemaClassName =
            this.limbSchemaGenerator.generate(monsterElement, monsterSetup);

        if (schemaClassName.isPresent()) {
          this.builderGenerator.generate(monsterElement, monsterSetup, schemaClassName.get());
          return true;
        }
        return false;
      }
    }

    return false;
  }

  // TODO fchovich MOVE THIS TO LIMB RETRIEVER
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
