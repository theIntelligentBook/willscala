package willscala.types


import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.{Common, Styles, styleSuite}
import Common.{marked, willCcBy}

import coderunner.JSCodable
import lavamaze.Maze

val variance = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Type hierarchies and variance
    """.stripMargin).withClass("center middle")
  .markdownSlide(
      """## Top types
        |
        |Let's define an unusual function and see what the type inferencer infers its type to be
        |
        |```scala
        |def awkward(i:Int) = if i > 0 then "bigger" else false
        |awkward // Int => Matchable
        |```
        |
        |The type inferencer will look at the two halves of the `if` and look for a common supertype. 
        |
        |`Matchable` is quite near the top, but the topmost type is `Any`. We can get that by using a type that isn't
        |a subtype of `Matchable`. `IArray` is an immutable array that can't be used safely in match expressions:
        |
        |```scala
        |def awkward2(i:Int) = if i > 0 then "bigger" else IArray(1, 2, 3)
        |awkward2 // Int => Any
        |```
        |""".stripMargin
  )
  .markdownSlide(
      """## Bottom types
        |
        |Let's define another unusual function and see what the type inferencer infers its type to be
        |
        |```scala
        |def doNothing(i:Int) = ???
        |awkward // Int => Nothing
        |```
        |
        |`Nothing` is a *bottom type* - it is "uninhabited" (there are no values of type `Nothing`) but it is a subtype of
        |every type.
        |
        |This is why we can, for instance, say
        |
        |```scala
        |def someFunction():Int = ???
        |```
        |
        |Although the function throws an error rather than returning a value, it type-checks because `Nothing` is a valid
        |subtype of `Int`.
        |""".stripMargin
  )
  .markdownSlide(
      """## Algebraic Data Types
        |
        |Algebraic data types are where we have a composite type formed by combining other types.
        |
        |One of the most common is *product types*, where a type contains fields of particular types. For instance:
        |
        |* Tuples, e.g. `(Int, String)`
        |* Records or classes, e.g. `case class Dog(name:String, age: Int)`
        |
        |""".stripMargin
  )
  .markdownSlide(
    """## Still writing...
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
