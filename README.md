<h1 align="center">
  <br>
  <img src="images/monsters_logo.png" alt="Monsters" width="400">
  <br>
  Monsters
  <br>
</h1>

<h4 align="center">When Object Mother meets random data generation.</h4>

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
  <a href="#quickstart">Quickstart</a> •
  <a href="#deep-dive">Deep dive</a> •
  <a href="#download">Download</a> •
  <a href="#related">Related</a>
</p>

## Key Features

* Generates data objects with preset random values
* Generates data objects following certain archetypes or profiles
* Provides builders to override random values.
* Easy to limbSetup, maintain, call and extend

## Quickstart

Let's go through a quick example of how **Monsters** can be used in your project.

### Import the dependency

First of all import the dependency into your project.

### Setup a *Monster* for your class

After having the dependency declared, you start by writing a limbSetup class for the *data object* class.

This class which we call the *Monster limbSetup*, will centralize every behavior related to generating data to that *data object*.

So for example if you want to generate data for a *data object* named `MyDataObject`, you have to create a class which extends from `MonsterSetup<MyDataObject>` and is annotated with `@Monster`.

### Type based data generation

The easiest and quickest of having a *data object* with values is by using type based data generation.

Following this strategy, the framework will generate random data according to the return type of a given method in your *data object*.

In our example, set `TypeBasedGenerator.class` on the parameter `defaultGenerators` in the `MonsterSetup` class:

### Custom data generation

Usually type based data generation is not enough. A couple of reasons are:

* The `TypeBasedGenerator` covers only native *Java* types (The framework is able to resolve another data objects recursively though)
* Random data inferred by type is void of meaning

That second one is a huge deal and you can change generation behavior very easily.

The `limbSetup` method of the `MonsterSetup` provides a `GeneratorBinder` and an instance of the *data object*. Using them both you can:

* Bind a generator that implements the very specific behavior you want
* Set a value stub to be returned every time

## Deep dive
## Download
## Related

TODO
* Exception and strict parameter when limb does not have value to generate
* Default implementations of the java faker
* Implement the rest of the primitive types
