package com.github.kidsoncoffee.monsters;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MonsterLimbRetriever {

    public List<? extends Element> retrieve(final Element monsterTargetElement) {
        return monsterTargetElement.getEnclosedElements().stream()
                .filter(e -> e.getKind().equals(ElementKind.METHOD) || e.getKind().isField())
                .filter(e -> !e.getModifiers().contains(Modifier.PRIVATE))
                .filter(e -> !e.getModifiers().contains(Modifier.PROTECTED))
                .collect(toList());
    }
}
