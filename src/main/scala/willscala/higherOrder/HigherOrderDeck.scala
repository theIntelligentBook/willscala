package willscala.higherOrder

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}

val higherOrderDeck = DeckBuilder(1280, 720)
  .markdownSlide(
    """
      |# Higher Order Functions
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """## Functions as values
      |
      |So far, we've met functional programming in terms of *pure functions* -- functions that have no side-effects
      |
      |Another characteristic of functional languages is that functions are first class citizens. Functions can be values and they can be arguments
      |
      |A function that takes a function as an argument, or returns a function, is called a ***higher order function***
      |
      |""".stripMargin)
  .markdownSlide("""
      |### Functions as values
      |
      |Let's consider a function that doubles an integer
      |
      |```scala
      |def twice(i:Int):Int = 2 * i
      |```
      |
      |`twice` is a name for something, but what is it?
      |
      |It's a function from `Int => Int`
      |
      |""".stripMargin)
  .markdownSlide("""
      |### Functions as values
      |
      |We can't just say this (try it):
      |
      |```scala
      |val t = twice
      |```
      |
      |But we can say:
      |
      |```scala
      |val t:Int => Int = twice
      |``` 
      |
      |Our value `t` now contains the *function* twice, not the result of a call to twice.
      |""".stripMargin)
  .markdownSlide("""
      |### Functions as arguments
      |
      |If we can put a function into a value (or variable), can we pass it around?
      |
      |Let's see if we can apply it to every member of a list...
      |
      |```scala
      |def applyToSeq(func:Int => Int)(seq:Seq[Int]):Seq[Int] = {
      |  import scala.collection.mutable
      |
      |  val b = new mutable.ArrayBuffer[Int](seq.length)
      |  var i = 0;
      |  while(i < seq.length) do
      |    b.append(func(seq(i)))
      |    i += 1
      |  
      |  b
      |}
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Functions as arguments
      |
      |Let's try applying `twice` to `applyToSeq`
      |
      |```scala
      |applyToSeq(twice)(Seq(1, 2, 3))
      |```
      |
      |This should give us as output in the worksheet
      |
      |```scala
      |res2: Seq[Int] = ArrayBuffer(2, 4, 6)
      |```
      |
      |That seems rather useful. I wonder if we could use it to write *thrice* (three times) quickly?
      |
      |--
      |
      |```scala
      |applyToSeq((x:Int) => x * 3)(Seq(1, 2, 3))
      |```
      |
      |--
      |
      |or (shorthand):
      |
      |```scala
      |applyToSeq(_ * 3)(Seq(1, 2, 3))
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Map
      |
      |In fact, that is so useful that *it's already provided on the `Seq` class*
      |
      |Their implementation is a little more complex for reasons we won't get into
      |
      |```scala
      |  def map[B, That](f: A => B)(implicit bf: CanBuildFrom[Repr, B, That]): That = {
      |    def builder = { // extracted to keep method size under 35 bytes, so that it can be JIT-inlined
      |      val b = bf(repr)
      |      b.sizeHint(this)
      |      b
      |    }
      |    val b = builder
      |    for (x <- this) b += f(x)
      |    b.result
      |  }
      |```
      |
      |--
      |
      |You'll notice, their version is *locally mutable* -- it uses a builder
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Map
      |
      |Now that we've got our `map` function, some of the exercises from tutorial 1 seem rather trivial...
      |
      |```scala
      |def doubleArray(arr:Array[Int]):Array[Int] = arr.map(_ * 2)
      |```
      |
      |--
      |
      |Even though Scala uses native Java arrays, it enhances them to add some of its collections functions such as `map`
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Times position
      |
      |To do this once:
      |
      |```scala
      |  /**
      |    * Multiply every element in an array by its position in the array
      |    * eg, for [3, 4, 2, 6, 2] [3 * 0, 4 * 1, 2 * 2, 6 * 3, 2 * 4]
      |    */
      |  def timesPosition(arr:Array[Int]):Array[Int] = ???
      |```
      |
      |We've got the problem that we need the position in the array.
      |
      |There's a function for that...
      |
      |```scala
      |Seq("a", "b", "c").zipWithIndex
      |// List((a,0), (b,1), (c,2))
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Times Position
      |
      |```scala
      |  /**
      |    * Multiply every element in an array by its position in the array
      |    * eg, for [3, 4, 2, 6, 2] [3 * 0, 4 * 1, 2 * 2, 6 * 3, 2 * 4]
      |    */
      |  def timesPosition(arr:Array[Int]):Array[Int] = {
      |    arr.zipWithIndex.map((tup) => {
      |      val (x, i) = tup
      |      x * i
      |    })
      |  }
      |```
      |
      |That looks too long. This week we'll also see pattern matching...
      |
      |```scala
      |def timesPosition(arr:Array[Int]):Array[Int] = {
      |  arr.zipWithIndex.map({case (x,i) => x * i})
      |}
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Returning a function
      |
      |* We could say
      |
      |  ```scala
      |  def double(i:Int) = 2 * i
      |  def triple(i:Int) = 3 * i
      |  ```
      |
      |* Or we could say
      |
      |    ```scala
      |    def multiplyBy(x:Int) = { y:Int => x * y } 
      |    
      |    val double = multiplyBy(2)
      |    val triple = multiplyBy(3)
      |    ```
      |
      |  `{ y:Int => x * y }` is called a *lambda function* (we locally declared it without using the `def` keyword)
      |
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Ranges
      |
      |We'll use ranges in the next few examples
      |
      |An *inclusive* range uses `to`
      |
      |```scala
      |1 to 10 // Range.Inclusive = Range 1 to 10
      |// ie, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
      |```
      |
      |An *exclusive* range uses `until`
      |
      |```scala
      |0 until 10 // Range = Range 0 until 10
      |// ie, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
      |
      |```
      |
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Filter
      |
      |Suppose we have a range of numbers
      |
      |```scala
      |val range = 1 to 100
      |```
      |
      |How can we get all the even numbers out? You might find the `filter` method useful. It takes a predicate `p: A => Boolean`
      |
      |--
      |
      |```scala
      |range.filter(_ % 2 == 0)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## exists and forall
      |
      |Let's do a very inefficient prime check using `exists`. It also takes a predicate, and returns true if there exists within the collection a value for which it returns true. 
      |
      |```scala
      |def prime(x:Int) = {
      |  x > 1 &&
      |  !(2 until x).exists(x % _ == 0)
      |}
      |```
      |
      |or we could use *forall*
      |
      |```scala
      |def prime(x:Int) = {
      |  x > 1 &&
      |  (2 until x).forall(x % _ != 0)
      |}
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## The primes from 1 to 100
      |
      |Let's pass our check into filter to produce a (very inefficient) prime number generator:
      |
      |```scala
      |def prime(x:Int) = {
      |  x > 1 &&
      |  !(2 until x).exists(x % _ == 0)
      |}
      |
      |(1 to 100).filter(prime)
      |```
      |
      |Hopefully by now you're getting the idea that functional code can be *really small*
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## `for` as Syntactic sugar
      |
      |In week 1, I said `for` in Scala does something quite unique, but actually quite readable.
      |
      |Let's start with the simple version
      |
      |```scala
      |for { i <- 1 to 100 } yield i * 2
      |```
      |
      |This actually *translates into* 
      |
      |```scala
      |(1 to 100).map(_ * 2)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## `for` as Syntactic sugar
      |
      |But now let's do something just as readable but where the translation is a bit different
      |
      |```scala
      |def isPrime(x:Int) = {
      |  x > 1 &&
      |  !(2 until x).exists(x % _ == 0)
      |}
      |
      |for { i <- 1 to 100 if isPrime(i) } yield i
      |```
      |
      |The for "loop" translates into
      |
      |```scala
      |(1 to 100).filter(isPrime).map(identity)
      |```
      |
      |`identity` is the function `i => i`
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## `map` on `Seq`, `List`, etc
      |
      |Coming back to `map`, let's show it works on other collections
      |
      |```scala
      |val myList = 1 :: 2 :: 3 :: Nil
      |myList.map(_ * 2) // List(2, 4, 6)
      |```
      |
      |```scala
      |for { i <- myList } yield i * 2
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## What if my f also produces a seq?
      |
      |```scala
      |def nTimes(n:Int) = for { i <- 0 until n } yield n
      |```
      |
      |```scala
      |Seq(1, 2, 3).map(nTimes)
      |// List(Vector(1), Vector(2, 2), Vector(3, 3, 3))
      |```
      |
      |That's a `Seq[Seq[Int]]`. Let's *flatten* it
      |
      |```scala
      |Seq(1, 2, 3).map(nTimes).flatten
      |// List(1, 2, 2, 3, 3, 3)
      |```
      |
      |or, shorter...
      |
      |```scala
      |Seq(1, 2, 3).flatMap(nTimes)
      |// List(1, 2, 2, 3, 3, 3)
      |```
      |
      |For the moment, think of *flatMap* as *map* followed by *flatten*. 
      |But we'll see a more mathematical way of thinking about it later.
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## for syntactic sugar again...
      |
      |```scala
      |val nums = List(1, 2, 3)
      |val chars = List('a', 'b', 'c')
      |
      |for { num <- nums; char <- chars } yield s"$num $char"
      |// List(1 a, 1 b, 1 c, 2 a, 2 b, 2 c, 3 a, 3 b, 3 c) 
      |
      |```
      |
      |translates to
      |
      |```scala
      |nums.flatMap { num =>
      |  chars.map { char => 
      |    s"$num $char"
      |  }
      |}
      |// List(1 a, 1 b, 1 c, 2 a, 2 b, 2 c, 3 a, 3 b, 3 c)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Matching Letters
      |
      |```scala
      |  /**
      |    * Suppose we are compiling a crossword. Given two words, find all the pairs of positions where those
      |    * words have letters in commong. eg, for "frogs" and "eggs", we would return
      |    * List((3,1), (3,2), (4,3)
      |    */
      |  def matchingLetters(wordA:String, wordB:String):List[(Int, Int)] = ???
      |```
      |
      |This sounds like it involves some filters, folds, and ...
      |
      |let's just write it using for notation and see if it works out!
      |
      |```scala
      |def matchingLetters(wordA:String, wordB:String):List[(Int, Int)] = {
      |  (for {
      |    (a, ai) <- wordA.zipWithIndex
      |    (b, bi) <- wordB.zipWithIndex if a == b
      |  } yield ai -> bi).toList
      |}
      |```
      |
      |Note, I just called *toList* on the result because otherwise it produces an *IndexedSeq* which is still a sequence but not a list
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## for makes business logic readable...
      |
      |```scala
      |for {
      |  unit <- lecturer.units
      |  student <- Enrolments.getStudents(unit)
      |  assignment <- unit.getAssignments()
      |  mark <- gradeBook.get(assignment, student) if mark < 50
      |} yield {
      |  s"$student failed assignment $assignment in $unit"
      |}
      |
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Fold
      |
      |Suppose we have a list of numbers
      |
      |0, 1, 2, 3, 4, 5, 6, 7, 8
      |
      |and we want to sum them using the `+` operator. Plus takes two parameters. How can we apply it to the whole list?
      |
      |Let's start with the answer...
      |
      |```scala
      |(0 until 9).foldLeft(0)(_ + _)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Fold
      |
      |What we've effectively done with foldLeft is:
      |
      |* take our list of numbers  
      |  0, 1, 2, 3, 4, 5, 6, 7, 8
      |  
      |* insert the + operator, nesting on the left  
      |  ((((((((? + 0) + 1) + 2) + 3) + 4) + 5) + 6) + 7) + 8)
      |
      |* and give it a base case of zero  
      |  ((((((((0 + 0) + 1) + 2) + 3) + 4) + 5) + 6) + 7) + 8)
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## FoldRight
      |
      |We could also fold from the right
      |
      |* take our list of numbers  
      |  0, 1, 2, 3, 4, 5, 6, 7, 8
      |  
      |* insert the + operator, nesting on the right  
      |  (0 + (1 + (2 + (3 + (4 + (5 + (6 + (7 + (8 + ?)))))))))
      |
      |* and give it a base case of zero  
      |    (0 + (1 + (2 + (3 + (4 + (5 + (6 + (7 + (8 + 0)))))))))
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Something to notice
      |
      |On `List[T]`, `foldLeft` is *tail recursive*!
      |
      |((((((((0 + 0) + 1) + 2) + 3) + 4) + 5) + 6) + 7) + 8)  
      |
      |We can show that with our own little re-implementation
      |
      |```scala
      |@tailrec
      |def fl2[A](l:List[Int])(base:A)(func:(A,Int) => A): A = {
      |  if (l.isEmpty)
      |    base
      |  else {
      |    fl2(l.tail)(func(base, l.head))(func)
      |  }
      |}
      |```
      |
      |```scala
      |fl2(List(7, 8, 9))(0)({ case (base, head) =>
      |  println(s"Called for base (sum so far) $base and head $head")
      |  base + head
      |})
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### foldLeft and Catamorphism
      |
      |`foldLeft` is defined on List, Seq, etc. But in principle, you can apply it to any ordered collection. 
      |When you create a fold for a data structure other than a list, it's also called a *catamorphism*.
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides