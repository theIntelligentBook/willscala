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
    """def sum(accum:Int, n:Int) = 
      |  if n <= 0 then
      |    accum
      |  else
      |    sum(accum + n, n - 1)
      |""".stripMargin)
)(marked("A recursive function (calls itself)")))


def example2 = <.div(<.h4("Code"), <.pre("""
        |def sum(accum:Int, n:Int) = 
        |  if n <= 0 then
        |    accum
        |  else
        |    sum(accum + n, n - 1)
        |    
        |sum(0, 3)
        |
        |
        |""".stripMargin))


def example3 = <.div(
  <.h4("Code"), 
  <.pre("""
     |@tailrec
     |def sum(accum:Int, n:Int) = 
     |  if n <= 0 then
     |    accum
     |  else
     |    sum(accum + n, n - 1)
     |    
     |sum(0, 3)
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
        |int sum = 0;
        |for (int i = 0; i < 100; i++) {
        |  sum += i;
        |}
        |```
        |
        |Both `i++` and `sum += i` involve *mutation*. This is something functional programmers try to avoid.
        |
        |""".stripMargin)
  .markdownSlide("## To understand recursion, first you must understand recursion").withClass("center middle")
  .veautifulSlide(<.div(
    <.h2("An example with rewriting..."),
    example1,
    sideBySide(
      <.pre(
        """sum(0, 3)
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
          |  sum(0 + 3, 3 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace `sum(3, 0)` with the definition
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
          |  sum(3, 2)
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
          |    sum(3 + 2, 2 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace inner call to `sum` with its defintion
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
          |    sum(5, 1)
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
          |  sum(5, 1)
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
          |    sum(5 + 1, 1 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace the call to `sum` with its definition
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
          |    sum(6, 0)
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
          |  sum(6, 0)
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
          |    sum(6 + 0, 0 - 1)
          |
          |""".stripMargin)
    )(
      marked(
        """Replace the call to `sum` with its definition
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
      |def sum(accum:Int, n:Int) = 
      |  if n <= 0 then
      |    accum
      |  else
      |    sum(accum + n, n - 1)
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
      |def sum(n:Int) = { 
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
      |However, the local variables being mutated (`i` and `total`) *never escape the function*. 
      |
      |To any caller, it might as well be pure.
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides