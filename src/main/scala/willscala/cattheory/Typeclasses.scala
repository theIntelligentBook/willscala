package willscala.cattheory


import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.{Common, Styles, styleSuite}
import Common.{marked, willCcBy}
import willscala.given

import coderunner.JSCodable
import lavamaze.Maze

val typeclasses = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Typeclasses
      |
      |(And monoids)
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """![Tiger Food](images/tigerfood.png)
      |
      |## Is this openable?
      |""".stripMargin).withClass("center middle")
  .markdownSlide(
      """## A different kind of "is a" relationship
        |
        |![Tiger Food and can opener](images/typeclasses.png)
        |
        |Sometimes, an "is a" relationship depends on something *else* we have.
        |
        |* A can is only openable if we've got a can opener
        |* A corked bottle is only openable if we've got a corkscrew
        |* A lock is only openable if we've got a key
        |
        |In Scala, we can express that using a *typeclass*.
        |""".stripMargin)
  .markdownSlide(
    """## Typeclasses
      |
      |Whether `A` is a subtype of `B` depends on whether there is another
      |item in scope. eg, the can opener.
      |
      |In the case of programming languages, however, we consider this
      |object the *evidence* that `A` is a subtype of `B`
      |
      |Let's declare a trait for the **evidence** that something is Openable
      |
      |```scala
      |trait Openable[A]:
      |  extension(a:A) def open: String
      |```
      |
      |This is not yet openable:
      |
      |```scala
      |class Wine(val name:String)
      |```
      |
      |because I haven't declared my corkscrew
      |""".stripMargin
  )
  .markdownSlide(
    """## Typeclasses
      |
      |Let's create a function, needing something openable.
      |
      |Note that we have a second argument list asking for an *implicit parameter* - evidence that this is openable
      |
      |```scala
      |def pour[A](item: A)(using ev: Openable[A]) = 
      |  val contents = item.open
      |  println(contents)
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Typeclasses
      |
      |Let's provide the corkscrew.
      |
      |```scala
      |// Corkscrew
      |given corkscrew:Openable[Wine] with
      |  extension (w:Wine)  
      |    def open = "Lovely " + w.name
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |## Typeclasses
      |
      |Now that we've declared our corkscrew, we can call our pour function, which was defined:
      |
      |```scala
      |def pour[A](item: A)(using ev: Openable[A]) = 
      |  val contents = item.open
      |  println(contents)
      |```
      |
      |Calling it with the corkscrew explicitly:
      |
      |```scala
      |pour(new Wine("Sussex pinot meunier"))(using Wine.Corkscrew)
      |```
      |
      |or letting the compiler find the corkscrew implicitly
      |
      |```scala
      |pour(new Wine("Sussex pinot meunier"))
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Syntactic sugar
      |
      |This is common enough that there's another way of writing this
      |
      |```scala
      |def pour[A](item: A)(using ev: Openable[A]) = 
      |  val contents = item.open
      |  println(contents)
      |```
      |
      |Can be written:
      |
      |```scala
      |def pour[A : Openable](item: A) = 
      |  val contents = item.open
      |  println(contents)
      |```
      |
      |We also don't have to give the evidence a name - it can be an anonymous given:
      |
      |```scala
      |given Openable[Wine] with
      |  extension (w:Wine) def open = "Lovely " + w.name
      |```
      |""".stripMargin
  )
  .markdownSlide(
    """
      |## Monoids and Composition
      |
      |No, they're not a Doctor Who monster...
      |""".stripMargin
  ).withClass("center middle")
  .markdownSlide(
    """## Multiplication
      |
      |I suspect you've noticed:
      |
      |* 1 * x == x
      |* x * 1 == 1
      |
      |Let's call 1 the *identity element* for multiplication
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Multiplication
      |
      |I suspect you've noticed:
      |
      |* (2 \* 3) \* 4 == 6 * 4 == 24
      |* 2 \* (3 \* 4) == 2 * 12 == 24
      |
      |In fact, 
      |
      |* (a \* b) \* c == a \* (b \* c)
      |
      |So much so that we can leave the parentheses off and just write
      |
      |* a \* b \* c
      |
      |Multiplication over integers is *associative*
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## We could do this for addition too
      |
      |I suspect you've noticed:
      |
      |* 0 + x == x
      |* x + 0 == x
      |
      |Let's call 0 the *identity element* for addition
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Addition
      |
      |I suspect you've noticed:
      |
      |* (2 + 3) + 4 == 5 + 4 == 9
      |* 2 + (3 + 4) == 2 + 7 == 9
      |
      |In fact, 
      |
      |* (a + b) + c == a + (b + c)
      |
      |So much so that we can leave the parentheses off and just write
      |
      |* a + b + c
      |
      |Addition over integers is *associative*
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## We could also do this for `List[T]`
      |
      |Concatenation uses the `++` operator:
      |
      |```scala
      |List(1, 2, 3) ++ List(4, 5) == List(1, 2, 3, 4, 5)
      |```
      |
      |You might have noticed:
      |
      |* List.empty ++ listA == listA
      |* listA ++ List.empty == listA
      |
      |Let's call the empty list the *identity element* for list concatenation
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Concatenation for Lists
      |
      |You might have noticed:
      |
      |* (List(1, 2) ++ List(3, 4)) ++ List(5, 6) == List(1, 2, 3, 4, 5, 6)
      |* List(1, 2) ++ (List(3, 4) ++ List(5, 6)) == List(1, 2, 3, 4, 5, 6)
      |
      |in fact, 
      |
      |* (listA ++ listB) + listC == listA + (listB ++ listC)
      |
      |So much so we can just drop the brackets and say
      |
      |* listA ++ listB ++ listC
      |
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## What do these have in common
      |
      |* *addition* over *integers* with *zero* as an identity element
      |* *multiplication* over *integers* with *one* as an identity element
      |* *concatenation* over *lists* with *the empty list (Nil)* as an identity element
      |
      |We could also add:
      |
      |* *concatenation* over *strings* with `""` as an identity element
      |
      |They're all *associative*, but that doesn't capture the part about   
      |*identity + x == x == x + identity*
      |
      |Let's give them a name. Let's call them ***monoids***
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Monoids
      |
      |What we need for a monoid:
      |
      |* A type `T`
      |* An identity element. Let's call it `mempty` for the slide
      |* An operation that takes two arguments of type `T`. Let's call it `<>` for the slide.
      |
      |
      |And these rules
      |
      |```scala
      |mempty <> x == x // left identity
      |```
      |
      |```scala
      |x <> mempty == x // right identity
      |```
      |
      |```scala
      |(a <> b) <> c == a <> (b <> c) // associativity
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Monoid as a typeclass
      |
      |Typeclasses give us a way of expressing these. Let's define two traits.
      |
      |The first is `Semigroup[T]`, a typeclass expressing the concatenation. 
      |Then, `Monoid[T]` as a typeclass for semigroups that also adds `mempty`
      |
      |```scala
      |trait Semigroup[T]:
      |  extension (item:T) def <>(other:T): T
      |
      |trait Monoid[T] extends Semigroup[T]:
      |  def mempty:T
      |```
      |
      |Things to point out:
      |
      |* The type system can't enforce the laws (left identity, right identity, associativity)
      |* It's a typeclass 
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Defining some monoids
      |
      |Let's provide the evidence that `Int` addition is a monoid:
      |
      |```scala
      |given intAddition:Monoid[Int] with
      |  def mempty = 0
      |  extension (item:Int) def <>(other:Int) = item + other
      |```
      |
      |Or, we could provide the evidence that `Int` multiplication is a monoid:
      |
      |```scala
      |given intMultiplication:Monoid[Int] with
      |  def mempty = 1
      |  extension (item:Int) def <>(other:Int) = item * other
      |```
      |     
      |Or, list append:
      |
      |```scala
      |given listAppend[T]:Monoid[List[T]] with 
      |  def mempty = Nil
      |  extension (item:List[T]) def <>(other:List[T]) = item ++ other
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |## Defining a `combineAll`
      |
      |Suppose we have
      |
      |```
      |a <> b <> c <> d <> e
      |```
      |
      |That looks an awful lot like our example of summing across lists:
      |
      |```
      |0 + a + b + c + d + e
      |```
      |
      |which we did using fold
      |
      |```
      |(((((0 + a) + b) + c) + d) + e)
      |```
      |
      |It looks like it ought to be possible to say something like
      |
      |```
      |List(a, b, c, d, e).foldLeft(mempty)(<>)
      |```
      |""".stripMargin
  )
  .markdownSlide(
    """## `combineAll`
      |
      |So, let's define an extension that we could apply to any monoid
      |
      |```scala
      |extension [M : Monoid] (list:List[M]) 
      |  def combineAll:M = list.foldLeft(summon[Monoid[M]].mempty)(_ <> _)
      |```
      |
      |It's a slightly convoluted piece of syntax, but we just expressed a lot:
      |
      |* For some `Monoid` `M`, if we have a `List[M]`
      |* We define an extension method `combineAll` that will fold across the list, starting with the `mempty` value, 
      |  and combining everything using that monoid's `<>` operator.
      |  
      |The `summon` keyword summons the Monoid implementation from the context. We need to do that because we defined
      |`mempty` as a method on it, rather than an extension method.
      |
      |```scala
      |trait Semigroup[T]:
      |  extension (item:T) def <>(other:T): T
      |
      |trait Monoid[T] extends Semigroup[T]:
      |  def mempty:T
      |```
      |""".stripMargin
  )
  .markdownSlide(
    """## `combineAll`
      |
      |Now that we have our definition in place
      |
      |```scala
      |extension [M : Monoid] (list:List[M]) 
      |  def combineAll:M = list.foldLeft(summon[Monoid[M]].mempty)(_ <> _)
      |```
      |
      |We can use it on different monoids:
      |
      |```scala
      |// Int multiplication
      |List(1, 2, 3, 4).combineAll(using intMultiplication)
      |
      |// Int addition
      |List(1, 2, 3, 4).combineAll(using intAddition)
      |
      |// List append - there's only one given to find, so we can skip the using parameter
      |List(List(1), List(2, 3), List(4, 5, 6)).combineAll
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Summary
      |
      |With typeclasses, we can express properties about types that *don't otherwise relate to each other*. 
      |`List[T]` is not a subclass of `Int`, but we expressed how `List` append and `Int` addition are both monoids.
      |
      |* Monoids are a type, an operation, and an identity element such that the monoid laws hold
      |
      |* Monoids let us compose operations without worrying about the order of the brackets.
      |
      |That's a nice property (knowing things compose well) so we'd often like operations to be monoid-like
      |
      |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides

