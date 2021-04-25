package willscala.laziness

import com.wbillingsley.veautiful.templates._
import willscala.Common._

val lazyListDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Lazy, Strict, and By-Name
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """## Lazy vs Strict
      |
      |Sometimes, a program might describe an expression whose value is never used.
      |
      |<dl>
      |  <dt>Strict</dt>
      |  <dd>the computation is evaluated once, as soon as it is declared</dd>
      |
      |  <dt>Lazy</dt>
      |  <dd>the computation is evaluated once, <em>only when it is used</em></dd>
      |</dl>
      |
      |Scala is mostly a strict language, but it includes a couple of keywords that can help us decide to be lazy.
      |
      |First, let's recap on how our program will behave if we define something as `def` or `val`.
      |
      |---
      |
      |## def
      |
      |Let's consider the following declaration:
      |
      |```scala
      |def myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |How many times will it print out `I was called` in the following code:
      |
      |```scala
      |println(myValue)
      |```
      |
      |---
      |
      |## def
      |
      |Let's consider the following declaration:
      |
      |```scala
      |def myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |How many times will it print out `I was called` in the following code:
      |
      |```scala
      |println(myValue)
      |println(myValue)
      |```
      |
      |---
      |
      |## def
      |
      |Let's consider the following declaration:
      |
      |```scala
      |def myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |How many times will it print out `I was called` in the following code:
      |
      |```scala
      |// empty
      |```
      |
      |---
      |
      |## val
      |
      |Let's change the definition:
      |
      |```scala
      |val myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |How many times will it print out `I was called` in the following code:
      |
      |```scala
      |println(myValue)
      |```
      |
      |---
      |
      |## val
      |
      |Let's change the definition:
      |
      |```scala
      |val myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |How many times will it print out `I was called` in the following code:
      |
      |```scala
      |println(myValue)
      |println(myValue)
      |```
      |
      |---
      |
      |## val
      |
      |Let's change the definition:
      |
      |```scala
      |val myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |How many times will it print out `I was called`:
      |
      |```scala
      |// empty
      |```
      |
      |---
      |
      |### Laziness
      |
      |What if we want it to run the calculation *exactly once*, but *only if it's needed*?
      |
      |```scala
      |lazy val myValue:Int =
      |  println("I was called")
      |  3
      |```
      |
      |Now, this will print `"I was called"` once:
      |
      |```scala
      |println(myValue)
      |```
      |
      |So will this
      |
      |```scala
      |println(myValue)
      |println(myValue)
      |```
      |
      |But this won't print it at all
      |
      |```scala
      |// empty
      |```
      |
      |---
      |
      |### Laziness
      |
      |Laziness is useful
      |
      |* We can declare a value that might involve a long computation
      |
      |* It can be evaluated *exactly once*, only when needed, and *never recalculated*
      |
      |---
      |
      |### Infinite Lists
      |
      |Suppose I have an unknown mathematical function `f: Int => Boolean`
      |
      |I would like to find the second occurrance in the natural numbers where `f(x)` is true.
      |
      |```scala
      |// ???
      |val naturals = (1 to infinity).toList
      |naturals.filter(f)(2)
      |```
      |
      |Even if this compiled, it would give us some problems
      |
      |* We can't really instantiate the whole list from 1 to infinity
      |
      |* We don't know how much of the list is "enough" to get our second value, to instantiate anything smaller than the whole list.
      |
      |---
      |
      |### Lazy lists
      |
      |* We need a list-like structure that'll *generate* the next value lazily as long as we keep asking for it
      |
      |* That sounds a lot like `lazy val` but for more than one value
      |
      |I wonder if there's an equivalent for `def` for more than one value?
      |
      |---
      |
      |### By-value and By-name
      |
      |Earlier, we defined
      |
      |* `def myValue`
      |* `val myValue`
      |*  `lazy val myValue`
      |
      |What if we could do that for *function arguments*?
      |
      |---
      |
      |### By value function arguments
      |
      |```scala
      |def myValue =
      |  println("myValue was calculated")
      |  3
      |```
      |
      |How many times is `myValue was calculated` printed in this:
      |
      |```
      |def myFunction(i:Int) =
      |  println(s"$i + 1 == ${i + 1}")
      |  println(s"$i + 2 == ${i + 2}")
      |
      |myFunction(myValue)
      |```
      |
      |---
      |
      |### By name function arguments
      |
      |```scala
      |def myValue =
      |  println("myValue was calculated")
      |  3
      |```
      |
      |How many times is `myValue was calculated` printed in this:
      |
      |```
      |def myFunction(i: => Int) =
      |  println(s"$i + 1 == ${i + 1}")
      |  println(s"$i + 2 == ${i + 2}")
      |
      |myFunction(myValue)
      |```
      |
      |---
      |
      |### By-name, with a lazy val assignment
      |
      |```scala
      |def myValue =
      |  println("myValue was calculated")
      |  3
      |```
      |
      |How many times is `myValue was calculated` printed in this:
      |
      |```
      |def myFunction(_i: => Int) =
      |  lazy val i = _i
      |  println(s"$i + 1 == ${i + 1}")
      |  println(s"$i + 2 == ${i + 2}")
      |
      |myFunction(myValue)
      |```
      |
      |---
      |
      |### By-name, with a lazy val assignment
      |
      |```scala
      |def myValue =
      |  println("myValue was calculated")
      |  3
      |```
      |
      |How many times is `myValue was calculated` printed in this:
      |
      |```
      |def myFunction(_i: => Int) =
      |  lazy val i = _i
      |  // nothing else
      |
      |myFunction(myValue)
      |```
      |
      |---
      |
      |### Lazy List
      |
      |Using this machinery, we can build up a `LasyList`
      |
      |Let's start with a *strict* version, and see how we can change it
      |
      |---
      |
      |### LList: a strict list
      |
      |Once again, lets define a list as either the empty list, or a cons element with a head and a tail.
      |
      |This time, I haven't used a case class.
      |
      |```scala
      |sealed trait LList[+T]:
      |  def head:T
      |  def tail:LList[T]
      |  def apply(n:Int):T
      |
      |object LNil extends LList[Nothing]:
      |  def head = throw new NoSuchElementException
      |  def tail = throw new NoSuchElementException
      |  def apply(n:Int) = throw new IndexOutOfBoundsException
      |  override def toString: String = "LNil"
      |
      |class LCons[T](val head:T, val tail:LList[T]) extends LList[T]:
      |  def apply(n:Int) = if n == 0 then head else tail.apply(n - 1)
      |  override def toString: String = s"($head, $tail)"
      |```
      |
      |---
      |
      |### A range function to test it with
      |
      |Let's define a *range* function that will generate the natural numbers between two values
      |*and print them out when they are calculated*
      |
      |```scala
      |def range(from:Int, to:Int):LList[Int] =
      |  if (to < from)
      |    LNil
      |  else
      |    println(s"Created element for $from")
      |    LCons(from, range(from + 1, to))
      |```
      |
      |---
      |
      |### Let's test the by-value version
      |
      |```scala
      |  val bnRange = Lst.range(1, 6)
      |  println(bnRange.apply(1))
      |```
      |
      |```
      |Created element for 1
      |Created element for 2
      |Created element for 3
      |Created element for 4
      |Created element for 5
      |Created element for 6
      |2
      |```
      |
      |---
      |
      |### Let's change LCons to be by-name
      |
      |```scala
      |class LCons[T](h: => T, t: => LList[T]) extends LList[T]:
      |  def head = h
      |  def tail = t
      |  def apply(n:Int) = if n == 0 then head else tail.apply(n - 1)
      |  override def toString: String = s"($head, $tail)"
      |```
      |
      |The constructor parameter *t* is by-name, and the trait function *tail* is defined as returning it.
      |
      |---
      |
      |### Let's test the by-name version
      |
      |```scala
      |  val bnRange = Lst.range(1, 6)
      |  println(bnRange.apply(1))
      |```
      |
      |```
      |Created element for 1
      |Created element for 2
      |2
      |```
      |
      |What's happening? Let's look at range again:
      |
      |```scala
      |LCons(from, range(from + 1, to))
      |```
      |
      |The second argument is by-name, and bnRange.tail.tail was never asked for. So, `range(3, ...)` was never called.
      |
      |---
      |
      |### But there's a problem
      |
      |```scala
      |  val bnRange = Lst.range(1, 6)
      |  println(bnRange.apply(1))
      |  println(bnRange.apply(1))
      |```
      |
      |```
      |Created element for 1
      |Created element for 2
      |2
      |Created element for 2
      |2
      |```
      |
      |We're *re-calculating* the tail every time we ask for it
      |
      |---
      |
      |### By-name with a lazy val
      |
      |This fixes our problem:
      |
      |```scala
      |class LCons[T](h: => T, t: => LList[T]) extends LList[T]:
      |  lazy val head = h
      |  lazy val tail = t
      |  def apply(n:Int) = if n == 0 then head else tail.apply(n - 1)
      |  override def toString: String = s"($head, $tail)"
      |```
      |
      |By making the tail a *lazy val*, it is calculated exactly once and only if asked for
      |
      |---
      |
      |### The lazy val works
      |
      |```scala
      |  val bnRange = Lst.range(1, 6000000)
      |  println(bnRange.apply(1))
      |  println(bnRange.apply(1))
      |```
      |
      |```
      |Created element for 1
      |Created element for 2
      |2
      |2
      |```
      |
      |---
      |
      |### But here's a different problem
      |
      |```scala
      |  val bnRange = Lst.range(1, 6)
      |  println(bnRange)
      |```
      |
      |```
      |(1, (2, (3, (4, (5, (6, Nl))))))
      |```
      |
      |Println causes everything to be generated. Just as well I didn't put in six million.
      |
      |---
      |
      |## Fixing println
      |
      |To fix the problem with println, we can give the tail expression a side-effect of remembering that it has
      |been evaluated
      |
      |```scala
      |class LCons[T](h: => T, t: => LList[T]) extends LList[T]:
      |  private var tailCalculated = false
      |  lazy val head = h
      |  lazy val tail = {
      |    tailCalculated = true
      |    t
      |  }
      |
      |  def apply(n:Int) = if n == 0 then head else tail.apply(n - 1)
      |  override def toString: String = s"($head, ${if tailCalculated then tail else "?"})"
      |```
      |
      |
      |---
      |
      |### LazyList in Scala
      |
      |`LazyList` is a lazy list in the Scala standard library
      |
      |```scala
      |println(LazyList.range(1, 6000000))
      |```
      |
      |produces
      |
      |```
      |LazyList(<not computed>)
      |```
      |
      |---
      |
      |### Infinite lazy lists
      |
      |Suppose I have an unknown mathematical function `f: Int => Boolean`
      |
      |I would like to find the second occurrance in the natural numbers where `f(x)` is true.
      |
      |```scala
      |// Yes, this works
      |val naturals = LazyList.from(1)
      |naturals.filter(f)(1)
      |```
      |
      |---
      |
      |### Infinite lazy lists
      |
      |```scala
      |  def f(i:Int) = i % 60 == 0
      |
      |  val naturals = LazyList.from(1)
      |
      |  println(naturals.filter(f)(1))
      |  println(naturals)
      |```
      |
      |```
      |120
      |LazyList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, <not computed>)
      |```
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
