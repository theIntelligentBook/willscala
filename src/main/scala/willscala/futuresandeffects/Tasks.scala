package willscala.futuresandeffects

import com.wbillingsley.veautiful.templates._
import willscala.Common._

val tasksDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Tasks
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """## Futures are not referentially transparent
      |
      |Although Futures compose in for-notations as if they were monoids, they are not referentiallly transparent
      |
      |```scala
      |def zen():Future[String] = wsClient.url("https://api.github.com/zen").get().map(_.body)
      |
      |// This makes one network call and gets one zen
      |val z = zen()
      |for
      |  a <- z
      |  b <- z
      |do println(s"$a then $b")
      |
      |// This makes two network calls and gets two different zens
      |for
      |  a <- zen()
      |  b <- zen()
      |do println(s"$a then $b")
      |```
      |
      |---
      |
      |## Futures are eager...
      |
      |Part of the reason they are not referentially transparent is because they are eager.
      |As soon as you have made them, they've made any network request they contain and tried to memoise the result.
      |
      |Perhaps we should have a type that can represent a task we want to do, *but not run it until we're told*.
      |
      |```scala
      |// This doesn't run the task. It's just an object representing the task.
      |val task = Task(doSomething())
      |```
      |
      |Then that task could be run repeatedly wherever we want
      |
      |```scala
      |// This would run the same task twice
      |task.unsafeRunAsync()
      |task.unsafeRunAsync()
      |```
      |
      |---
      |
      |## Tasks are referentially transparent
      |
      |If we make our `Task` type a monoid, then unlike `Future` it will be referentially transparent
      |
      |```scala
      |val t = Task(someWebCall())
      |val combined = for
      |  a <- t
      |  b <- t
      |yield s"$a then $b"
      |```
      |
      |`combined` hasn't been run *at all*. It is now a `Task[String]` representing running task `t` twice and
      |composing the answers.
      |
      |```scala
      |// This would then run the combined task
      |combined.unsafeRunAsync()
      |```
      |
      |---
      |
      |## Tasks in practice
      |
      |There are a number of popular libraries that implement `Task` types, often with powerful
      |additional functionality (e.g. controlling when tasks are parallelised and when they aren't).
      |
      |These include:
      |
      |* [Monix](https://monix.io/)
      |* [ZIO](https://zio.dev/)
      |* [cats-effect](https://typelevel.org/cats-effect/)
      |
      |However, I'm going to show you roughly how we can build one just using extension methods,
      |based on my own [handy](https://github.com/wbillingsley/handy) library.
      |
      |---
      |
      |## Tasks are lazy
      |
      |The key feature of a Task is it doesn't run until we need it to. It's *lazy*.
      |
      |Something else that doesn't run until we tell it to is *a function*.
      |
      |So, for our home-grown cheap `Task` type, let's type alias a function that produces a Future
      |
      |```scala
      |type Task[+T] = ExecutionContext => Future[T]
      |```
      |
      |---
      |
      |## A companion object
      |
      |... still writing
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
