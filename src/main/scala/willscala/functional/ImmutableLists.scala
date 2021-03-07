package willscala.functional


import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}

val immutableLists = DeckBuilder(1280, 720)
  .markdownSlide(
    """
      |# Immutable Lists
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """## Let's make a List
      |
      |One of the most common data structures in functional programming exercises is an immutable singly linked list,
      |`List[T]`
      |
      |`List[T]` is in the standard library with lots of funcitonality, but to explore it let's start by defining
      |our own. Because we haven't done the types chapter yet, we'll keep it simple and just define our own list of
      |integers. We won't even use the `enum` shortcut this time.
      |
      |```scala
      |sealed trait IntList
      |case object Empty extends IntList
      |case class Cons(head:Int, tail:IntList) extends IntList
      |```
      |
      |Notice that `Empty` is a case object - there is only one empty list object.
      |
      |""".stripMargin)
  .markdownSlide("""
                   |## Let's create a list
                   |
                   |```scala
                   |sealed trait IntList
                   |case object Empty extends IntList
                   |case class Cons(head:Int, tail:IntList) extends IntList
                   |
                   |val myList = Cons(1, Cons(2, Cons(3, Empty)))
                   |```
                   |
                   |Some questions:
                   |
                   |* What is `myList.head`?
                   |* What is `myList.tail`?
                   |* What is `myList.tail.head`?
  """.stripMargin)
  .markdownSlide("""
                   |## Modifying our list
                   |
                   |Let's change it so that `head` and `tail` are defined on `MyList`, not just on `Cons`
                   |
                   |```scala
                   |sealed trait IntList:
                   |  def head:Int
                   |  def tail:IntList
                   |
                   |case object Empty extends IntList:
                   |  def head = throw new NoSuchElementException()
                   |  def tail = throw new NoSuchElementException()
                   |
                   |case class Cons(head:Int, tail:IntList) extends IntList
                   |```
                   |
                   |Now let's try that again:
                   |
                   |* What is `myList.head`?
                   |* What is `myList.tail`?
                   |* What is `myList.tail.head`?
  """.stripMargin)
  .markdownSlide(
    """## Let's define a `length` method 
      |
      |```scala
      |sealed trait IntList:
      |  def length:Int = 
      |    ???
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """## Let's define a `length` method 
      |
      |```scala
      |sealed trait IntList:
      |  
      |  def length:Int = this match 
      |    case Empty => 0
      |    case Cons(_, t) => 1 + t.length
      |```
      |
      |Question:
      |
      |* Is `length` tail recursive?
      |
      |*No*. Although lexically, it looks like `t.length` is at the end, we still have to add 1 to it after it returns.
      |So it isn't "in tail position".
  """.stripMargin)
  .markdownSlide(
    """### Apply
      |
      |Let's consider something else. At the moment, we've got no way of indexing into an `IntList`.
      |eg, we can't say `myList(3)`
      |
      |```scala
      |trait IntList:
      |  def apply(n:Int):Int = ???
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """### Apply
      |
      |Let's consider something else. At the moment, we've got no way of indexing into an `IntList`.
      |eg, we can't say `myList(3)`
      |
      |```scala
      |trait IntList:
      |  def apply(n:Int):Int = this match {
      |    case Empty => throw new IndexOutOfRangeException()
      |    case Cons(h, tail) => ???
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """### Apply
      |
      |Let's consider something else. At the moment, we've got no way of indexing into an `IntList`.
      |eg, we can't say `myList(3)`
      |
      |```scala
      |trait IntList:
      |  def apply(n:Int):Int = this match {
      |    case Empty => throw new IndexOutOfRangeException()
      |    case Cons(h, tail) => if n <= 0 then h else tail.apply(n - 1)
      |```
      |
      |Question: 
      |
      |* Is `apply` tail recursive?
  """.stripMargin)
  .markdownSlide(
    """## Tail recursive?
      |
      |We can ask the compiler to verify with an annotation
      |
      |```scala
      |import scala.annotation.tailrec
      |
      |trait IntList:
      |  @tailrec
      |  def apply(n:Int):Int = this match {
      |    case Empty => throw new IndexOutOfRangeException()
      |    case Cons(h, tail) => if n <= 0 then h else tail.apply(n - 1)
      |```
      |
  """.stripMargin)
  .markdownSlide(
    """## Lists, structurally
      |
      |In the standard library, `List[T]` is a singly linked immutable list. A list is either 
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
      |`Nil` is case object and `::` is a case class, so we can use them in pattern matching.
      |""".stripMargin)
  .markdownSlide("""
                   |## Pattern matching on a List 
                   |
                   |Just as we were able to pattern match inside tuples, we can also pattern match inside lists
                   |
                   |```scala
                   |def describeList[T](list:List[T]) = 
                   |  list match 
                   |    case Nil => "The list was empty"
                   |    case a :: Nil => "It only had one element"
                   |    case a :: b :: tail if b % 2 == 0 => "The second element was even"
                   |    case _ => "The second element was odd"
                   |    
                   |describe(1 :: 2 :: 3 :: 4 :: Nil)
                   |```
                   |
                   |Notice that we gave the function a *type parameter* for the contents of the list.
  """.stripMargin)
  .markdownSlide("""
                   |## Pattern matching on a List 
                   |
                   |Scala being Scala, we can also use List's constructor syntax in some of the cases
                   |
                   |```scala
                   |def describeList(l:List[Int]):String = l match 
                   |  case List() => "It was empty"
                   |  case List(_) => "It only had one element"
                   |  case _ :: b :: _ if b % 2 == 0 => "The second element was even"
                   |  case _ => "The second element was odd"
                   |
                   |describe(1 :: 2 :: 3 :: 4 :: Nil)
                   |```
  """.stripMargin)
  .markdownSlide(
    """## Type erasure
      |
      |Type parameters are known at compile time but *not* at runtime. They are "erased" and `List[T]` operates
      |just as a list at runtime without knowing the type of its content.
      |
      |That means we can't pattern match directly on the type parameter:
      |
      |```scala
      |// Sorry, this will give us a warning that it can't do the type check
      |def whatListIsThis[T](l:List[T]):String = 
      |  l match 
      |    case li:List[Int] => "A list of numbers"
      |    case ls:List[String] => "A list of strings"
      |    case _ => "Something else"
      |    
      |whatListIsThis(List("Hello")) // This would give us "A list of numbers" (!) as the first matching case
      |```
      |
      |Because the type parameter is erased, at run-time both the first two patterns just match on `l` being a List.
      |""".stripMargin)
  .markdownSlide(
    """## List exercises
      |
      |It's very common to set functional programming exercises that involve doing things with Lists.
      |
      |For example, 
      |
      |* Define a function that will repeat every element in a list. E.g.  
      |  `repeat(List(1, 2, 3))` should produce `List(1, 1, 2, 2, 3, 3)`
      |
      |* Define a function that will produce a list containing every alternate element from a list. E.g.
      |  `alternate(List(1, 2, 3, 4, 5, 6))` should produce `List(2, 4, 6)`
      |
  """.stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides