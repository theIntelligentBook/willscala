package willscala.higherOrder

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}

val higherOrderDeck = DeckBuilder(1920, 1080)
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
      |```scala
      |twice // : Function1[Int, Int]
      |```
      |
      |It's a function from `Int => Int`
      |
      |""".stripMargin)
  .markdownSlide("""
      |### Functions as values
      |
      |Functions can themselves be put into values or variables
      |
      |```scala
      |val t = twice
      |```
      |
      |Our value `t` now contains the *function* twice, not the result of a call to twice.
      |
      |```scala
      |t(2)
      |```
      |""".stripMargin)
  .markdownSlide("""
      |## Functions as arguments
      |
      |We can also pass a function as a value.
      |
      |Let's start by showing this imperatively
      |
      |```scala
      |def twice(i:Int) = 2 * i
      |
      |def runIt(f: Int => Int):Unit = 
      |  var i = 0
      |  while i < 10 do 
      |    println(s"The result of applying f to $i is ${f(i)}")
      |    i += 1
      |    
      |runIt(twice)
      |```
      |""".stripMargin)
  .markdownSlide("""
      |## A higher order function on lists
      |
      |Let's suppose we have a home-grown list of `Int`s:
      |
      |```scala
      |sealed trait IntList
      |case object Empty extends IntList
      |case class Cons(head:Int, tail:IntList) extends IntList
      |
      |val listOf123 = Cons(1, Cons(2, Cons(3, Empty)))
      |```
      |
      |Let's define a method that will take some function `Int => Int` and apply it to every element in the list,
      |producing a new list containing the results.
      |
      |This function is called `map` because it uses the function to *map* every value in the list to a corresponding 
      |value in the new list.
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Defining map
      |
      |A recursive definition of `map` for our `IntList` is very short
      |
      |```scala
      |sealed trait IntList:
      |  def map(f: Int => Int):IntList = this match 
      |    case Empty => Empty
      |    case Cons(head, tail) => Cons(f(head), tail.map(f))
      |
      |case object Empty extends IntList
      |case class Cons(head:Int, tail:IntList) extends IntList
      |```
      |
      |Let's try it out:
      |
      |```scala
      |def twice(i:Int) = 2 * i
      |Cons(1, Cons(2, Cons(3, Empty))).map(twice)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## `List[T].map`
      |
      |Map is such a useful method that it's already part of the Scala core library and is defined on `List[T]`
      |(as well as on lots of other types)
      |
      |```scala
      |val myList:List[Int] = List(1, 2, 3)
      |myList.map(twice)  // List(2, 4, 6)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Passing a lambda to a higher order function
      |
      |We can also call `List[T].map` with a lambda
      |
      |```scala
      |// Let's double these
      |List(1, 2, 3).map((x) => x * 2)
      |```
      |
      |Hmm, that makes one of our tutorial exercises from week 1 (doubling the contents of a list) pretty short!  
      |We can go shorter still using a shorthand syntax:
      |
      |```scala
      |// Let's triple these
      |List(1, 2, 3).map(_ * 3)
      |```
      |
      |Scala even defines a `map` method on arrays for us:
      |
      |```scala
      |// Let's quadruple an array
      |Array(1, 2, 3, 4).map(_ * 4)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Mapping from one type to another
      |
      |List takes a type parameter - it's `List[A]`. If we go and take a look at the definition of `map`, we'll see it's
      |
      |```scala
      |  def map[B](f: A => B):List[B]
      |```
      |
      |So, if we have a list of integers, we could map them to a list of strings, by passing `f: Int => String`
      |
      |```scala
      |val strings = List("Algernon", "Bertie", "Cecily")
      |
      |List[Int](1, 2, 3).map[String]((x) => strings(x))
      |```
      |
      |or, letting the type parameters be inferred, just:
      |
      |```scala
      |List(1, 2, 3).map((x) => strings(x))
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Times position
      |
      |To do this exercise using `map`:
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
      |That looks too long. From Scala 3, we can automatically unapply a tuple in a lambda.
      |
      |```scala
      |def timesPosition(arr:Array[Int]):Array[Int] =
      |  arr.zipWithIndex.map((x,i) => x * i)
      |```
      |
      |Or, we could use a `case` expression to unapply it like any other case class
      |
      |```scala
      |def timesPosition(arr:Array[Int]):Array[Int] = 
      |  arr.zipWithIndex.map({ case (x,i) => x * i })
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
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
       |## Filter
       |
       |Let's go back to our home-grown `IntList` and define a method that will take some function `Int => Boolean` and 
       |apply it to every element in the list, producing a new list containing only the results that map to `true`.
       |
       |This function is called `filter` because it filters the list to include only the ones where the predicate is
       |true
       |
       |```scala
       |sealed trait IntList:
       |  def filter(f: Int => Boolean):IntList = this match
       |    case Empty => Empty
       |    case Cons(head, tail) if f(head) => Cons(head, tail.filter(f))
       |    case Cons(_, tail) => tail.filter(f)
       |
       |Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty))))).filter(_ % 2 == 0)
       |```
       |""".stripMargin)
  .markdownSlide("""
       |## Exists
       |
       |Let's define a method `exists` that will take a function `Int => Boolean` and will return true if *any* of
       |the numbers in the list map to `true`.
       |
       |```scala
       |sealed trait IntList:
       |  def exists(f: Int => Boolean):Boolean = this match
       |    case Empty => false
       |    case Cons(head, tail) if f(head) => true
       |    case Cons(_, tail) => tail.exists(f)
       |
       |Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty))))).exists(_ % 2 == 0)  // true
       |Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty))))).exists(_ % 200 == 0)  // false
       |```
       |""".stripMargin)
  .markdownSlide("""
       |## Exists
       |
       |Let's define a method `exists` that will take a function `Int => Boolean` and will return true only if *all* of
       |the numbers in the list map to `true`.
       |
       |Let's be lazy on this one - `list.forall(f)` is true if `lists.exists(x => !f(x))` is false
       |
       |```scala
       |sealed trait IntList:
       |  def exists(f: Int => Boolean):Boolean = 
       |    !exists(!f(_))
       |
       |Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty))))).forall(_ % 2 != 0)  // false
       |Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty))))).exists(_ % 200 != 0)  // true
       |```
       |""".stripMargin)
  .markdownSlide("""
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
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Filter on `List[T]`
      |
      |Suppose we have a range of numbers
      |
      |```scala
      |val range = 1 to 100
      |```
      |
      |This will give us all the even numbers
      |
      |```scala
      |range.filter(_ % 2 == 0)
      |```
      |""".stripMargin)
  .markdownSlide("""
      |## A prime number check
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
      |## The primes from 1 to 100
      |
      |Let's pass our check into filter to produce a (not very efficient) prime number generator:
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
      |## `for` as Syntactic sugar
      |
      |When I introduced Scala syntax, I said `for` in Scala does something unique but quite readable.
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
      |So, in our home-grown `IntList`, by defining `map`, we enabled a simple for-comprehension:
      |
      |```scala
      |sealed trait IntList:
      |  def map(f: Int => Int):IntList = this match 
      |    case Empty => Empty
      |    case Cons(h, t) => Cons(f(h), t.map(f))
      |
      |val oneToFive = Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty)))))
      |for i <- oneToFive yield i * 2
      |```
      |""".stripMargin)
  .markdownSlide("""
      |## `for` as Syntactic sugar
      |
      |Let's do something just as readable but where the translation is a bit different
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
      |## Flattening two lists
      |
      |Let's make a function that repeats the number *n*, *n* times.
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
      |""".stripMargin)
  .markdownSlide("""
      | ## Our own flatMap
      | 
      | Let's define `flatMap` for our homegrown `IntList`. 
      | 
      | ```scala
      | sealed trait IntList:
      |   def flatMap(f: Int => IntList):IntList = ???
      | ```
      | 
      | We're going to have a little problem. It can't really be "map then flatten" because our `IntList` only supports
      | lists of `Int`s, not lists of `IntList`s.
      | 
      | But we can still have a `flatMap` function that has that type signature.
      | For each element, let's apply `f`, getting an `IntList`, and then concatenate it with whatever the tail produces.
      | (We'd better define concatenate too.)
      | 
      |```scala
      |sealed trait IntList:
      |
      |  def flatMap(f: Int => IntList):IntList = this match
      |    case Empty => Empty
      |    case Cons(h, tail) => f(h) ++ tail.flatMap(f)
      | 
      |  def ++(other:IntList):IntList = this match 
      |    case Empty => other
      |    case Cons(h, tail) => Cons(h, tail ++ other)
      |```
      | 
      |""".stripMargin)
  .markdownSlide("""
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
      |On `List[T]` in particular, `foldLeft` is tail recursive.
      |
      |((((((((0 + 0) + 1) + 2) + 3) + 4) + 5) + 6) + 7) + 8)  
      |
      |We can show that with our own little re-implementation
      |
      |```scala
      |sealed trait IntList:
      |
      |  @tailrec
      |  def foldLeft(start:Int)(f: (Int, Int) => Int):Int = this match
      |    case Empty => start
      |    case Cons(h, t) => t.foldLeft(f(start, h))(f)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |### foldLeft and Catamorphism
      |
      |`foldLeft` is defined on List, Seq, etc. But in principle, you can apply it to any ordered collection. 
      |When you create a fold for a data structure other than a list, it's also called a *catamorphism*.
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides