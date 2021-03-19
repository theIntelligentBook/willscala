package willscala.types

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.{Common, Styles, styleSuite}
import Common.{marked, willCcBy}

import coderunner.JSCodable
import lavamaze.Maze

val typeSystems = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Type Systems
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """## What are you talking about?
      |
      |Some actions only make sense on certain types of data. Even in natural language, this becomes apparent
      |
      |Let's start with some sums...
      |""".stripMargin)
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/1plus1.jpg"), <("figcaption")("1 + 1 = ?")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/2.jpg"), <("figcaption")("1 + 1 = 2")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/redplusgreen.jpg"), <("figcaption")("red + green = ?")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/yellow.jpg"), <("figcaption")("red + green = yellow")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/redplusrabbit.jpg"), <("figcaption")("red + rabbit = ?")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/redrabbit.jpg"), <("figcaption")("red + rabbit = a red rabbit")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/rabbitplusrabbit.jpg"), <("figcaption")("rabbit + rabbit = ?")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/babyrabbits.jpg"), <("figcaption")("rabbit + rabbit = lots of baby rabbits")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/1plusred.jpg"), <("figcaption")("1 + red = ?")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/1plusrabbit.jpg"), <("figcaption")("1 + rabbit = ?")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/oneplusone.jpg"), <("figcaption")(""""one" + "one" = ?""")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/two.jpg"), <("figcaption")(""""one" + "one" = "two"""")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .veautifulSlide(
    <.div(^.cls := "wrapper",
      <.img(^.src := "images/types/oneone.jpg"), <("figcaption")(""""one" + "one" = "oneone"""")
    )
  ).withClass(Styles.imageSlide.className + " cover")
  .markdownSlide(
    """## Types and operations
      |
      |Even in natural language, it makes sense to us that
      |
      |* Some operations only make sense on certain *types* of thing.
      |
      |* Some operations *mean* different things when applied to different types of thing
      |
      |This is also true in programming languages, but programming languages formally specify a *type system*.
      |
      |*Type errors* occur when a program tries to apply an operation to a type it is not valid for.
      |
      |*Type checking* is the process of checking for type errors
      |
      |""".stripMargin)
  .markdownSlide(
    """## Statically Typed Languages
      |
      |The source code you write is in a language written for humans. At some stage, it has to be translated into machine
      |operations.
      |
      |  In a **statically typed** language, the *compiler* (a program that converts your code from source code to a
      |  binary before you can run it) analyses the *type safety* of the program and will throw a *compile error* if
      |  it finds problems.
      |
      |  e.g.: Scala, Java, C++
      |
      |
      |  If there's a problem in your code, you might not be able to compile it, so you can't run it at all.
      |
      |  Typically, this means that variables and function arguments have types
      |
      |   ```scala
      |   var a:Int = 1
      |   a = "one"
      |   ```
      |
      |   In Scala, this would give a compile error. We cannot assign a string into variable of type `Int`.
      |""".stripMargin)
  .veautifulSlide(<.div(
    <.h2("Dynamically Typed Languages"),
    Challenge.split(
      marked(
        """In a **dynamically typed** language, the types of values and operations are checked when the program is run (at "run-time").
          |
          |e.g., JavaScript, Python
          |
          |```js
          |let a = 1
          |a = "one"
          |```
          |
          |JavaScript is happy with this. The *variable* (the name `a`) has no type. Only the *value* that is stored in it has a type.
          |
          |This means most type errors will occur at *run-time* -- you won't know you've got a bug until you hit it.
          |""".stripMargin
      )
    )(
      JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("Strong vs Weak Typing"),
    Challenge.split(
      marked(
        """Another way in which languages can differ is what they do if they encounter a type discrepancy
          |
          |For example, what is `1 + true`?
          |
          |* **Strongly typed** languages will throw an error
          |
          |   Scala: `None of the alternatives of + in class Int match arguments (true: Boolean)`
          |
          |* **Weakly typed** languages will try to find a *type conversion* that will satisfy the operation
          |
          |   JavaScript: `2`
          |
          |Because JavaScript is *dynamically*, *weakly* typed, mistakes in your program will sometimes show up
          |through the program behaving unexpectedly, rather than an error being thrown.
          |
          |""".stripMargin
      )
    )(
      JSCodable(Maze()((0,0), (0,0)) { _ => })(tilesMode = false)
    )
  ))
  .markdownSlide(
    """## Widening conversions
      |
      |Suppose I have
      |
      |```scala
      |def isSquare(x:Long):Boolean = Math.sqrt(x) % 1 == 0
      |```
      |
      |* Should I be able to call it with an `Int`?
      |* Should I be able to call it with a `BigNum`?
      |* Should I be able to call it with a `String`?
      |* Should I be able to call it with a `char`?
      |
      |How do we define what conversions are legitimate?
      |""".stripMargin
  )
  .markdownSlide(
    """## A conversion in the context...
      |
      |Some languages (Haskell, Scala, and others) have the idea that there can be some *implicit* items - that is, 
      |items that the compiler will find for you.
      |
      |In Scala, we can add extra items to the *context* using the `given` keyword. 
      |
      |To define a conversion we wish to consider legitimate, we can give Scala a conversion:
      |
      |```scala
      |case class DogName(s:String)
      |
      |given Conversion[String, DogName] with
      |  def apply(s:String) = new DogName(s)
      |  
      |val dn:DogName = "Rosie"
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Extending a type
      |
      |Something that can be common in dynamic languages is to add methods to a type, even ones we would not normally
      |think of extending such as Strings.
      |
      |```js
      |String.prototype.evenLetters = function() {
      |    let x = 0
      |    for (c of this.toUpperCase()) {
      |        let diff = c.charCodeAt(0) - 'A'.charCodeAt(0) - 1
      |        if (diff % 2 == 0) {
      |            x += 1
      |        }
      |    }
      |    
      |    return x
      |}
      |
      |console.log("Hello".evenLetters())
      |```
      |
      |However this can lead to bugs. Famous example: [Overriding the Array constructor in JS before 2007](https://bugzilla.mozilla.org/show_bug.cgi?id=376957) 
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Typesafe extensions
      |
      |Context gives us a typesafe way of doing this. For instance, in Scala we could define
      |
      |```scala
      |extension (s:String)
      |  def evenLetters:Int = s.count(c => c.charValue % 2 == 0)
      |
      |"Hello".evenLetters // 3
      |```
      |
      |Internally, this produces a function taking the object being extended as a paramenter
      |
      |```scala
      |evenLetters("Hello") // 3
      |```
      |
      |In the JavaScript case, the array constructor was altered for all callers, whether or not they intended to.
      |
      |A Scala extension method just creates an additional method that is only used at call sites where it is in the 
      |compiler context (e.g. imported).
      |""".stripMargin
  )
  .markdownSlide(
    """## Type parameters and extensions
      |
      |Extension methods can also take advantage of type parameters. 
      |
      |```scala
      |extension (nums:Seq[Double])
      |  def sd:Double = 
      |    val mean = nums.sum / nums.length
      |    val variance = nums.map(x => Math.pow(x - mean, 2)).sum / nums.length
      |    Math.sqrt(variance)
      |```
      |
      |Now if we have a list of doubles, we can get their standard deviation:
      |
      |```scala
      |List(1.3, 2.7, 1.6, 2.9, 5.1).sd // about 1.34
      |```
      |
      |But if we have a list of something else we'll get a compile error if we try to take their standard deviation
      |
      |```scala
      |List("Algernon", "Bertie", "Cecily").sd // value sd is not a member of List[String].
      |```
      |
      |But we could get a standard deviation of their lengths if we wanted...
      |
      |```scala
      |List("Algernon", "Bertie", "Cecily").map(_.length.toDouble).sd
      |```
      |""".stripMargin
  )
  .markdownSlide(
    """## Domain-Specific Languages
      |
      |Sometimes, this means it can be possible to define a domain-specific language for a particular purpose.
      |For instance, in `build.sbt`, dependencies use strings separated by `%` characters - these are functions.
      |
      |Just as a game, let's make `"one" plus "one" equals "two"` evaluate to `true`
      |
      |```scala
      |val strings = Seq("zero", "one", "two", "three", "four", "five")
      |
      |extension (s:String)
      |  def value = strings.indexOf(s)
      |
      |  def plus(o:String):String =
      |    strings(s.value + o.value) 
      |
      |"one" plus "one" equals "two"
      |```
      |
      |Here, we've used another little bit of Scala syntactic sugar, that 
      |
      |```scala
      |"one".plus("one")
      |```
      |
      |can also be written
      |
      |```scala
      |"one" plus "one"
      |```
      |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides