package willscala.asyncstreams

import com.wbillingsley.veautiful.doctacular.*
import willscala.Common._
import willscala.given

val catsEffectDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Cats Effect
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """## An asynchronous `IO` monad
      | 
      |Earlier, we described how we could create a little home-grown *synchronous* IO monad, loosely similar to what you might see in Haskell
      |
      |The Cats Effect library introduces an *asynchronous* `IO` monad, that includes features like being able to cancel a process after it has started.
      |
      |So, code using Cats Effect can look largely like a for-comprehension. A simple example from the Cats Effect docs:
      |
      |```scala
      |import cats.effect.IO
      |
      |for {
      |  _ <- IO.println("Hello")
      |  _ <- IO.println("World")
      |} yield ()
      |```
      |
      |Ok, those are synchronous tasks, but we could include asynchronous ones.
      |
      |---
      |
      |## IO monads describe a program
      |
      |When we implemented our own synchronous `IO` monad, our program didn't run until we called `unsafeRunSync()`
      |For example:
      |
      |```scala
      |// We can describe our program in terms of its IO actions
      |def wordleGame:IO[Unit] = 
      |  for 
      |    wordList <- getWordList
      |    target <- chooseWord(wordList)
      |    gs = GameState(wordList, target, 6)
      |    _ <- gs.play
      |  yield ()
      |
      |// To execute our program, we run the program description
      |@main def runWordle = wordleGame.unsafeRunSync()
      |```
      |
      |Our function producing an `IO[Unit]` described a program that *could be* run, but didn't immediately run it.
      |
      |---
      |
      |## Cats-Effect IO monads also describe a program
      |
      |Similarly, Cats Effect's asynchronous `IO` monad does not immediately run your program. It lets you describe
      |a program that can be run
      |
      |```scala
      |// We could descibe a program
      |val prog = for {
      |  name <- IO.readLine
      |  _ <- IO.sleep(1.second)
      |  _ <- IO.println(s"Hello $name")
      |} yield *()
      |
      |// We could run it asynchronously
      |prog.unsafeRunAsync()(using IORuntime.global)
      |```
      |
      |Here, I've explicitly shown the `using` parameter to make clear that the `IO` monad has to be
      |run on a *runtime*. (Something has to schedule its asynchronous tasks to take place on real threads).
      |
      |---
      |
      |## Different kinds of thread
      |
      |Traditionally, there are a few different levels of concurrency in how programs can run on a computer
      |
      |* *Processes* have their own heap memory space (i.e., where objects are allocated) and are kept independent of each other by the operating system
      |
      |* *Kernel threads* can exist within the same process and share heap memory space (e.g., objects) but have their own *program counter*
      |  (their execution point in the program) and *stack*. They are managed by the operating system, and can be *preemptively multitasked* 
      |  (i.e. paused by the operating system to let other threads run)
      |
      |* *User threads* are where a virtual machine, rather than the operating sytem, simulates threads. Lighter than kernel threads, but still fairly heavy
      |  because the runtime has to know how to "preempt" (interrupt) one thread so than another thread can have its turn.
      |
      |* *Fibers* (or *green threads*) are very lightweight simulations of threads. The virtual machine doesn't do preemptive multitasking. 
      |  Instead, think of the fiber as something that keeps producing tiny tasks, and a runtime thread as something that
      |  takes those tiny tasks and schedules them on a real thread.
      |
      |---
      |
      |## Cats Effect and Fibers
      |
      |In Cats Effect, your for-comprehension is (effectively) a fiber, with each step being an *effect* (microtask) that the runtime will queue onto a real
      |thread:
      |
      |```scala
      |import cats.effect.IO
      |
      |// This for-comprehension expresses a fiber of microtasks
      |for {
      |  _ <- IO.println("Hello")  // each step is an effect
      |  _ <- IO.println("World")  // that the runtime will schedule
      |} yield ()
      |```
      |
      |---
      |
      |## Writing a simple app
      |
      |If the app you are writing is very simple, Cats Effect supplies an `IOApp` trait so that you don't have to write
      |the call to `unsafeRunAsync` and specify the runtime yourself.
      |
      |For example:
      |
      |```scala
      |import cats.effect._
      |
      |object Main extends IOApp {
      |
      |  override def run(args:List[String]): IO[Unit] = {
      |    for {
      |      name <- IO.readLine
      |      _ <- IO.sleep(1.second)
      |      _ <- IO.println(s"Hello $name")
      |    } yield ()
      |  }
      |
      |}
      |```
      |
      |---
      |
      |## Async / await via macros
      |
      |You might feel that having your program inside a for-comprehension looks a bit ugly.
      |Some other languages (e.g. JavaScript and Rust) have an async/await syntax that lets you
      |write asynchronous code as if it were synchronous code.
      |
      |Cats Effect also supports this, using *macros* (a way of hooking into the compiler process
      |and rewriting your code as it is compiled). From the documentation:
      |
      |```scala
      |import cats.effect.cps._
      |
      |async[IO] {
      |  IO.println("Hello").await
      |  IO.println("World").await
      |}
      |```
      |
      |However, it's a trade-off whether you think the simplicity gained by this change in notation
      |is worth the conceptual complexity of importing macros and knowing that your code is being transformed.
      |
      |---
      |
      |## Cancellation
      |
      |If we have an asynchronous program with a lot of external tasks to run, we might sometimes find we want
      |to *cancel* execution of it.
      |
      |In Cats-Effect, one fiber can call `cancel` on another. This is an asynchronous function that returns 
      |when the other fiber has agreed to be cancelled. 
      |
      |```scala
      |import scala.concurrent.duration._
      |
      |lazy val loop: IO[Unit] = IO.println("Hello, World!") >> loop
      |loop.timeout(5.seconds)   // => IO[Unit]
      |```
      |
      |By default, most tasks are cancellable at any time, but the fact
      |that this uses *fibers* (cooperative multitasking) means a fiber can define when it will cancel.
      |
      |```scala
      |// Any cancel signals would be delayed until all this has completed
      |def atomic = IO.uncancellable { _ => 
      |  for {
      |    _ <- IO.println("This")
      |    _ <- IO.println("won't")
      |    _ <- IO.println("be")
      |    _ <- IO.println("interrupted")
      |  } yield ()
      |}
      |```
      |
      |---
      |
      |## Typeclasses in Cats Effect
      |
      |Cats Effect uses typeclasses in many places. Particularly, `IO` is considered asynchonrous because there is an instance of
      |`Async[IO]`. i.e., `Async` is a typeclass.
      |
      |```scala
      |// Effectively, paraphrasing the Cats Effect scaladoc
      |object IO {
      |
      |  // Typeclass showing IO is Async
      |  implicit def asyncForIO: kernel.Async[IO] 
      |  
      |}
      |```
      |
      |Within Cats Effect's "kernel" (like a runtime), `Async` actions are considered an extension of `Sync` actions:
      |
      |```scala
      |// paraphrasing the scaladoc
      |trait Async[F[_]] extends Sync[F]
      |```
      |
      |So, `IO[T]` is considered to be both an *asynchronous* and a *synchronous* IO monad.
      |
      |`SyncIO[T]` is another monad within Cats Effect, which just does synchronous effects.
      |
      |---
      |
      |## Switching synchronous and asynchronous
      |
      |If you're writing a unit test for your code, it can be annoying having to deal with asynchronous effects and test asynchronously.
      |But if you're deploying your code into production, you might have to deal with asynchronous effects and run asynchronously.
      |
      |What if we could write our code so that we test it with a *synchronous* implementation, but deploy it with an *asynchronous* implementation?
      |
      |We could write something a little like this:
      |
      |```scala
      |class MyAPI[F[_] : Sync)() {
      |
      |  def vitallyImportantMethod(): F[Int] = 
      |    // assume we implement this
      |  
      |  def anotherImportantMethod(): F[String] = 
      |    // assume we implement this
      |}
      |```
      |
      |Now we can instantiate our API using asynchronous effects at runtime:
      |```scala
      |val myAPI = MyAPI[IO]() // remembering that Async extends Sync
      |```
      |
      |Or we could instantiate it using only synchronous effects at test time:
      |```scala
      |val myAPI = MyAPI[SyncIO]()
      |```
      |
      |---
      |
      |## Tagless final style
      |
      |A few years ago, a style of programming became popular in the Cats Effect community that takes this to extreme.
      |That, rather than write your API in terms of `IO` or `SyncIO`, you should write it in terms of a monad that uses
      |only the specific features that your code actually uses.
      |
      |So, for instance you might see something like
      |
      |```scala
      |class MyAPI[F[_] : MonadError : Console]() {
      |  
      |  def myFunction():F[Int] = 
      |    // etc  
      |
      |}
      |```
      |
      |Which is essentially just a way of *minimising your code's commitment* to which monad implementation it uses.
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
