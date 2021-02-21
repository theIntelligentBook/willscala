package willscala.functional

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}

val patternsAndLists = DeckBuilder(1280, 720)
  .markdownSlide(
    """
      |# Pattern Matching and Lists
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
      |Let's take a look at pattern matching, and use `List[T]` as the type we'll work with.
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
    """## Lists, structurally
      |
      |`List[T]` is a singly linked immutable list. A list is either 
      |* `Nil`, the empty list, or
      |* `::(head:T, tail:List[T])`, a "cons" element with a head and a tail. Yes, the class is called `::`.
      |
      |So, `List(1, 2, 3)` is structurally
      |
      |```scala
      |::(1, ::(2, ::(3, Nil)))
      |```
      |
      |which by the power of some Scala syntactic sugar is the same as writing
      |
      |```scala
      |1 :: 2 :: 3 :: Nil
      |```
      |
      |Notice that the `tail` of a list is a `List[T]`.
      |""".stripMargin)
  .markdownSlide("""
      |## Pattern matching on a List 
      |
      |Just as we were able to pattern match inside tuples, we can also pattern match inside lists
      |
      |```scala
      |val list = 1 :: 2 :: 3 :: 4 :: Nil
      |list match {
      |    case Nil => "The list was empty"
      |    case a :: Nil => "It only had one element"
      |    case a :: b :: tail if b % 2 == 0 => "The second element was even"
      |    case _ => "The second element was odd"
      |}
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """### Let's define our own List type, ListInt
      |
      |```scala
      |sealed trait ListInt
      |case class ConsInt(head:Int, tail:ListInt) extends ListInt
      |case object EmptyListInt extends ListInt
      |```
      |
      |We'll see more of `case` later, but for the moment, it means we can use it in `match`/`case` expressions.
      |
      |--
      |
      |We can now define `ListInt` values, eg:
      |
      |```scala
      |val myList = ConsInt(1, ConsInt(2, ConsInt(3, EmptyListInt)))
      |```
      |
      |--
      |
      |* What is `myList.head`?
      |
      |--
      |
      |* What is `myList.tail`?
      |
      |--
      |
      |* What is `myList.tail.head`?
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Let's define a `length` function 
      |
      |```scala
      |def length(l:ListInt):Int = {
      |    ???
      |}
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Let's define a `length` function 
      |
      |```scala
      |def length(l:ListInt):Int = l match {
      |    case EmptyListInt => 0
      |    case ConsInt(head, tail) => ???
      |}
      |```
      |
      |--
      |
      |However many there are in tail, plus this one
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Let's define a `length` function 
      |
      |```scala
      |def length(l:ListInt):Int = l match {
      |    case EmptyListInt => 0
      |    case ConsInt(head, tail) => length(tail) + 1
      |}
      |```
      |
      |--
      |
      |Is `length` tail recursive?
      |
      |--
      |
      |No. Once the recursive call is made, the function still has to add one to the result.
      |It's not "in tail position"
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Tail recursion?
      |
      |Is `length` tail recursive now?
      |
      |```scala
      |def length(l:ListInt):Int = l match {
      |    case EmptyListInt => 0
      |    case ConsInt(head, tail) => 1 + length(tail)
      |}
      |```
      |
      |--
      |
      |Still no. It's not about being *lexically* at the end of the function. Once the recursive call is made, the function still has to add one to the result. It's still not "in tail position"
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Apply
      |
      |Let's consider something else. At the moment, we've got no way of indexing into a `ListInt`.
      |eg, we can't say `myList(3)`
      |
      |--
      |
      |```scala
      |trait ListInt {
      |  def apply(n:Int):Int = ???
      |}
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Apply
      |
      |Let's consider something else. At the moment, we've got no way of indexing into a `ListInt`.
      |eg, we can't say `myList(3)`
      |
      |```scala
      |trait ListInt {
      |  def apply(n:Int):Int = this match {
      |      case EmptyListInt => throw new IndexOutOfRangeException()
      |      case ConsInt(h, tail) => ???
      |  }
      |}
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Apply
      |
      |Let's consider something else. At the moment, we've got no way of indexing into a `ListInt`.
      |eg, we can't say `myList(3)`
      |
      |```scala
      |trait ListInt {
      |  def apply(n:Int):Int = this match {
      |      case EmptyListInt => throw new IndexOutOfRangeException()
      |      case ConsInt(h, tail) => if (n == 0) h else tail.apply(n - 1)
      |  }
      |}
      |```
      |
      |--
      |
      |Is this tail recursive?
      |
      |--
      |
      |Yes. If `tail.apply(n-1)` is called, its result can just be returned.
      |The recursive call is in tail position.
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Tail recursive?
      |
      |We can ask the compiler to verify with an annotation
      |
      |```scala
      |import scala.annotation.tailrec
      |
      |trait ListInt {
      |
      |  @tailrec
      |  def apply(n:Int):Int = this match {
      |      case EmptyListInt => throw new IndexOutOfRangeException()
      |      case ConsInt(h, tail) => if (n == 0) h else tail.apply(n - 1)
      |  }
      |}
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |class: center, middle
      |
      |### Examples of recursion
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Repeat the elements in a list
      |
      |--
      |
      |```scala
      |def repeat[T](l:List[T]):List[T] = l match {
      |    case h :: t => h :: h :: t
      |    case _ => _
      |}
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """
      |
      |### Select only every second element in a list
      |
      |--
      |
      |```scala
      |def alternate[T](l:List[T]):List[T] = l match {
      |    case a :: b :: tail => b :: alternate(tail)
      |    case _ => Nil
      |}
      |```
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides