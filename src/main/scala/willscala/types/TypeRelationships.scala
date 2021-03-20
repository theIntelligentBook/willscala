package willscala.types


import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.{Common, Styles, styleSuite}
import Common.{marked, willCcBy}

import coderunner.JSCodable
import lavamaze.Maze

val typeRelationships = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Type relationships
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
        |doNothing // Int => Nothing
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
    """## Path dependent types
      |
      |We can define traits and classes inside an object. In Scala, these make the inner types *path dependent* because 
      |the nested types depend on the value they are contained within.
      |
      |This site uses an example. It's built on a library *Doctacular* in which each site keeps has some "routes" to 
      |know how to map URLs to pages to display. You can have the situation that there's more than one site in 
      |the library, each with its own set of routes.
      |But you can't accidentally ask one site to navigate to another site's route.
      |
      |Here's a simplified version of the idea:
      |
      |```scala
      |class Site():
      |
      |  sealed trait Route
      |  case object Home extends Route
      |  case class Page(name:String) extends Route
      |  
      |  def routeTo(r:Route):Unit = ???
      |
      |val siteA = Site()
      |val siteB = Site()
      |
      |siteA.routeTo(siteB.Home) // compile error. Found siteB.Home required siteA.Route
      |```
      |
      |""".stripMargin
  )
  .markdownSlide("## Algebraic Data Types").withClass("center middle")
  .markdownSlide(
    """## Algebraic Data Types
      |
      |Algebraic data types are where we compose a type by combining other types.
      |
      |The most common are *product types*, where a type contains fields of particular types. For instance:
      |
      |* Tuples, e.g. `(Int, String)`
      |* Records or classes, e.g. `case class Dog(name:String, age: Int)`
      |
      |Note, though, that the types we're seeing so far have an order to them and are not commutative. 
      |`(Int, String)` is a product of an `Int` and a `String`, but it is not the same as `(String, Int)`.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Sum types
      |
      |If a *product* type contains both types, a *sum* type contains one or the other.
      |
      |One kind that we've already come across is `enum`s.
      | 
      |```scala
      |enum Pet:
      |  case Bird, Cat, Dog, Horse, Fish
      |  case Other(name:String)
      |```
      |
      |A pet is a bird *or* a cat *or* a dog *or* a horse *or* a fish, *or* some other named Animal.
      |
      |Scala also has a class `Either[L, R]` that is sometimes used in older libraries. It is *either* `Left(ofTypeL)`
      |or `Right(ofTypeR)`. 
      |
      |```scala
      |val l:Either[Int, String] = Left(3)
      |val r:Either[Int, String] = Right("Hello")
      |```
      |
      |Where `(L, R)` is the product of two types, `Either[L, R]` is the sum of two types.
      |
      |Note, though, that the types we're seeing so far have an order to them and are not commutative. 
      |`Either[Int, String]` is a sum of an `Int` and a `String`, but it is not the same as `Either[String, Int]`.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Union types
      |
      |`Either[L, R]` is not `Either[R, L]`. Which is a pain, because sometimes we just want to say "one or the other"
      |and don't want to have to remember which is a `Left` and which is a `Right`. And maybe somewhere else we 
      |want to define the union of that with something else...
      |
      |From Scala 3, we can define *union types*. Here's a little example with cards: 
      |
      |```
      |enum FaceCard:
      |  case Jack, Queen, King
      |
      |type CardValue = Int | FaceCard
      |
      |enum Card:
      |  case Club(n:CardValue)
      |  case Diamond(n:CardValue)
      |  case Heart(n:CardValue)
      |  case Spade(n:CardValue)
      |
      |import Card._
      |import FaceCard._
      |type Black = Club | Spade
      |type Red = Heart | Diamond
      |
      |var rc:Red = Heart(Jack)
      |rc = Spade(Queen) // Compile error. Found Card.Spade, required Card.Red
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## An example of union types
      |
      |There's an example of union types in this site too. Doctacular is built on *Veautiful*, my React-like front-end
      |framework that lets me write HTML user interfaces from Scala.
      |
      |In composing the HTML, a node might take a variety of children
      |
      |```scala
      |div(
      |  attr("style") := "background: antiquewhite;",
      |  h1("Here's an example"),
      |  "This is just a string",
      |  for num <- 1 to 10 yield label(num.toString)
      |)
      |```
      |
      |In that call to *div*, we've got an attribute setting, an `h1` node, a plain `String`, and a `Seq` of nodes.
      |If we let the inferencer choose a common supertype, it'd go all the way up to `Matchable`.
      | 
      |Instead, what I have (simplified here) is define a union type:
      |
      |```scala
      |type SingleChild = String | Attribute | VHtmlNode
      |type ElementChild = SingleChild | Iterable[SingleChild]
      |```
      |
      |My web framework can now define that it takes `ElementChild`s in a vararg. Again this is a little simplified:
      |
      |```scala
      |def div(contents:ElementChild*) = ???
      |```
      |""".stripMargin
  )
  .markdownSlide(
    """## Intersection types
      |
      |We can also define intersection types using `&`. Because we can't really have a value that is simultaneously an
      |`Int` and a `String`, this mostly makes sense for use with traits.
      |
      |```scala
      |trait Writeable[T]:
      |  def write(data:T):Unit
      |  
      |trait Readable[T]:
      |  def read():T
      |  
      |type ReadWrite[T] = Readable[T] & Writeable[T]
      |```
      |
      |""".stripMargin)
  .markdownSlide("## Variance").withClass("center middle")
  .markdownSlide(
    """## Variance
      |
      |A big complication with types is when we put them inside a container...
      |
      |* Is a list of cats a list of animals?
      |* Is a list of animals a list of cats?
      |* Is an array of cats an array of animals?
      |
      |The question here, is we've got some container type `C[_]`, some element type `E` we're asking
      |
      |> If `E2` is a subtype of `E1`, is `C[E2]` a subtype of `C[E1]`?
      |
      |And the answer is it depends on the *variance* of the container type `C[_]`
      |
      |Let's try to intuit this with some examples.
      |""".stripMargin
  )
  .markdownSlide(
    """## Covariance
      |
      |When we asked *"Is a list of cats a list of animals?"* we probably thought *"Yes, it sounds like it should be"*.
      |
      |And yes it is. 
      |
      |List is defined as *covariant*. Let's make up our own simplified one:
      |
      |```scala
      |enum MyList[+T]:
      |  case Nil
      |  case Cons(head:T, tail:MyList[T])
      |  
      |  def count:Int = this match 
      |    case Nil => 0
      |    case Cons(h, t) => t.count + 1
      |  
      |import MyList._
      |```
      |
      |When we declared it `MyList[+T]` we declared this is *covariant* in type `T`.
      |
      |If cats are a subtype of animal, then `MyList`s of cats are a subtype of `MyList`s of animal.
      |
      |Note: With the import, we've *shadowed* `Nil` from Scala's list type - that is, when we refer to `Nil` later in 
      |this file, the the compiler will understand we mean `MyList.Nil` not `List.Nil`. 
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Covariance
      |
      |Let's put the cats in 
      |
      |```scala
      |trait Animal
      |case class Cat(name:String) extends Animal
      |
      |def even(animals:MyList[Animal]):Boolean = 
      |  animals.count % 2 == 0
      |  
      |val cats = Cons(Cat("Shyla"), Cons(Cat("Macavity"), Nil)) // :MyList[Cat]
      |even(cats) // true - yes, this works
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Nothing is a subtype of everything...
      |
      |In fact, it's just as well this does work, or we would have had a problem defining our list in the first place.
      |
      |```scala
      |enum MyList[+T]:
      |  case Nil
      |  case Cons(head:T, tail:MyList[T])
      |  
      |  def count:Int = this match 
      |    case Nil => 0
      |    case Cons(h, t) => t.count + 1
      |  
      |import MyList._
      |```
      |
      |`MyList.Nil` is an *object* representing all empty lists. We know there's nothing in it, but to the compiler 
      |what type is it a list of?
      |
      |```scala
      |MyList.Nil // MyList[Nothing]
      |```
      |
      |So, this only works because `MyList` is covariant. `Nil` can represent an empty list of cats or animals or Ints
      |because `Nothing` is a subtype of cats, or animals, or ints, so `MyList[Nothing]` is a subtype of lists of cats,
      |or lists of animals, or lists of Ints.
      |""".stripMargin
  )
  .markdownSlide(
    """## Invariance
      |
      |Is an array of cats an array of animals?
      |
      |With lists, we could only get the cats *out*. We couldn't put a new one *in*.
      |If `Array[T]` was covariant, this would compile without an error:
      |
      |```scala
      |val cats:Array[Cat] = Array(Cat("Shyla"), Cat("Macavity"), Cat("Scarface Claw"))
      |
      |def insertRosie(arr:Array[Animal]):Unit = 
      |  arr(1) = Dog("Rosie")
      |  
      |insertRosie(cats) // Compile error. Found Array[Cats]. Required Array[Animal]
      |```
      |
      |If it compiled without an error, then we'd have been able to insert a dog into an `Array[Cat]`, and some other
      |code expecting only to find cats would have got a dog-shaped surprise.
      |
      |Arrays are mutable - items can go in and out, so `Array[T]` has to be *invariant* in `T`.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Contravariance
      |
      |To introduce contravariance, let's start just with a real world analogy.
      |
      |Soup is a kind of food. Gravy is also a kind of food.
      |Suppose I have a soup stain on a tablecloth, and I go to buy a stain remover.
      |
      |```scala
      |enum Food:
      |  case Soup, Gravy
      |  
      |class StainRemover[-F]():
      |  def remove(f:F):Unit = ???
      |```
      |
      |I've declared this as *contravariant* in type `F`. Why?
      |
      |* An all-purpose food-stain remover is a soup-stain remover - it'll get rid of my soup stain.
      |* But if I ask for a food-stain remover, and they give me a gravy-stain remover, I wouldn't be happy (It won't 
      |  work on my soup stain).
      |  
      |So we have a contravariant relationship.
      |
      |* `Soup` is a (subtype of) `Food`.
      |* `StainRemover[Soup]` is not a `StainRemover[Food]` (because it doesn't work on all food)
      |* `StainRemover[Food]` is a `StainRemover[Soup]` (because it works on all soup)
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Covariant and Contravariant position
      |
      |The error you are most likely to see from the compiler has text that reads like 
      |
      |> covariant type T appears in contravariant position
      |
      |Let's engineer one of these
      |
      |```scala
      |enum MyList[+T]:
      |  case Nil
      |  case Cons(head:T, tail:MyList[T])
      |  
      |  def contains(item:T):Boolean = ??? // Compile error. covariant type T appears in contravariant position
      |```
      |
      |This might make more sense if we look at the type definition of Function1 (a function with 1 argument):
      | 
      |```scala
      |trait Function1[-T1, +R]
      |```
      |
      |Functions are *contravariant* in their input, and *covariant* in their output.
      |
      |Because `contains` takes an item of type `T` in its input, it's using `T` in "contravariant position".
      |
      |""".stripMargin)
  .markdownSlide(
    """## Functions and variance
      |
      |Let's just intuit that bit about functions:
      |
      |```scala
      |trait Function1[-T1, +R]
      |```
      |
      |In their inputs, they work like stain removers (contravariant):
      | 
      |* A function accepting any food is a function that accepts gravy.
      |* A function accepting only gravy is not a function that accepts any food
      |
      |In their outputs, they are covariant
      |
      |* A function returning you a cat is a function returning you an animal.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Function type paramenters
      |
      |Let's make `contains` work - it sounds useful
      |
      |```scala
      |enum MyList[+T]:
      |  case Nil
      |  case Cons(head:T, tail:MyList[T])
      |  
      |  def contains[B](item:B):Boolean = this match 
      |    case Cons(h, _) if item == h => true
      |    case Cons(_, t) => t.contains(item)
      |    case Nil => false
      |```
      |
      |By giving it its own type parameter, we broke away from `MyList[T]` needing to accept `T`s as an input, 
      |solving the contravariant position problem.
      |
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Adding a type bound
      |
      |Let's momentarily us `contains` to explore the idea of a *lower type-bound*.
      |
      |```scala
      |enum MyList[+T]:
      |  case Nil
      |  case Cons(head:T, tail:MyList[T])
      |  
      |  def contains[B >: T](item:B):Boolean = this match 
      |    case Cons(h, _) if item == h => true
      |    case Cons(_, t) => t.contains(item)
      |    case Nil => false
      |```
      |
      |By saying `[B >: T]` we *bounded* type `B` so it can only be `T` or a supertype of `T`.
      |
      |```scala
      |val cats:MyList[Cat] = Cons(Cat("Shyla"), Cons(Cat("Macavity"), Nil))
      |cats.contains[Cat](Cat("Shyla")) //  Yes, this works
      |cats.contains[Animal](Dog("Rosie")) //  Animal is a supertype, so we can ask this
      |cats.contains[Dog](Dog("Rosie")) //  No, Dog is not a supertype, this won't compile
      |cats.contains(Dog("Rosie")) // But if we let the type inferencer infer it, it will find common supertype `Animal`
      |```
      |
      |Notice the bound was on the *type parameter* `B`, not directly on the value.
      |We were able to pass a dog (a dog is an Animal), but the *type parameter* had to be `Animal`.
      |""".stripMargin
  )
  .markdownSlide(
    """## Upper type bounds
      |
      |*Upper type-bounds* mean we're only allowing subtypes. 
      |Let's model the nineteenth century English card game "Happy Families". 
      |
      |In the deck, there are eleven families, each of which (by nineteenth century coincidence) has four members: "Mr", "Mrs", "Master", and "Miss".
      |You each have a hand of five random cards. If you have one card from a *family* (say, Mrs Bun the Baker), you can ask
      |another player if they have any of the Bun cards. If so, they have to give them to you.
      |
      |```scala
      |enum Title:
      |  case Mr, Mrs, Master, Miss
      |
      |enum HappyFamily:
      |  case Block(m:Title)
      |  case Bun(m:Title)
      |  case Bones(t:Title)
      |  case Bung(t:Title)
      |  case Chip(t:Title)
      |```
      |""".stripMargin)
  .markdownSlide(
    """## Without an upper type bound
      |
      |Let's create a function `askFor` that takes a card of a particular family, and returns some number of
      |cards from the same family.
      |
      |If we want to express this without the type constraint, it'd just be a filter:
      |
      |```scala
      |case class Hand(cards:Seq[HappyFamily]):
      |  def askFor(card:HappyFamily):Seq[HappyFamily] = 
      |    cards.filter({ c => c.ordinal == card.ordinal })
      |```
      |
      |We take a card, we give back some cards.
      |The families are members of an 'enum', so we can just check if the ordinal values match.
      |
      |Under normal circumstances, this would be absolutely fine.
      |
      |But *just to show we can*, let's try expressing the fact the cards are from the same family in the type
      |signature.
      |""".stripMargin
  )
  .markdownSlide(
    """## Upper type bounds
      |
      |So we can talk about type bounds, let's try to express the type constraint that the returned cards are all
      |from the same family in the function signature.
      |
      |```scala
      |import Title._
      |import HappyFamily._
      |
      |case class Hand(cards:Seq[HappyFamily]):
      |  def askFor[F <: HappyFamily](card:F):Seq[F] = ???
      |```
      |
      |We've said that type `F` is some subtype of `HappyFamily` and we will return a sequence *of the same subtype*.
      |
      |i.e. we've said we'll guaruntee any cards you get back are from the same family as the one you gave us.
      |
      |However, we'd need lots of clever machinery to be able to implement this function!
      |""".stripMargin
  )
  .markdownSlide(
    """## Type erasure
      |
      |The first problem we'll run into is type erasure. Although the *compiler* knows all our type parameters,
      |they are *erased* at runtime - the runtime only knows about concrete types.
      |
      |Let's take our Happy Families hand and just implement a function called `sameFamily` to see if one of our
      |cards is from the same family
      |
      |```scala
      |case class Hand(cards:Seq[HappyFamily]):
      |  def sameFamily[F <: HappyFamily](card:C, myCard:HappyFamily):Boolean = 
      |    myCard match 
      |      case c:F => true
      |      case _ => false
      |```
      |
      |This *looks* like it should work. Check the type and see if it's the same family:
      |
      |> the type test for F cannot be checked at runtime
      |""".stripMargin)
  .markdownSlide(
    """## Type erasure
      |
      |The problem here is that the *compiler* knows all about our type `F` and its bound, but at *runtime* all the 
      |type parameters are forgotten, and our code is effectively "erased" down to:
      |
      |```scala
      |case class Hand(cards:Seq):
      |  def sameFamily(card:HappyFamily, myCard:HappyFamily):Boolean = 
      |    myCard match
      |      case c:HappyFamily => true
      |      case _ => false
      |```
      |
      |Suddenly it becomes apparent we that at runtime, every card will match.
      |""".stripMargin
  )
  .markdownSlide(
    """## A simple solution
      |
      |Although at runtime we don't know the *type* F, we do still have the card we were passed and the cards we're
      |looking at in scope. It won't get rid of the type warning, but we could refine our pattern match:
      |
      |```scala
      |case class Hand(cards:Seq):
      |  def sameFamily(card:HappyFamily, myCard:HappyFamily):Boolean = 
      |    myCard match
      |      case c:HappyFamily if c.ordinal == card.ordinal => true
      |      case _ => false
      |```
      |
      |Don't forget - we don't have to go chasing super-tight type bounds. We can keep things simple.
      |
      |But this is a demo, so let's try out some clever machinery
      |""".stripMargin
  )
  .markdownSlide(
    """## Context parameters
      |
      |Let's show you a super-powered solution that will look like magic until we've talked more about context.
      |
      |It so happens, that in this case we could solve the issue by asking the compiler to *create an extra parameter* 
      |for us, that will do the type test at runtime.
      |
      |```scala
      |import scala.reflect.TypeTest
      |
      |case class Hand(cards:Seq[HappyFamily]):
      |  def sameFamily[F <: HappyFamily](card:C, myCard:HappyFamily)(using TypeTest[HappyFamily, C]):Boolean = 
      |    myCard match 
      |      case c:F => true
      |      case _ => false
      |```
      |
      |The `using` keyword asks the compiler to find a paramater for us.
      |
      |* `given` adds something to the context
      |* `using` indicates a parameter will come from the context
      |
      |In this case, the compiler is capable of *generating* the type test automatically:
      |For example, a type test for `Bun` just has to ask if this item is of concrete type `Bun`.
      |""".stripMargin)
  .markdownSlide(
    """## Why context parameters work
      |
      |Type erasure removes the type parameters, *but not the value parameters*.
      |
      |* If we say we pass a type parameter, `[F]`, it is erased.
      |* If we say we pass a value parameter, `(typeTest:TypeTest), we do pass that value.
      |
      |By passing a type tests as a value, the compiler will change our match from a test against the type parameter
      |to a function call on the `TypeTest` value we passed.
      |
      |Effectively, it now erases to:
      |
      |```scala
      |  def sameFamily(card:HappyFamily, myCard:HappyFamily)(using typeTest:TypeTest):Boolean = 
      |    myCard match 
      |      case c if typeTest(c).nonEmpty => true
      |      case _ => false
      |```
      |
      |```scala
      |val hand = Hand(Seq(Bun(Mr), Block(Mrs), Bun(Master), Chip(Miss))
      |hand.sameFamily(Bun(Mrs), hand.card(0)) // true
      |```
      |
      |Because the `TypeTest` value it generates *is the one for Buns*, it will correctly determine if it was a Bun.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Collect
      |
      |We're now just about in a position to implement `askFor`. We'll use the `collect` function, which takes 
      |a *partial function* (an incomplete pattern match) and keeps anything that was matched.
      |
      |```scala
      |  def askFor[C <: HappyFamily](card:C, myCard:HappyFamily)(using typeTest:TypeTest[HappyFamily, C]):Seq[C] = 
      |    cards.collect({ case c:C => c })
      |```
      |
      |```scala
      |val hand = Hand(Seq(Bun(Mr), Block(Mrs), Bun(Master), Chip(Miss))
      |hand.askFor(Bun(Mrs)) // :Seq[Bun]   Seq(Bun(Mr), Bun(Master))
      |```
      |
      |It turns out very short, but we've had the compiler do some clever things for us.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Practicality
      |
      |I've shown you a lot of machinery, that we can use if we want to keep our type-checking very tight.
      |
      |But it's a trade-off - there's probably nothing wrong with saying that `askFor` takes a card and returns
      |a sequence of cards. In which case we don't need all this machinery.
      |
      |```scala
      |  def askFor(card:HappyFamily):Seq[HappyFamily] = 
      |    cards.filter({ c => c.ordinal == card.ordinal })
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
