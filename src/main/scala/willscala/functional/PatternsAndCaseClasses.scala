package willscala.functional

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}

val patternsAndCaseClasses = DeckBuilder(1280, 720)
  .markdownSlide(
    """
      |# Pattern Matching and Case Classes
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """## Immutable data
      |
      |Functional programmers tend to work with *immutable* data types - ones whose content cannot be modified.
      |For example:
      |
      |* `Option[T]` - an immutable item that may be `Some(item)` or `None`,
      |* `Map[K, V]` - an immutable map from keys to values,
      |* `List[T]` - an immutable singly-linked list,
      |* `Vector[T]` - a different structure for an immutable sequence,
      |* or classes that you define, or many more
      |
      |Functional programs also tend to take advantage of *pattern matching* - control logic that decides what to do by
      |inspecting inside a variable and seeing if it matches a pattern.
      |
      |Let's take a look at pattern matching, and along the way we'll see how we might model a deck of cards.
      |""".stripMargin)
  .markdownSlide("""
      |## `match` ... `case` expressions
      |
      |`match` considers a variable and lets us have some number of `case` statements that test against the variable.
      |The first `case` expression to match is the one that is evaluated.
      |
      |Let's start with a simple example just testing the value of an Int.
      |
      |```scala
      |def describe(x:Int):String = 
      |  x match 
      |    case 0 => "zero"
      |    case 1 => "one"
      |    case x if x > 0 && x % 2 == 0 => "something positive and even"
      |    case x if x < 0 && x % 2 == 1 => "something negative and odd"
      |    case _ => "something else"
      |
      |println(describe(9))
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """## Pattern matching and destructuring
      |
      |So far, pattern matching looks like a glorified if statement. 
      |However, we can also pattern match inside structures
      |
      |```scala
      |def describePosition(pos:(Int, Int)) = 
      |  pos match 
      |    case (x, y) if x > 0 && y > 0 => "top right quadrant"
      |    case (x, y) if x < 0 && y > 0 => "top left quadrant"
      |    case (x, y) if x > 0 && y < 0 => "bottom right quadrant"
      |    case (x, y) if x < 0 && y < 0 => "bottom left quadrant"
      |    case _ => "You're on an axis"
      |
      |println(describePosition((1, -1))
      |```
      |
      |Notice that when we said `case (x, y)`, it performed a *destructuring assignment* to extract `x` and `y` from the
      |tuple.
  """.stripMargin)
  .markdownSlide(
    """## A deck of cards...
      |
      |Let's model a deck of cards. First, let's just define the suits using an enum:
      |
      |```scala
      |enum Suit:
      |  case Clubs, Diamonds, Hearts, Spades
      |```
      |
      |We can alread see the keyword `case` appearing.
      |
      |In Scala, `enum` is some syntactic sugar over a trait, so this is roughly:
      |
      |```scala
      |sealed trait Suit:
      |  def ordinal:Int
      |
      |object Suit:
      |  case object Clubs extends Suit:
      |    def ordinal = 0
      |  
      |  case object Diamonds extends Suit:
      |    def ordinal = 1
      |    
      |  // etc
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """## Case objects
      |
      |Let's go back to our (shorter) `enum` definition
      |
      |```scala
      |enum Suit:
      |  case Clubs, Diamonds, Hearts, Spades
      |```
      |
      |The take-home message here is that `Suits.Clubs` etc are defined as *case objects*.
      |
      |We can use them in `match` expressions:
      |
      |```scala
      |import Suit._
      |
      |def colour(s:Suit):String = s match
      |  case Clubs | Spades => "black"
      |  case Diamonds | Hearts => "red"
      |```
      |
      |Yes, I just snuck in an `|` as an "or" in the cases!
      |""".stripMargin)
  .markdownSlide(
    """## Case Classes
      |
      |Let's define a card as a `case class`
      |
      |```scala
      |case class Card(value:Int, suit:Suit)
      |```
      |
      |Because it's a *case* class, Scala will auto-define a few things we might need:
      |
      |* It'll automatically make the constructor parameters public, as if we'd declared
      |  ```scala
      |  case class Card(val value:Int, val suit:Suit)
      |  ```
      |
      |* It'll define `equals` to use structural equality, so that for instance 
      |  ```scala 
      |  Card(1, Spades) == Card(1, Spades)
      |  ```
      |
      |* It'll aslo define `hashCode`, which is needed if you use it as a key in a map, so that  
      |  ```scala
      |  val map = Map(Card(1, Spades) -> "Old Frizzle") // nickname of an 1828 Ace of Spades
      |  map(Card(1, Spades)) == "Old Frizzle"
      |  ```
      |  
      |* It'll also define `toString` to produce something like `"Card(1, Spades)"`
      |
      |* It'll define `unapply`, which is the special method used behind the scenes for pattern matching
      |
      |""".stripMargin)
  .markdownSlide(
    """## Using our case class
      |
      |Here's our case class again:
      |
      |```scala
      |case class Card(value:Int, suit:Suit)
      |```
      |
      |We can use a destructuring assignment on it:
      |
      |```scala
      |val Card(value, suit) = card
      |```
      |
      |We can also pattern match on parts of the card. 
      |
      |```scala
      |def describe(card:Card):String = card match
      |  case Card(1, Spades) => "Old Frizzle"
      |  case Card(1, _) => "An ace"
      |  case Card(11, Spades) | Card(11, Hearts) => "A one-eyed Jack"
      |  case Card(x, _) if x > 10 => "A picture card"
      |  case Card(x, y) => s"Just a boring old $x of $y"
      |```
      |""".stripMargin
  )
  .markdownSlide(
    """## Something or nothing
      |
      |In Java, if you have a reference to an object, it could be `null`.
      |
      |```java
      |String name = null;
      |```
      |
      |The person who invented null references, Tony Hoare, (jokingly) calls it his [billion dollar mistake](https://www.infoq.com/presentations/Null-References-The-Billion-Dollar-Mistake-Tony-Hoare/)
      |because of how much productivity has been lost to null pointer exceptions.
      |
      |The problem with `null` is *any* reference can be null. We're not communicating whether the field can be null or not.
      |
      |Scale *does* have null, but by convention Scala programmers don't use it. Instead if a field can be something *or nothing*,
      |they'll declare it using `Option[T]`.
      |
      |`Option[T]` is a class in the standard library, but we can take a look at how we *could* define one ourselves.
      |""".stripMargin)
  .markdownSlide(
    """## Option
      |
      |An `Option[T]` is either
      |* `Some(value)`, or
      |* `None`
      |
      |Conceptually, this is a *disjoint union*. It's one thing or the other.
      |
      |Haskell's `Maybe` is defined as 
      |
      |```haskell
      |data Maybe = Just a | Nothing
      |```
      |
      |In Scala, we *could* do the same thing (we'll meet type aliases in a later week)
      |
      |```scala
      |case class Just[T](value:T)
      |case object Nothing
      |type Maybe[T] = Just[T] | Nothing.type
      |```
      |
      |That'd work, but Scala is object-oriented, so we'd like our Option class to have some methods!
      |""".stripMargin
  )
  .markdownSlide(
    """## A home-made `Option`
      |
      |If we wanted to make our own Option class, here's another way we could do it:
      |
      |```scala
      |enum Option[+T]:
      |  case None
      |  case Some(value:T)
      |```
      |
      |Now, `None` and `Some(value)` are both members of `Option[T]`, and we could define more methods on `Option`.
      |(We'll come back to what that `+` means in the type parameter in the types chapter.)
      |
      |In practice, `Option` is defined in much more detail, with lots of other methods, but hopefully this gives the
      |basic idea that:
      |
      |* `None` is a case object, and 
      |* `Some(value)` is a case class
      |""".stripMargin
  )
  .markdownSlide(
    """## Matching on Option
      |
      |Because `None` is a case object and `Some(value)` is a case class, we can use them in pattern matches.
      |
      |```scala
      |def playOrPass(opt:Option[Card]):String = 
      |  case Some(card) => s"You played a $card"
      |  case None => "You passed"
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Matching nested structures
      |
      |When we're matching a pattern, we don't have to stop at the top level.
      |
      |```scala
      |def describe(opt:Option[Card]):String = 
      |  opt match
      |    case Some(Card(1, Spades)) => "Old Frizzle"
      |    case Some(Card(_, Hearts)) | Some(Card(_, Diamonds)) => "A red card"
      |    case Some(Card(x, _)) if x > 10 => "A picture card"
      |    case Some(card) => "Just $card"
      |    case _ => "You didn't give me a card"
      |```
      |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides