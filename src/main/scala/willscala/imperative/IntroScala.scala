package willscala.imperative

import com.wbillingsley.veautiful.templates._
import willscala.Common

val introScala = DeckBuilder(1280, 720)
  .markdownSlide(
  """
    |# An imperative intro to Scala
    |
    |.byline[ *Will Billingsley, CC-BY* ]
    |""".stripMargin).withClass("center middle")
  .markdownSlides(
    """
      |### Scala compile targets
      |
      |* Compiles to run on the Java Virtual Machine (JVM)
      |
      |* Scala.js compiles to JavaScript (Node.js or browsers)
      |
      |* Scala Native compiles to native machine code
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Expression-oriented
      |
      |Everything in Scala is an expression, and has a return type
      |
      |```scala
      |// Int
      |1 * 7
      |
      |// Boolean
      |true == !false
      |
      |// String
      |"Hello World"
      |
      |// If statements have a return type
      |if ("February".contains("r")) 
      |  "it's going to be hot" 
      |else 
      |  "it's going to be cold"
      |
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """
      |## Naming things
      |""".stripMargin).withClass("center middle")
  .markdownSlide("""
      |
      |### Values 
      |
      |Values (things which do not change) can be declared with `val`
      |
      |```scala
      |val pi:Double = 3.1415927
      |```
      |
      |Type declarations come after the variable name. But Scala can also do "type inference", so we can often omit them
      |
      |```scala
      |val pi = 3.1415927
      |```
      |""".stripMargin)
    .markdownSlide("""
      |
      |### Variables
      |
      |Variables (things which can be changed) can be declared with `var`
      |
      |```scala
      |var radius:Int = 10
      |```
      |
      |Again, we can omit the type annotation and the compiler will infer it
      |
      |```scala
      |var radius = 10
      |```
      |
      |""".stripMargin)
    .markdownSlide("""
      |
      |### Functions
      |
      |Functions can be declared with `def`
      |
      |```scala
      |def circumference(r:Double):Double = 2 * pi * r
      |```
      |
      |Notice that we say that the function *equals* its result
      |
      |Again we can omit the type annotation on the function if we want
      |
      |```scala
      |def circumference(r:Double) = 2 * pi * r
      |```
      |
      |(Though I generally encourage explicitly declaring the return type of functions)
      |
      |""".stripMargin)
    .markdownSlide("""
      |
      |### Unit
      |
      |What about functions that really don't have a result to return?
      |
      |```scala
      |def sayHello(person:String):Unit = println("Hello " + person)
      |```
      |
      |`Unit` is a type in Scala which is roughly equivalent to `void` in Java.
      |
      |There is one value of type `Unit`: `()`
      |
      |```scala
      |def notVeryUseful:Unit = ()
      |```
      |
      |""".stripMargin)
    .markdownSlide("""
      |
      |### Functions don't have to have arguments
      |
      |```scala
      |def sayHello = println("Hello there!")
      |```
      |
      |...but if a function has a side-effect, it's good practice to give it ()
      |
      |```scala
      |def sayHello() = println("Hello there!")
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """
      |### Function names
      |
      |Functions in scala can have names with unicode characters or symbols. This is very common in Scala libraries.
      |
      |For instance, if I write this:
      |
      |```scala
      |def myComplexFunction(s:String, i:Int):List[String] = ???
      |```
      |
      |The `???` on the right hand side is a call to the function `???`, which is defined in the standard library as:
      |
      |```scala
      |def ??? : Nothing = throw new NotImplementedError
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |### Functions can have more than one argument list
      |
      |```scala
      |def saySomething(what:String, to:String)(exclamationMark:Boolean) = ???
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Named and default values
      |
      |Functions can have default values, and arguments can be referred to by their names
      |
      |```scala
      |def hello(name:String = "World") = println("hello " + name)
      |
      |hello()
      |
      |hello(name = "Class!")
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """
      |## Imperative loops
      |""".stripMargin).withClass("center middle")
  .markdownSlide("""
      |
      |### Loop with while
      |
      |Scala has while loops. And like Java, they can be written using braces
      |
      |```scala
      |var i = 0
      |while(i < 100) {
      |  if (i % 15 == 0) {
      |    println("fizzbuzz")
      |  } else if (i % 5 == 0) {
      |    println("buzz")
      |  } else if (i % 3 == 0) {
      |    println("fizz")
      |  } else println(i)
      |  i += 1
      |}
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Optional braces
      |
      |However, if you're coming from a Python background, Scala 3 can also use indentation instead...
      |
      |```scala
      |var i = 0
      |while i < 100 do
      |  if i % 15 == 0 then
      |    println("fizzbuzz")
      |  else if i % 5 == 0 then
      |    println("buzz")
      |  else if i % 3 == 0 then
      |    println("fizz")
      |  else println(i)
      |  i += 1
      |```
      |
      |The language designers' idea here is that in Scala, wrong indentation can then produce compile errors 
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### There is no `do ... while`
      |
      |Scala used to have a `do ... while` loop, but it was removed in Scala 3, because you can implement it just using
      |while.
      |
      |```scala
      |var i = 0
      |
      |while {
      |  if (i % 15 == 0) {
      |    println("fizzbuzz")
      |  } else if (i % 5 == 0) {
      |    println("buzz")
      |  } else if (i % 3 == 0) {
      |    println("fizz")
      |  } else println(i)
      |  i += 1
      |  
      |  i < 100
      |} do () 
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """
      |### In indentation syntax
      |
      |We can do this using indentation syntax too
      |
      |```scala
      |  var i = 0
      |  
      |  while 
      |    if i % 15 == 0 then
      |      println("fizzbuzz")
      |    else if i % 5 == 0 then
      |      println("buzz")
      |    else if i % 3 == 0 then
      |      println("fizz")
      |    else println(i)
      |    i += 1
      |
      |    i < 100
      |  do ()
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Leading question -- any kind of loops I've missed?
      |
      |""".stripMargin)
  .markdownSlide(
    """
      |## Arrays, Lists, Sequences, Sets
      |""".stripMargin).withClass("center middle")
  .markdownSlide("""
      |
      |### Arrays
      |
      |Scala uses square brackets `[ ]` to parameterise types.
      |
      |```scala
      |val names = new Array[String](3)
      |```
      |
      |And round brackets `( )` to index into arrays
      |
      |```scala
      |names(0) = "William"
      |```
      |
      |Notice that although we declared `names` as a `val`, arrays are mutable. We can't point names to a different array, but we can change its contents.
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Syntactic sugar
      |
      |Indexing into an array looks like it's a method call:
      |
      |```scala
      |names(0)
      |```
      |
      |And to the compiler, it *is* a method call, but on a special method called `apply`.
      |
      |```scala
      |names.apply(0)
      |```
      |
      |If you put the name of a variable, and then call it as if it were a function, Scala will try to call an `apply` method on it
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Update
      |
      |Setting a value in an array is also syntactic sugar, for a call to `update`
      |
      |```scala
      |names(0) = "Fred"
      |```
      |
      |is equivalent to 
      |
      |```scala
      |names.update(0, "Fred")
      |```
      |
      |> when an assignment is made to a variable to which parentheses and one or more arguments have been applied, the compiler will transform that into an invocation of an update method that takes the arguments in parentheses as well as the object to the right of the equals sign -- Odersky & Spoon
      |
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Any
      |
      |Suppose we wanted to have an Array that could hold anything.
      |
      |In Java, we have to have an `Array<Object>` and remember that primitive `int`s are being "autoboxed" to `Integers`
      |
      |In Scala, there is a universal supertype: `Any`
      |
      |```scala
      |val arrayOfAnything = new Array[Any](3)
      |arrayOfAnything(2) = 3
      |arrayOfAnything(2).toString
      |```
      |
      |Behind the scenes it will still autobox (Scala is implemented on Java) but Scala does not have a distinction between `int` and `Integer`. It's just `Int`.
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Lists
      |
      |Most functional programming languages have a `List` type -- a type for an *immutable* singly-linked list of data.
      |
      |```scala
      |val names = List("Fiona", "Euan", "Rebecca", "Hamish")
      |
      |// This won't compile - `List[T]` has no `update` method
      |names(1) = "Matthew"
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Lists
      |
      |An empty list comprises just the element `Nil`
      |
      |Other lists have a `head`, which is an element, and a `tail` which is a List
      |
      |For example:
      |
      |  * `Nil` - the empty list
      |
      |  * `1 :: Nil` - 1 is the head, Nil is the tail
      |
      |  * `2 :: 1 :: Nil` - 2 is the head, 1 :: Nil is the tail
      |  
      |We can also produce lists using `List(2, 1)` but it will create the same structure.
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Appending Lists
      |
      |```scala
      |val extendedNames = "Grace" :: names
      |```
      |
      |Note that we haven't *changed* the list, we've created a *new* list that happens to have the old list as its *tail*
      |
      |```scala
      |extendedNames.tail == names
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Empty lists
      |
      |An empty list can be created in a few ways:
      |
      |```scala
      |val emptyList:List[Int] = List()
      |val anotherEmptyList:List[Int] = Nil
      |val yetAnotherEmptyList:List[Int] = List.empty
      |```
      |
      |But these all give us the same object (`Nil`).
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Sequences
      |
      |Where `List` is a particular struture, `Seq` is a *trait* that indicates a sequence of elements.
      |
      |`List[Int]` is a `Seq[Int]`. But so is `Vector[Int]` (a different structure for holding an immutable sequence)
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Mutable vs Immutable collections
      |
      |In Scala, programmers prefer data-types that are *immutable* (their value can't be changed). 
      |We'll see why when we introduce functional programming.
      |
      |However, the standard library does also provide mutable versions of many collections (in a different package).
      |For example:
      |
      |* Guarunteed to be immutable: `scala.collection.immutable.Seq` 
      |* Guarunteed to be mutable: `scala.collection.mutable.Seq`
      |
      |For `Seq`, there's even a third:
      |
      |* Have the immutable API, but also allow passing subclasses that are really mutable: `scala.collection.Seq`
      |
      |By default, `Seq` is an alias for `scala.collection.immutable.Seq`.
      |""".stripMargin)
  .markdownSlide("""
      |### Programmers expect immutable collections
      |
      |If you're going to use a mutable collection, it's good practice to make it obvious in the code:
      |
      |```scala
      |// Stop at 'mutable' with the import...
      |import scala.collection.mutable
      |
      |// ...so that our type has "mutable" at the start
      |val myMutableSeq:mutable.Seq = ???
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Set
      |
      |`Set` meanwhile is more like a mathematical set -- unordered, and defined by which elements are in it (not how often they are repeated) 
      |
      |```scala
      |val mySet = Set(1, 2, 3, 4)
      |
      |mySet == Set(4, 4, 3, 3, 2, 2, 1, 1)
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Tuples
      |
      |*Tuples* are pairs of objects. 
      |
      |```scala
      |val tuple:(Int, String) = (1, "a")
      |```
      |
      |That's a tuple of an Int and a String
      |
      |We can have longer tuples, and we can miss out the type annotation
      |
      |```scala 
      |val longerTuple = (1, "a", 2, "b", 3, "c")
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Syntactic sugar for tuples
      |
      |Instead of `(1, 2)` we can also write `1 -> 2`
      |
      |This helps make some code more legible. Instead of
      |
      |```scala
      |Seq((1, 'a'), (2, 'b'), (3, 'c'))
      |```
      |
      |we can write
      |
      |```scala
      |Seq(1 -> 'a', 2 -> 'b', 3 -> 'c')
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """
      |### More syntactic sugar for tuples!
      |
      |We can also write tuples as if they were a list that knows its element types:
      |
      |```
      |val a = 1 *: 2 *: 3 *: Tuple()
      |// (1, 2, 3)
      |
      |val b = 4 *: 5 *: 6 *: Tuple()
      |// (4, 5, 6)
      |
      |val c = a ++ b
      |// (1, 2, 3, 4, 5, 6)
      |```
      |
      |But they are not as flexible as lists because the compiler has to track the type of every *individual* element
      |
      |```
      |// The compiler knows the type of this is (Int, Int, Int, String, String)
      |val foo = (1, 1, 1) ++ ("duck", "goose")
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Destructuring assignment
      |
      |A fairly common pattern if you have a tuple is to extract it into variables
      |
      |```scala
      |val myTuple = (1, "a")
      |val letter = myTuple._2 // can be inferred this is a String
      |```
      |
      |You can also extract more than one variable at once
      |
      |```scala
      |val (number, letter) = myTuple
      |```
      |
      |""".stripMargin)
  .markdownSlide("""
      |
      |### Destructuring assignment on arrays
      |
      |It's not just tuples that can use destructuring assignment; lots of classes can.
      |
      |For example, suppose I have an array passed to me from Java, and I know there's five of them at compile time.
      |
      |```java
      |String[] names = new String { "Algernon", "Bertie", "Cecily", "Dahlia", "Earnest" }
      |```
      |
      |```scala
      |val Array(a, b, c, d, e) = names
      |```
      |
      |
      |""".stripMargin
  )
  .renderSlides
