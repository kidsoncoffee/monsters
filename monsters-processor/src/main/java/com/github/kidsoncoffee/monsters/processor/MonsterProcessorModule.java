package com.github.kidsoncoffee.monsters.processor;

import com.google.inject.AbstractModule;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class MonsterProcessorModule extends AbstractModule {

  private final ProcessingEnvironment roundEnv;

  public MonsterProcessorModule(ProcessingEnvironment roundEnv) {
    this.roundEnv = roundEnv;
  }

  @Override
  protected void configure() {
    bind(Types.class).toInstance(this.roundEnv.getTypeUtils());
    bind(Elements.class).toInstance(this.roundEnv.getElementUtils());
    bind(Filer.class).toInstance(this.roundEnv.getFiler());
  }
}
