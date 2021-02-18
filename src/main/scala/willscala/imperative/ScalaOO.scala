package willscala.imperative

import com.wbillingsley.veautiful.templates._
import willscala.Common

val scalaOO = DeckBuilder(1280, 720)
  .markdownSlide(
    """
      |# Intro to Object Orientation in Scala
    """.stripMargin).withClass("center middle")
  .markdownSlide("""
      |
      |## Object-Oriented
      |
      |Scala is *object-oriented*. Let's give a very quick recap of this, using a (slightly tired) analogy.
      |
      |A dog is a kind of animal. Let's say there's a *class* called `Dog`.
      |
      |```scala
      |class Dog(name: String)
      |```
      |
      |Let's assume that dogs are well-trained, and we can tell them to sit.
      |
      |```scala
      |class Dog(name: String) {
      |
      |   def sit():Unit = ???
      |
      |}
      |```
      |
      |--
      |
      |Note: `???` is short for "throw a NotImplementedError"
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Instances
      |
      |I happen to own a dog -- a brown labrador called Rosie. Let's say that Rosie is an *instance* of the class `Dog`
      |
      |```scala
      |val rosie:Dog = new Dog("Rosie")
      |```
      |
      |I could tell her to sit
      |
      |```scala
      |rosie.sit()
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Some things to notice:
      |
      |* Classes represent a kind of thing &mdash; they are an abstraction. We then have instances that represent actual things.
      |* Classes have constructor parameters. Unlike Java, they are not written as if they are a method, but as if the class definition itself takes parameters.
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Let's call Rosie...
      |
      |Dogs listen to what we say, only they only recognise a few words. But one of them is their name. So, let's change how we model telling Rosie to do things.
      |
      |```scala
      |class Dog(name:String) {
      |
      |   def listen(word:String):Unit = {
      |       if (word == name) {
      |           ???
      |       }
      |   }
      |}
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Some things to notice:
      |
      |* Constructor parameters stay in scope. the `listen` method can refer to the `name` parameter.
      |* `==` routes to Java's `equals()` method, but also handles nulls correctly. So, what equality means for a class can be defined on the class itself -- by how the `equals()` method is implemented.
      |  For example, two words both saying "rosie" should be equal. But Rosie the dog is not equal to another dog who is also called Rosie.
      |  
    """.stripMargin)
  .markdownSlide("""
      |
      |### Making Constructor arguments visible
      |
      |That constructor parameter, `name` is only in scope inside the class. I can't get Rosie to tell me her name. But suppose Rosie was a wonderdog, and could -- how might we model that?
      |Well, if we put `val` before the constructor parameter, then it also becomes externally accessible.
      |
      |```scala
      |class Dog(val name:String) {
      |  // etc
      |}
      |
      |val rosie:Dog = new Dog("Rosie")
      |println(rosie.name)
      |```
      |
      |Also notice that I didn't need to say `System.out.println`. (Though I could have done that too.)
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Other fields
      |
      |Not all of the fields of our dog have to be in the constructor
      |
      |```scala
      |class Dog(val name:String) {
      |  var age = 0
      |}
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Singletons
      |
      |Scala has a special syntax for defining an *object* that you only ever want there to be one of:
      |
      |```scala
      |object Universe {
      |  // There is only one universe!
      |}
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Companion objects
      |
      |In Java, it's common to declare *static* methods that belong to the class definition.
      |
      |In Scala, instead, there is a *companion object* -- an object that has the same name as the class.
      |
      |```scala
      |class Dog(val name:String) {
      |  // etc
      |}
      |
      |object Dog {
      |  // A place for static methods...
      |}
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Applications
      |
      |Just as in Java you might have
      |
      |```java
      |public class MyApp {
      |  public static void main(String... args) {
      |    // do stuff
      |  }
      |}
      |```
      |
      |the Scala equivalent is
      |
      |```scala
      |object MyApp {
      |  def main(args:Array[String]) = {
      |    // do stuff
      |  }
      |}
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Traits
      |
      |Traits are similar to Java's interfaces
      |
      |```scala
      |trait HasLocation {
      |  def x:Double
      |  def y:Double
      |}
      |```
      |
      |Subclassing works using the `extends` keyword.
      |
      |```scala
      |class Circle(var x:Double, var y:Double, var r:Double) extends HasLocation
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |
      |### Traits and type parameters
      |
      |It can be useful to give a trait a type parameter. This is (almost) a type I quite often use:
      |
      |```scala
      |trait HasId[T, K] {
      |  def id:Id[T, K]
      |}
      |
      |class User extends HasId[User, String] {
      |  def id:Id[User, String] = ???
      |}
      |```
      |""".stripMargin)
  .renderSlides
