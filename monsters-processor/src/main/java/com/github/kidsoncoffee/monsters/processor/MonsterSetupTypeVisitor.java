package com.github.kidsoncoffee.monsters.processor;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class MonsterSetupTypeVisitor implements TypeVisitor<TypeMirror, Object> {

  @Override
  public TypeMirror visitDeclared(DeclaredType t, Object o) {
    return t.getTypeArguments().get(0);
  }

  @Override
  public TypeMirror visit(TypeMirror t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visit(TypeMirror t) {
    return null;
  }

  @Override
  public TypeMirror visitPrimitive(PrimitiveType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitNull(NullType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitArray(ArrayType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitError(ErrorType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitTypeVariable(TypeVariable t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitWildcard(WildcardType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitExecutable(ExecutableType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitNoType(NoType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitUnknown(TypeMirror t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitUnion(UnionType t, Object o) {
    return null;
  }

  @Override
  public TypeMirror visitIntersection(IntersectionType t, Object o) {
    return null;
  }
}
