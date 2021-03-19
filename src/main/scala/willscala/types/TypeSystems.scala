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
  .markdownSlide("## Type hierarchies").withClass("center middle")
  .markdownSlide(
    """## Supertypes
      |
      |Let's define an unusual function and see what the type inferencer infers its type to be
      |
      |```scala
      |def awkward(i:Int) = if i > 0 then "bigger" else false
      |awkward // Int => Matchable
      |```
      |
      |The type inferencer will look at the two halves of the `if` and look for a common supertye
      |
      |* An `Int` is 
      |""".stripMargin
  )
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
      |How do we define what conversions are legitimate?
      |""".stripMargin
  )  
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides