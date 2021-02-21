package willscala.functional

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}


val sideBySideStyling = Styling(
  """display: grid;
    |grid-template-columns: 40ch auto;
    |column-gap: 2rem;
    |""".stripMargin).register()

def sideBySide(a:VHtmlNode)(b:VHtmlNode) = <.div(^.cls := sideBySideStyling.className, a, b)

val stackPanelStyling = Styling(
  "height: 100%; position: relative; top: 0; width: 20ch;"
).modifiedBy(
  " .frames" -> "margin-top: auto; margin-bottom: 0; width: 100%;",
  " .frame:first-child" -> "background-color: antiquewhite;",
  " .frame" -> "border-top: 1px solid #aaa; width: 20ch; margin: 0;"
).register()

case class StackFrame(vars:String*)

def stackPanel(frames:StackFrame*):VHtmlNode = <.div(^.cls := stackPanelStyling.className,
  <.h4("Stack"),
  <.div(^.cls := "frames",
    for f <- frames yield <.pre(^.cls := "frame", f.vars.mkString("\n"))
  )
)


def example1 = unique(sideBySide(
  <.pre(
    """def triangular(accum:Int, n:Int) = 
      |  if n <= 0 then
      |    accum
      |  else
      |    triangular(accum + n, n - 1)
      |""".stripMargin)
)(marked("A recursive function (calls itself)")))


def example2 = <.div(<.h4("Code"), <.pre("""
        |def triangular(accum:Int, n:Int) = 
        |  if n <= 0 then
        |    accum
        |  else
        |    triangular(accum + n, n - 1)
        |    
        |triangular(0, 3)
        |
        |
        |""".stripMargin))


def example3 = <.div(
  <.h4("Code"), 
  <.pre("""
     |@tailrec
     |def triangular(accum:Int, n:Int) = 
     |  if n <= 0 then
     |    accum
     |  else
     |    triangular(accum + n, n - 1)
     |    
     |triangular(0, 3)
     |""".stripMargin)
)

