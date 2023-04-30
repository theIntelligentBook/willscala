package willscala.actors

import com.wbillingsley.veautiful.html.*
import com.wbillingsley.veautiful.doctacular.*

import willscala.Common._
import willscala.ScastieSnippet
import willscala.given
import com.wbillingsley.veautiful.PushVariable
import scala.util.Random

import org.scalajs.dom


val reactiveIntro = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Reacting to a single value
      |
      |""".stripMargin).withClass("center middle")
  .markdownSlides(
    """|## Reactive Programming
       |
       |**Reactive programming** is a paradigm where we try to write declaratively, describing how a program responds to events.
       |
       |For example:
       |
       |* Responding to user input
       |* Responding to events as they arrive
       |* Responding to changes in dependent values
       |
       |---
       |
       |## One we've already seen
       |
       |In the single-value case, we've already seen a case of describing how a program behaves as events occur: `Future`s, `Promise`s, and `Task`s
       |
       |For example: 
       |
       |```scala
       |  // Now we can create our web client and make a request
       |  val wsClient = StandaloneAhcWSClient()
       |  val f = wsClient
       |    .url("https://api.github.com/zen")
       |    .get()
       |
       |  // When it completes successfully, print the body
       |  f.foreach({ req => println(req.body) })
       |```
       |
       |In the `foreach` line, we described how the program should *react* when the data from the web request came in.
       |
       |---
       |
       |## Referential transparency, again
       |
       |When we were using `Future[T]`, we were using an eager structure, that made the request as soon as we declared it.
       |
       |So, we introduced the idea of `Task[T]` that could describe a re-usable workflow that was asynchronous and reactice
       |
       |```scala
       |val t = Task(
       |  wsClient
       |    .url("https://api.github.com/zen")
       |    .get().map(_.body)
       |)
       |
       |val combined = for
       |  a <- t
       |  b <- t
       |yield s"$a then $b"
       |
       |// Now we can re-use that flow as often as we like
       |combined.unsafeRunAsync()
       |combined.unsafeRunAsync()
       |```
       |
       |---
       |
       |## Something locally reactive
       |
       |`Future`, `Promise`, and `Task` are asynchronous. However we can also react to data synchronously. 
       |A common example is the `Observer` pattern, in which an object notifies some number of listeners if it's been updated
       |
       |```scala
       |trait Listener[T]:
       |  def onUpdate(value:T):Unit
       |
       |class Observable[T](initial: T):
       |  private var listeners:Set[Listener[T]] = Set.empty
       |  def subscribe(listener: Listener[T]):Unit = listeners += listener
       |
       |  private var _value = initial
       |  def value = _value
       |  def value_=(newValue:T):Unit = 
       |    _value = newValue
       |    for l <- listeners do l.onUpdate(newValue)
       |
       |val myObservable = Observable(3)
       |myObservable.subscribe((v) => println(s"Value changed to $v"))
       |myObservable.value = 4 // Prints out "Value changed to 4"
       |```
       |
       |[Try it on Scastie](https://scastie.scala-lang.org/OCuVVZBbSlONIRZ64M48lA)
       |
       |---
       |
       |## Chainging computation
       |
       |Manually having to register listeners, however, wouldn't be very declarative. 
       |Many frameworks let you have observable values and *bind* new observables to them.
       |
       |```scala
       |val derivedValue = observable.map(_ * 3)
       |```
       |
       |---
       |
       |
       |
       |
       |""".stripMargin
  )
  .veautifulSlide(<.div(
    markdown.div(
      """|## PushVariables
         |
         |We'll get into proper libraries for this in future chapters. 
         |For the moment, let's demonstrate an example so tiny I built it into my web framework.
         |
         |`PushVariable[T]` is a class that holds a variable, and can trigger an event when you push a value to it.
         |
         |It's like a synchronous version of `Promise[T]`
         |
         |```scala
         |val push = PushVariable(3)(onUpdate = (x) => dom.window.alert(s"New value of $x"))
         |button("Set a random value", ^.on.click --> { push.value = Random.nextInt(100) })
         |```
         |
         |""".stripMargin
    ), 
    {
      val push = PushVariable(3)(onUpdate = (x) => dom.window.alert(s"New value of $x"))
      <.button("Set a random value", ^.on.click --> { push.value = Random.nextInt(100) })
    }
  ))
  .markdownSlide(
    """|## DynamicValues
       |
       |If `PushValue` is like a `Promise`, letting you set a value, `DynamicValue` is like `Future`, dynamically receiving it
       |
       |```scala
       |val primeString:DynamicValue[String] = 
       |  for v <- push.dynamic yield 
       |    if prime(v) then s"$v is prime" else s"$v is not prime"
       |```
       |
       |Unlike `Future`/`Promise`, though, this is synchronous. The dynamic value's new value is being triggered by listeners on the `PushValue` 
       |on the same thread that set it. 
       |
       |""".stripMargin
  )
  .veautifulSlide(<.div(
    markdown.div(
      """|## A dynamic value in action
         |
         |If `PushValue` is like a `Promise`, letting you set a value, `DynamicValue` is like `Future`, dynamically receiving it
         |
         |```scala
         |// For numbers up to 99 this should be fast enough 
         |def prime(n:Int):Boolean = !(2 until n).exists((x) => n % x == 0)
         |
         |val push = PushVariable(3)(onUpdate = (x) => dom.window.alert(s"New value of $x"))
         |val primeString = 
         |  for v <- push.dynamic yield 
         |    if prime(v) then s"$v is prime" else s"$v is not prime"
         |
         |Seq(
         |  <.button("Set a random value", ^.on.click --> { push.value = Random.nextInt(100) }),
         |  <.dynamic.div(primeString)
         |)
         |```
         |
         |""".stripMargin
    ), 
    {
      // For numbers up to 99 this should be fast enough 
      def prime(n:Int):Boolean = !(2 until n).exists((x) => n % x == 0)

      val push = PushVariable(3)(onUpdate = (x) => dom.window.alert(s"New value of $x"))
      val primeString = 
        for v <- push.dynamic yield 
          if prime(v) then s"$v is prime" else s"$v is not prime"

      Seq(
        <.button("Set a random value", ^.on.click --> { push.value = Random.nextInt(100) }),
        <.dynamic.div(primeString)
      )
    }
  ))
  .veautifulSlide(<.div(
    markdown.div(
      """|## Dynamic values can change more than once
         |
         |It's usual for reactive libraries to let the value change more than once. 
         |
         |For instance, in my UI library, there's a dynamic value `Animator.now` that changes every animation frame.
         |Is value is about how long the page has been running (JavaScript's `performance.now`) rather than the time on a clock, 
         |but we could still use it to derive a dynamic value with the current date:
         |
         |```scala
         |<.dynamic.p(
         |  for _ <- Animator.now yield (new scalajs.js.Date()).toString
         |)
         |```
         |
         |""".stripMargin
    ), 
    {
      <.dynamic.p(
        for _ <- Animator.now yield (new scalajs.js.Date()).toString
      )
    }
  ))
  .markdownSlides(
    """|## Ok, that's a toy...
       |
       |There are a lot of libraries that implement observable values, not just in reactive libraries.
       |
       |For example, the JavaFX UI toolkit uses [observable values](https://openjfx.io/javadoc/17/javafx.base/javafx/beans/value/ObservableValue.html) and 
       |"bindings" to automatically update UI elements as data behind them changes.
       |
       |The point at this stage isn't really to teach you how any particular library works, but to explain the concept of declaring derived values rather than
       |manually wiring up listeners.
       |
       |
       |---
       |
       |## Towards streams
       |
       |So far, we've just dealt with single values changing
       |
       |* synchronously for `PushVariable`/`DynamicValue` (or any other observer pattern implementation)
       |* asynchronously for `Promise`/`Future`
       |
       |However, it'll start to get more complex when we want to talk about *streams* of values:
       |
       |* emitters emitting events asynchronously
       |* consumers processing and consuming the stream asynchronously
       |
       |That's the destination, but first let's talk about an old but simple model of co-ordinating work between different tasks: Actors.
       |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
