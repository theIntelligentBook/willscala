package willscala.imperative

import com.wbillingsley.veautiful.templates._
import willscala.Common
import willscala.Common.willCcBy

val scalaOO = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Intro to Object Orientation in Scala
    """.stripMargin).withClass("center middle")
  .markdownSlide("""
      |
      |## Object-Oriented
      |
      |Scala is sometimes described as a fusion of *object-oriented programming* and *functional programming*. 
      |
      |If you're coming from a Java background, you'll probably already be familiar with OO. But there are some
      |extra features that Scala has.
      |
      |So, let's 
      |
      |* give a quick recap of what OO is, using a (slightly tired) analogy,
      |* and start to show you some of Scala's extra features
      |
      |""".stripMargin)
  .markdownSlide("""
      |### Classes and instances
      |
      |A dog is a kind of animal. Let's say there's a *class* called `Dog`.
      |
      |```scala
      |class Dog(name: String)
      |```
      |
      |I happen to own a dog -- a brown labrador called Rosie. Let's say that Rosie is an *instance* of the class `Dog`
      |
      |```scala
      |val rosie:Dog = Dog("Rosie")
      |```
      |
      |I could also say
      | 
      |```scala
      |val rosie = new Dog("Rosie")
      |```
      |
      |which would mean the same thing. From Scala 3, the word `new` is optional.
      |""".stripMargin)
  .markdownSlide("""
      |## Methods
      |
      |Let's assume that dogs are well-trained, and we can tell them to sit.
      |
      |```scala
      |class Dog(name:String):
      |
      |  def sit():Unit = ???
      |  
      |end Dog
      |```
      |
      |Notes: 
      |
      |* `???` is short for "throw a NotImplementedError"
      |* `end Dog` is optional, but it seemed a good place to show you "optional end markers" if you're not using 
      |  curly braces.
      |
      |In curly brace syntax, this would be:
      |
      |```scala
      |class Dog(name:String) {
      |
      |  def sit(): Unit = ???
      |
      |}
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Calling a method
      |
      |Once we've defined `Dog`s 
      |
      |```scala
      |class Dog(name:String):
      |  
      |  def sit():Unit = ???
      |```
      |
      |I could tell Rosie to sit
      |
      |```scala
      |val rosie = Dog("Rosie")
      |rosie.sit()
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |## Some things to notice:
      |
      |* Classes represent a kind of thing &mdash; they are an abstraction. We then have instances that represent actual things.
      |* Classes have constructor parameters. Unlike Java, they are not written as if they are a method, but as if the class definition itself takes parameters.
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Alternative constructors 
      |
      |As of Scala 3, we can define alternative constructors. Let's define one that takes a name from a set list
      |
      |```scala
      |val dogNames = List("Rosie", "Buster", "Hairy Maclairy")
      |
      |class Dog(name:String):
      |
      |  def this(i:Int) = this(dogNames(i))
      |
      |  def sit():Unit = ???
      |  
      |val r = Dog(1) // Buster
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |## Let's call Rosie...
      |
      |Dogs listen to what we say, only they only recognise a few words. But one of them is their name. 
      |So, let's change how we model telling Rosie to do things.
      |
      |```scala
      |class Dog(name:String):
      |
      |  def listen(word:String):Unit = 
      |    if (word == name) then
      |      println(s"$name looks at you")
      |      
      |end Dog
      |```
    """.stripMargin)
  .markdownSlide("""
      |### Some things to notice:
      |
      |* Constructor parameters stay in scope. the `listen` method can refer to the `name` parameter.
      |* `==` routes to Java's `equals()` method, but also handles nulls correctly.  
      |  So, what equality means for a class can be defined within the class itself, by how the `equals()` method is implemented.
      |  For example:
      |   
      |   * two words both saying "Rosie" should be equal.  `"Rosie" == "Rosie"`
      |   * Rosie the dog is not equal to another dog who is also called Rosie. So, `Dog("Rosie") != Dog("Rosie")`
      |   * But two lists just containing the word "Rosie" should be equal. So, `List("Rosie") == List("Rosie")`
      |  
      |  This is the difference between *structural equality* and *referential equality*.
      |  
    """.stripMargin)
  .markdownSlide("""
      |## Making Constructor arguments visible
      |
      |That constructor parameter, `name` is only in scope inside the class. 
      |
      |I can't get Rosie to tell me her name. But suppose Rosie was a wonderdog, and could -- how might we model that?
      |If we put `val` before the constructor parameter, then it also becomes externally accessible.
      |
      |```scala
      |class Dog(val name:String):
      |  // etc
      |
      |val rosie:Dog = new Dog("Rosie")
      |println(rosie.name)
      |```
      |
      |Also notice that I didn't need to say `System.out.println`. (Though I could have done that too.)
      |Scala has a class called `Predef` (predefined) that declares a number of things that are automatically in 
      |scope. `println` is one of them.
      |
    """.stripMargin)
  .markdownSlide("""
      |## Other fields
      |
      |Not all of the fields of our dog have to be in the constructor
      |
      |```scala
      |class Dog(val name:String):
      |  var age = 0
      |```
      |
      |By default, a field in a class is *public*. This means we could alter a dog's age.
      |
      |```scala
      |val r = Dog("Rosie")
      |r.age = 2
      |```
      |
      |Or we could mark the field private
      |
      |```scala
      |class Dog(val name:String):
      |  private var age = 0
      |```
      |
    """.stripMargin)
  .markdownSlide("""
      |## Singletons
      |
      |Scala has a special syntax for defining an *object* that you only ever want there to be one of:
      |
      |```scala
      |// There is only one universe!
      |object Universe 
      |```
      |
      |The object we decared here isn't part of a class. It has type `Universe.type`
      |
      |However, I could define methods and fields on it, and pass it around like any other object.
      |
      |```scala
      |object Universe:
      |  /** Gravitational constant */
      |  val bigG = 6.674e-11
      |```
    """.stripMargin)
  .markdownSlide("""
      |## Companion objects
      |
      |In Java, it's common to declare *static* methods that belong to the class definition.
      |
      |In Scala, instead, there is a *companion object*: an object that has the same name as the class.
      |
      |```scala
      |class Dog(val name:String)
      |  // etc
      |
      |object Dog:
      |  val commonNames = List("Rosie", "Buster", "Hairy Maclairy")
      |```
      |
      |Note, the companion object is *not* a member of the `Dog` class. Its type is `Dog.type` like any other singleton
      |object. (An object containing fields and methods that are relevant to dogs, like a list of common names, is not automatically a dog!)
    """.stripMargin)
  .markdownSlide("""
     |## `apply` methods on companion objects
     |
     |Often in Scala, programmers would define factory methods on the companion object.
     |So, we could do our "alternative constructor" example from earlier like this:
     |
     |```scala
     |class Dog(name:String)
     |
     |object Dog:
     |  val commonNames = List("Rosie", "Buster", "Hairy Maclairy")
     |  
     |  def apply(i:Int):Dog = new Dog(commonNames(i))
     |  
     |val r = Dog(2) // short for Dog.apply(2), produces Hairy Maclairy
     |```
     |
     |In the factory method, we do need to use the `new` keyword, roughly so the compiler knows you're calling the 
     |constructor rather than calling `apply` again.
     |""".stripMargin)
  .markdownSlide("""
      |## Traits
      |
      |Traits are similar to Java's interfaces. 
      |
      |```scala
      |trait HasLocation:
      |  def x:Double
      |  def y:Double
      |  
      |  def distanceToOrigin:Double = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
      |```
      |
      |Subclassing works using the `extends` keyword.
      |
      |```scala
      |class Circle(val x:Double, val y:Double, val r:Double) extends HasLocation
      |```
      |
      |Notice that the publicly accessible constructor values met the requirement to declare `x` and `y` methods.
      |Values can be treated as accessor methods.
      |
      |We can also define values and vars on traits
    """.stripMargin)
  .markdownSlide("""
      |## Traits and type parameters
      |
      |It can be useful to give a trait a type parameter. This is (almost) a type I quite often use:
      |
      |```scala
      |trait HasId[T, K]:
      |  def id:Id[T, K]
      |
      |class User extends HasId[User, String]
      |  def id:Id[User, String] = ???
      |```
      |
      |""".stripMargin)
  .markdownSlide(
      """## Enumerations
        |
        |Scala 3 introduces `enum`, which can declare a type that has a list of named values
        | 
        |```scala
        |enum Planet:
        |  case Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune
        |  
        |println(Planet.Earth.ordinal) // 2
        |println(Planet.fromOrdinal(5)) // Saturn
        |```
        |
        |In Scala, though, `enum` is a bit of syntactic sugar on defining a trait and a companion object. 
        |This means you might see them used for defining closed types of a few classes, rather than just things that 
        |have an order.
        |""".stripMargin)
  .markdownSlide(
      """## Using an `enum` for a `sealed trait`
        |
        |Let's suppose we're modelling a university, and we have some students and some courses.
        |Suppose also that we want to have *typed* id objects, so we can't accidentally use a course id where we should
        |use a user id.
        |
        |```scala
        |class Student
        |class Course
        |
        |enum Id[T, K]:
        |  case StudentId(id:K) extends Id[Student, K]
        |  case CourseId(id:K) extends Id[Course, K]
        |
        |val sid = Id.StudentId("123")
        |val cid = Id.CourseId("123")
        |
        |sid == cid // false, these are different
        |
        |val sid2:Id[Student, String] = Id.CourseId("456") // won't compile, it's the wrong type
        |```
        |""".stripMargin)
  .markdownSlide("""
      |## Applications
      |
      |In Java you might have
      |
      |```java
      |public class MyApp {
      |  public static void main(String... args) {
      |    // do stuff
      |  }
      |}
      |```
      |
      |In Scala, the direct equivalent would be
      |
      |```scala
      |object MyApp:
      |  def main(args:Array[String]):Unit = 
      |    // do stuff
      |```
      |
      |But from Scala 3, we can have a "top-level main method":
      |
      |```scala
      |@main def myMainMethod():Unit = ???
      |```
      |
      |This doesn't have to belong to an object. You'll see an example in the tutorial.
    """.stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