val tailRecursion = DeckBuilder(1280, 720)
  .markdownSlide(
    """
      |# Recursion and the Stack
    """.stripMargin).withClass("center middle")
  .markdownSlide(
      """## Iteration
        |
        |```java
        |int total = 0;
        |for (int i = 0; i < 100; i++) {
        |  total += i;
        |}
        |```
        |
        |Both `i++` and `total += i` involve *mutation*. This is something functional programmers try to avoid.
        |
        |""".stripMargin)
  .markdownSlide("## To understand recursion, first you must understand recursion").withClass("center middle")
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """triangular(0, 3)
          |""".stripMargin)
    )(marked("What we're going to evaluate")),    
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."), 
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  triangular(0 + 3, 3 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace `triangular(3, 0)` with the definition
          |
          |Substituting 
          | * `n` = `3`
          | * `accum` = `0`
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  triangular(3, 2)
          |
          |""".stripMargin)
    )(
      marked(
        """Rewrite arithmetic
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  if 2 <= 0 then
          |    3
          |  else
          |    triangular(3 + 2, 2 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace inner call to `triangular` with its defintion
          |
          |Substituting
          |* `n` = `2`
          |* `accum` = `3`
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  if 2 <= 0 then
          |    3
          |  else
          |    triangular(5, 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Rewrite arithmetic
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  triangular(5, 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Ok, let's rewrite the inner `if`
          |
          |Remember: *the order of rewriting shouldn't matter*
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  if 1 <= 0 then
          |    5
          |  else 
          |    triangular(5 + 1, 1 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace the call to `triangular` with its definition
          |
          |Substituting
          |* `n` = `1`
          |* `accum` = `5`
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  if 1 <= 0 then
          |    5
          |  else 
          |    triangular(6, 0)
          |
          |""".stripMargin)
    )(
      marked(
        """Rewrite arithmetic
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  triangular(6, 0)
          |
          |""".stripMargin)
    )(
      marked(
        """Rewrite the inner `if`
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  if 0 <= 0 then
          |    6
          |  else
          |    triangular(6 + 0, 0 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace the call to `triangular` with its definition
          |
          |Substituting
          |* `n` = `0`
          |* `accum` = `6`
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """if 3 <= 0 then
          |  0
          |else 
          |  6
          |
          |""".stripMargin)
    )(
      marked(
        """Rewriting the inner `if`
          |
          |I didn't bother rewriting the arithmetic this time - it was going to get thrown away.
          |""".stripMargin)
    )
  ))
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """6
          |
          |""".stripMargin)
    )(
      marked(
        """Rewriting the outer `if`
          |
          |We've reached the answer by rewriting
          |""".stripMargin)
    )
  ))
  .markdownSlide(
    """## Recursion and the Stack
      |
      |Although *we* can come to the answer by rewriting, in practice Scala runs on the JVM and that means it
      |executes using a stack.
      |
      |Let's model what happens on the stack while the calls take place.
      |""".stripMargin)
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel())
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("accum = 5", "n = 1", "PC"), StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("accum = 6", "n = 0", "PC"), StackFrame("accum = 5", "n = 1", "PC"), StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6", "accum = 6", "n = 0", "PC"), StackFrame("accum = 5", "n = 1", "PC"), StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6"), StackFrame("accum = 5", "n = 1", "PC"), StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6", "accum = 5", "n = 1", "PC"), StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6"), StackFrame("accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6", "accum = 3", "n = 2", "PC"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6"), StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6", "accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Stack example..."),
    sideBySide(example2)(stackPanel(StackFrame("return 6")))
  ))
  .markdownSlide(
    """## Recursion and the Stack
      |
      |At runtime, the stack grew and shrank. So although our *program* doesn't have any side-effects, the machine that
      |runs it does so imperatively using mutation.
      |
      |If our recursion is very large, the stack could grow too big and we'd get a *stack overflow* exception.
      |
      |""".stripMargin)
  .markdownSlide(
    """## Tail recursion (or tail call optimisation)
      |
      |In our example, we don't do anything with the value from the recursive call except return it.
      |
      |```scala
      |def triangular(accum:Int, n:Int) = 
      |  if n <= 0 then
      |    accum
      |  else
      |    triangular(accum + n, n - 1)
      |```
      |
      |This means we can *reuse* the stack frame - we don't need the local variables in this stack frame any more.
      |""".stripMargin)
  .veautifulSlide(<.div(
    <.h2("Tail-recursive example..."),
    sideBySide(example3)(stackPanel(StackFrame("accum = 0", "n = 3", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Tail-recursive example..."),
    sideBySide(example3)(stackPanel(StackFrame("accum = 3", "n = 2", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Tail-recursive example..."),
    sideBySide(example3)(stackPanel(StackFrame("accum = 5", "n = 1", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Tail-recursive example..."),
    sideBySide(example3)(stackPanel(StackFrame("accum = 6", "n = 0", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Tail-recursive example..."),
    sideBySide(example3)(stackPanel(StackFrame("return 6", "accum = 6", "n = 0", "PC")))
  ))
  .veautifulSlide(<.div(
    <.h2("Tail-recursive example..."),
    sideBySide(example3)(stackPanel(StackFrame("return 6")))
  ))
  .markdownSlide(
    """## Mutual recursion
      |
      |Tail recursion works if you a function is *self-recursive* - that is, it calls itself.
      |
      |However, here's something it won't work for. 
      |(Again, this is a made-up example, not tremendously useful, but it's a simple way of showing a technique).
      |
      |```scala
      |def isEven(n:Int):Boolean = 
      |  if n == 0 then true else isOdd(n - 1)
      |  
      |def isOdd(n:Int):Boolean = 
      |  if n == 0 then false else isEven(n - 1)
      |
      |isEven(100000) // StackOverflow
      |```
      |
      |We get a stack overflow exception, but it's not easy to make this tail recursive because it's a mutual recursion.
      |
      |""".stripMargin)
  .markdownSlide(
    """## Trampolines
      |
      |To make a tail-recursive solution, let's move some of the management of the call state from the *stack*
      |to the *heap*. We're going to create some objects that represent the computation we're doing now.
      |
      |Let's declare a *sealed trait* - one whose only members are defined in the same file
      |
      |```scala
      |/** Something that represents a computation step */
      |sealed trait Trampoline[T]
      |
      |/** Our computation has finished */
      |case class Done[T](val result: T) extends Trampoline[T]
      |
      |/** We have more work to do, in f */
      |case class More[T](val f: () => Trampoline[T]) extends Trampoline[T]
      |```
      |
      |We've declared these as `case class`es because later we're going to use the `match` keyword to ask which one
      |we've got.
      |""".stripMargin
  )
  .markdownSlide(
    """## Converting our code to Trampolines
      |
      |Now, let's redefine `isEven` and `isOdd` in terms of trampolines
      |
      |```scala
      |def isEven(n:Int):Trampoline[Boolean] = 
      |  if n == 0 then Done(true) else More(() => isOdd(n - 1))
      |  
      |def isOdd(n:Int):Trampoline[Boolean] = 
      |  if n == 0 then Done(false) else More(() => isEven(n - 1))
      |```
      |
      |The `More` objects we're creating are objects on the *heap* (the part of memory where new objects are stored) 
      |not the *stack*. We've changed our functions from mutally recursive functions, to functions that always return an object containing
      |a computation.
      |
      |If we call
      |
      |```scala
      |isEven(100000)
      |```
      |
      |all we will get is a `More` object, `More(() => isOdd(n - 1))`, "closing over" `n = 100000`. 
      |No recursion has happened yet.
      |""".stripMargin
  )
  .markdownSlide(
    """## Running a trampoline
      |
      |To make our code run, we need to write an interpreter for the Trampoline. 
      |Since Trampoline is a trait, let's do it there.
      |
      |`match` lets us do a *pattern match* to see whether this is a `Done` or a `More`.
      |
      |```scala
      |/** Something that represents a computation step */
      |sealed trait Trampoline[T]:
      |    @tailrec  
      |    final def run:T = this match {
      |        case Done(result) => result
      |        case More(f) => f().run
      |    }
      |```
      |
      |Fortunately, our interpreter *is* tail-recursive (though we need to declare `run` as `final` so it can't be
      |overridden).
      |
      |Our `isEven` and `isOdd` calls can now be executed like this:
      |
      |```scala
      |isEven(100000).run  // true
      |```
      |
      |Although it won't overflow the stack, we are creating and discarding a lot of objects in heap memory, so it does have some
      |performance overhead.
      |""".stripMargin
  )
  .markdownSlide(
    """## The good news
      |
      |In practice, Scala programmers don't often find themselves needing to manually writing little recursive functions.
      |(But it's good to learn).
      |
      |You'll spend more time using *higher order functions*, e.g. `map`, `fold`, `flatMap`, etc., which we'll look at
      |in the next chapter.
      |""".stripMargin)
  .markdownSlide(
    """## A little pragmatism
      |
      |Locally, this is impure:
      |
      |```scala
      |def triangular(n:Int) = { 
      |  var total = 0
      |  var i = 0 
      |  while i <= n
      |    total += n
      |    i++
      |    
      |  total
      |}
      |```
      |
      |However, the mutation never escapes the function.
      |To any caller, it might as well be pure.
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides