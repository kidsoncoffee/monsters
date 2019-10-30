THIS IS A WORK IN PROGRESS

<h1 align="center">
  <br>
  <img src="images/monsters_logo.png" alt="Monsters" width="400">
  <br>
  Monsters
  <br>
</h1>

<h4 align="center">Object Mother meets Random Data Generation</h4>

<p align="center">
  <a href="https://oss.sonatype.org/#nexus-search;quick~kidsoncoffee">
    <img alt="Sonatype Nexus (Releases)" src="https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.kidsoncoffee/monsters.svg?style=popout-square">
  </a>
  <a href="https://circleci.com/gh/kidsoncoffee/workflows/monsters">
      <img src="https://circleci.com/gh/kidsoncoffee/monsters.svg?style=svg"/>
  </a>
  <a href="https://www.codacy.com/app/fernandochovich/monsters?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kidsoncoffee/monsters&amp;utm_campaign=Badge_Grade">
      <img src="https://api.codacy.com/project/badge/Grade/d06b366b33a74e1ba180a44fe68d20cd"/>
  </a>
  <a href="https://github.com/kidsoncoffee/monsters/issues">
      <img src="https://img.shields.io/github/issues/kidsoncoffee/monsters.svg"/>
  </a>
  <a href="https://opensource.org/licenses/MIT">
      <img src="https://img.shields.io/badge/license-MIT-blue.svg"/>
  </a>
  <br/>
  <a href="#">
      <img src="https://img.shields.io/badge/contributions-welcome-orange.svg"/>
  </a>
  <a href="https://gitter.im/monsters-ddt">
    <img src="https://badges.gitter.im/monsters-ddt.svg"/>
  </a>
  <a href="https://saythanks.io/to/kidsoncoffee">
      <img src="https://img.shields.io/badge/SayThanks.io-%E2%98%BC-1EAEDB.svg"/>
  </a>
</p>

<p align="center">
  <a href="#key-features">Key features</a> •
  <a href="#motivation">Motivation</a> •
  <a href="#use-cases">Use cases</a> •
  <a href="#quickstart">Quickstart</a> •
  <a href="#download">Download</a> •
  <a href="#related">Related</a>
</p>

## Key Features

* Generates data objects with preset random values
* Generates data objects following certain archetypes or profiles
* Provides builders to override random values.
* Easy to limbSetup, maintain, call and extend

## Motivation

The framework is based on the following premises:

1. Tests are usually inputed with data objects.
1. Data objects are not mocked (you only mock dependency behavior).
1. On a test, what is expressed is exactly the necessary to make said test relevant in contrast to other tests.
1. If your data object expresses precisely the values the test needs, it is irrelevant what are the rest of the properties.
1. Then if a property is irrelevant for the test, it can be swaped by any other value without failing the test, and that's where *random data generation* comes into play.

## Use cases

1. You may use it as an audit tool for tests: assuming that a property is irrelevant the test shouldn't fail if the value is different at each test run.
1. You may want to create easy to use and reuse test data objects to simulate and create tests quicker
1. You may need to generate a large number of objects but you still need a variation of data and even something more close to real production data.

## Quickstart

Let's go through a quick example of how **Monsters** can be used in your project.

### Import the dependency

First of all import the dependency into your project.

#### Maven

```diff
<dependencies>

+  <dependency>
+    <groupId>com.github.kidsoncoffee</groupId>
+    <artifactId>monsters</artifactId>
+    <version>...</version>
+    <scope>test</scope>
+  </dependency>

</dependencies>
```

#### Gradle

```diff
dependencies {

+  testImplementation 'com.github.kidsoncoffee:monsters:...'

}
```

### Keywords

* **Monster:** We will call a *Monster* a *data object* configured to return pre-configured values from its *Limbs*.
* **Limb:** *Limbs* are methods (usually getters) that returns a pre-configured value.
* **Archetype:** An *Archetype* determines a kind of *Monster* and it's behavior. This is where the *Object Mother pattern* comes into play.

### Random data generation

In this example we will defaultSetup a *Monster* for the following data object.

```java
public class MyDataObject {
  public String getName() {
    return "DATA OBJECT NAME";
  }

  public int getNumber() {
    return 0;
  }

  public String getProfession() {
    return "DEVELOPER";
  }
}
```

The first step is creating a class which will centralize all information regarding that *Monster*. For the simplicity of this example, the class only needs to implement `Monster.Setup`.

```java
@MonsterOptions
public class MyDataObjectMonsterLimbSetup implements Monster.Setup<MyDataObject> {

  @Override
  public void defaultSetup(final MonsterLimb.Binding valueGeneratorBinding, final MyDataObject monster) {

  }

  @Override
  public void defaultSetup(final MonsterArchetype.Binding<MyDataObject> valueGeneratorBinding) {

  }
}
```

The `Monster.Setup` is a composite interface of `MonsterLimb.Setup` and `MonsterArchetype.Setup`. You may only use one of both, or even none, but let's just keep the methods's body empty for now.

#### Set up *Limbs's* value generation

