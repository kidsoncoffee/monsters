package com.kidsoncoffee.monsters;

import java.util.List;
import java.util.Objects;

/**
 * @author fernando.chovich
 * @since 1.0
 */
public class ImmutableMonsterMemberSchema implements MonsterMember.Schema {

  private String name;

  private List<String> parameters;

  private boolean method;

  private boolean field;

  private boolean publicAccess;

  private boolean packagePrivate;

  public ImmutableMonsterMemberSchema(
      String name,
      List<String> parameters,
      boolean method,
      boolean field,
      boolean publicAccess,
      boolean packagePrivate) {
    this.name = name;
    this.parameters = parameters;
    this.method = method;
    this.field = field;
    this.publicAccess = publicAccess;
    this.packagePrivate = packagePrivate;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public List<String> getParameters() {
    return this.parameters;
  }

  @Override
  public boolean isMethod() {
    return this.method;
  }

  @Override
  public boolean isField() {
    return this.field;
  }

  @Override
  public boolean isPublic() {
    return this.publicAccess;
  }

  @Override
  public boolean isPackagePrivate() {
    return this.packagePrivate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ImmutableMonsterMemberSchema that = (ImmutableMonsterMemberSchema) o;
    return method == that.method
        && field == that.field
        && publicAccess == that.publicAccess
        && packagePrivate == that.packagePrivate
        && name.equals(that.name)
        && parameters.equals(that.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, parameters, method, field, publicAccess, packagePrivate);
  }
}
