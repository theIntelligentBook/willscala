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
      |def zen()(using ec:ExecutionContext) =
      |  val randomStr = Random.nextString(4)
      |  println(s"A call was made with random String $randomStr")
      |  wsClient.url(s"https://api.github.com/zen?$randomStr").get().map(_.body)
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
      |We now have something that *describes* the task we want to perform, but doesn't perform it yet.
      |
      |To make this usable, though, we're going to want to give it some functions and turn it into a monad.
      |
      |---
      |
      |## Adding `unsafeRunAsync()`
      |
      |`Task[T]` is just a function, `ExecutionContext => Future[T]`. It only really has the methods that functions do,
      |for instance `apply(ec)` to run it.
      |
      |But we can appear to add some methods by using extension methods
      |
      |```scala
      |extension[T] (task:Task[T])
      |  def unsafeRunAsync()(using ec:ExecutionContext) = task.apply(ec)
      |```
      |
      |Now, if we have a task, we can say `task.unsafeRunAsync()` to run it
      |(if there's an implicit execution context in scope to run it on)
      |
      |---
      |
      |## Adding `map` and `flatMap`
      |
      |To use `Task[T]` in for-comprehensions, we need to make it more monad-like. Particularly, we're going to
      |need to define `map` and `flatMap`.
      |
      |These can be added as extension methods too.
      |In each case, we want a task that runs this task and then processes the result.
      |
      |```scala
      |extension[T] (task:Task[T])
      |  def unsafeRunAsync()(using ec:ExecutionContext) = task.apply(ec)
      |
      |  def map[B](f: T => B):Task[B] =
      |    (ec) => task.apply(ec).map(f)(using ec)
      |
      |  def flatMap[B](f: T => Task[B]):Task[B] =
      |    (ec) => task.apply(ec).flatMap(t => f(t).apply(ec))(using ec)
      |```
      |
      |---
      |
      |## Now we can use our tasks
      |
      |Let's define `zen` slightly differently - we're going to change the implicit `ExecutionContext` to an explicit
      |one.
      |
      |```scala
      |def zen(ec:ExecutionContext):Future[String] =
      |  val randomStr = Random.nextString(4)
      |  println(s"A call was made with random String $randomStr")
      |  wsClient.url(s"https://api.github.com/zen?$randomStr").get().map(_.body)(using ec)
      |```
      |
      |Now, `zen` is of type `ExecutionContext => Future[String]`. i.e., it is a `Task[String]`.
      |
      |```scala
      |val zenIsATask:Task[String] = zen
      |```
      |
      |---
      |
      |## Let's define a combined task
      |
      |Because `zen` is a task, it won't run until we call `unsafeRunAsync()` (or call `apply(executionContext)`).
      |
      |But because it's monad-like, we can use it in for-comprehensions to describe bigger tasks
      |
      |```scala
      |val twoZens:Task[String] =
      |  for
      |    a <- zen
      |    b <- zen
      |  yield s"$a then $b"
      |```
      |
      |No web calls have been made yet. We've just described the *task* of making two web calls.
      |
      |---
      |
      |## Let's run our combined task
      |
      |To run my combined task I pick an execution context and call `unsafeRunAsync`
      |
      |```scala
      |import scala.concurrent.ExecutionContext.Implicits.global
      |for result <- twoZens.unsafeRunAsync() do
      |  println(result)
      |  System.exit(0)
      |```
      |
      |This task is also reusable (if we run it again, it runs again).
      |
      |---
      |
      |## Tasks are referentially transparent
      |
      |Tasks are referentially transparent until we call `unsafeRunAsync()`
      |
      |```scala
      |val twoZens:Task[String] =
      |  for
      |    a <- zen
      |    b <- zen
      |  yield s"$a then $b"
      |```
      |
      |is
      |
      |```scala
      |val twoZens:Task[String] =
      |  for
      |    a <- (ec:ExecutionContext) => // some code
      |    b <- (ec:ExecutionContext) => // some code
      |  yield s"$a then $b"
      |```
      |
      |Nothing has really run yet - we're just composing functions.
      |
      |---
      |
      |## Tasks are an effect system
      |
      |Types like `Task[T]` are also sometimes considered to be an *effect system*
      |
      |When we call `unsafeRunAsync()`, they have an effect on the world: e.g. making a network call that might
      |fetch input or update something on another system.
      |
      |However, we've represented this in the type: `Task[T]`. Whenever we receive something of type `Task[T]`,
      |we know that (when run) it will have an effect on the world.
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