The easiest way to set up data generation for a *Limb* is by using the `MonsterLimb.Setup` `defaultSetup` method.

```diff
@MonsterOptions
public class MyDataObjectMonsterLimbSetup implements Monster.Setup<MyDataObject> {

  @Override
  public void defaultSetup(final MonsterLimb.Binding valueGeneratorBinding, final MyDataObject monster) {
-
+     valueGeneratorBinding.on(monster.getName()).generate(() -> UUID.randomUUID().toString());
+     valueGeneratorBinding.on(monster.getNumber()).fix(42);
+     valueGeneratorBinding.on(monster.getProfession()).pickFrom(asList("Dream Alchemist", "Digital Dynamo"));
  }

  @Override
  public void defaultSetup(final MonsterArchetype.Binding<MyDataObject> valueGeneratorBinding) {

  }
}
```

The example above shows all the possible set ups for generating data:
* Supplying data following a specific logic
* Fixing a value to be returned
* Picking up a random value from a list of options

A very important concept here is that the *Monster* will never return a null value unless explicitly configured. Saying that, if there is a *Limb* in the *Monster* without any generation valueGeneratorBinding, an exception will be thrown when called.

#### Set up *Monster* default generator

There is a way of setting up more generic generators.

```diff
- @MonsterOptions
+ @MonsterOptions(fallbackValueGenerators = TypeBasedDefaultGenerator.class)
public class MyDataObjectMonsterLimbSetup implements Monster.Setup<MyDataObject> {

  @Override
  public void setup(final MonsterLimb.Binding valueGeneratorBinding, final MyDataObject monster) {
    valueGenera torBinding.on(monster.getName()).generate(() -> UUID.randomUUID().toString());
    valueGeneratorBinding.on(monster.getNumber()).fix(42);
    valueGeneratorBinding.on(monster.getProfession()).pickFrom(asList("Dream Alchemist", "Digital Dynamo"));
  }

  @Override
  public void setup(final MonsterArchetype.Binding<MyDataObject> valueGeneratorBinding) {

  }
}
```

In the example above the default generator specified is the `TypeBaseDefaultGenerator` which generates random values for a *Limb* according to its type. It is possible to specify more than one default generator and the framework will given priority to the most right declared class.

### Object Mother pattern

It's easy to create objects following the [**Object Mother pattern**](https://martinfowler.com/bliki/ObjectMother.html). This is done by setting up *Archetypes*.

```diff
@MonsterOptions(fallbackValueGenerators = TypeBasedDefaultGenerator.class)
public class MyDataObjectMonsterLimbSetup implements Monster.Setup<MyDataObject> {

+   public static final MonsterArchetype.Schema PARENT = ImmutableMonsterArchetypeSchema.builder()
+     .description("PARENT")
+     .build();
+
+   public static final MonsterArchetype.Schema CHILD = ImmutableMonsterArchetypeSchema.builder()
+     .description("CHILD")
+     .extendsFrom(PARENT)
+     .build();

  @Override
  public void setup(final MonsterLimb.Binding valueGeneratorBinding, final MyDataObject monster) {
    valueGeneratorBinding.on(monster.getName()).generate(() -> UUID.randomUUID().toString());
    valueGeneratorBinding.on(monster.getNumber()).fix(42);
    valueGeneratorBinding.on(monster.getProfession()).pickFrom(asList("Dream Alchemist", "Digital Dynamo"));
  }

  @Override
  public void setup(final MonsterArchetype.Binding<MyDataObject> valueGeneratorBinding) {
-  
+     valueGeneratorBinding.when(PARENT).setup(
+       (generation, monster) -> {
+         generation.on(monster.getName()).fix("Mr. Parent");
+         generation.on(monster.getNumber()).fix(666);
+       }
+     );
+     valueGeneratorBinding.when(CHILD).setup(
+       (generation, monster) -> {
+         generation.on(monster.getName()).fix("Kiddo");
+       }
+     );
  }
}
```

In the example above, it was created two archetypes:
* **Parent**: Have fixed values for name and number.
* **Child**: Have fixed values for name but extends from **Parent**.

The default fallback for an archetype, unless specified otherwise, is the default archetype and that means what is configured on the `setup(final MonsterLimb.Binding valueGeneratorBinding, final MyDataObject monster)` method.

So let's invoke the three archetypes and see how the values were generated:

```java
// The default archetype
MyDataObjectMonster.myDataObject().build(); 

// name: a random UUID
// number: 42
// profession: one of the three professions
```

```java
// The parent archetype
MyDataObjectMonster.myDataObject().build(MyDataObjectMonsterLimbSetup.PARENT); 

// name: Mr. Parent
// number: 666
// profession: one of the three professions
```

```java
// The child archetype
MyDataObjectMonster.myDataObject().build(MyDataObjectMonsterLimbSetup.CHILD); 

// name: Kiddo
// number: 666
// profession: one of the three professions
```

## Download
## Related

TODO
* Exception and strict parameter when limb does not have value to generate
* Default implementations of the java faker
* Implement the rest of the primitive types
