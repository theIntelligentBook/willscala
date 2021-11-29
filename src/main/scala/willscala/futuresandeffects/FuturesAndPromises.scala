package willscala.futuresandeffects

import com.wbillingsley.veautiful.templates._
import willscala.Common._
import willscala.given

val futuresDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Futures and Promises
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """## A recap (of sorts)
      |
      |**Referential transparency** means a pure function call can always be replaced by its result without changing
      |the outcome of the program.
      |
      |However, sometimes we'll have a task where we don't want our code to *block* while waiting for a result.
      |For example:
      |
      |* Long-running task in a UI framework
      |
      |* Network call to another computer
      |
      |* Waiting for user input
      |
      |---
      |
      |## Threads and thread-blocking in Java
      |
      |In Java, you might have seen code like this, to start code running in another *thread* of execution:
      |
      |```java
      |new Thread(() -> {
      |    // Do something
      |}).start();
      |```
      |
      |Most UI toolkits only have one UI thread.
      |If you do something long-running in an event-handler, it blocks the thread and freezes the UI, so instead
      |you might want to put the long-running task on a worker thread.
      |
      |---
      |
      |## A simple app
      |
      |```java
      |public class Main extends Application {
      |    volatile double h = 0;
      |    volatile double rot = 0;
      |
      |    @Override
      |    public void start(Stage primaryStage) throws Exception{
      |        Rectangle r = new Rectangle(200,200);
      |        Button b = new Button("Long task!");
      |        VBox vBox = new VBox(r, b);
      |        Scene scene = new Scene(vBox);
      |        primaryStage.setScene(scene);
      |        primaryStage.show();
      |
      |    public static void main(String[] args) {
      |        launch(args);
      |    }
      |}
      |```
      |
      |This shows a black rectangle, sitting above a button
      |
      |---
      |
      |## Let's make it spin the rectangle
      |
      |```java
      |TimerTask timerTask = new TimerTask() {
      |    @Override
      |    public void run() {
      |        h = (h + 1) % 255;
      |        rot = (rot + 3) % 360;
      |
      |        Platform.runLater(() -> {
      |            r.setFill(Color.hsb(h, 1, 1));
      |            r.setRotate(rot);
      |        });
      |    }
      |};
      |Timer t = new Timer();
      |t.schedule(timerTask, 1000/60, 1000/60);
      |```
      |
      |---
      |
      |## Now let's do something long when you press the button
      |
      |```java
      |b.setOnAction((evt) -> {
      |    try {
      |        Thread.sleep(10000);
      |    } catch (Exception ex) {
      |        //
      |    }
      |});
      |```
      |
      |When we press the button, the rectangle stops spinning
      |
      |---
      |
      |## What's happening...
      |
      |Our timer is still working (we can put in a println to check), because it is running on a different thread
      |
      |But the *UI thread* is busy (sleeping) for 10 seconds, so can't repaint the screen or react to any more button presses
      |
      |---
      |
      |## Let's fix it
      |
      |```java
      |b.setOnAction((evt) -> {
      |    new Thread(() -> {
      |      try {
      |        Thread.sleep(10000);
      |      } catch (Exception ex) {
      |        //
      |      }
      |    }).start();
      |});
      |```
      |
      |Now the "long running task" happens on a different thread (not the UI thread) and doesn't block the UI
      |
      |---
      |
      |## Communicating between threads
      |
      |Let's take another look at this code...
      |
      |```java
      |TimerTask timerTask = new TimerTask() {
      |    @Override
      |    public void run() {
      |        h = (h + 1) % 255;
      |        rot = (rot + 3) % 360;
      |
      |        Platform.runLater(() -> {
      |            r.setFill(Color.hsb(h, 1, 1));
      |            r.setRotate(rot);
      |        });
      |    }
      |};
      |Timer t = new Timer();
      |t.schedule(timerTask, 1000/60, 1000/60);
      |```
      |
      |
      |---
      |
      |## Communicating between threads
      |
      |In our Java code, the TimerTask runs on its own thread.
      |But the instructions to update the rectangle have to be put *back on the UI thread* because they modify the UI state.
      |
      |```java
      |Platform.runLater(() -> {
      |    r.setFill(Color.hsb(h, 1, 1));
      |    r.setRotate(rot);
      |});
      |```
      |
      |This is possible because the UI thread has a queue of tasks, and goes back to check it regularly.
      |
      |ie, this communication isn't built into Threads, it's just that the code that is *running on the UI thread* was
      |written to look in a queue for tasks to do.
      |
      |(Platform.runLater is a method from the javafx packages)
      |
      |---
      |
      |## An observation
      |
      |We've talked about concurrency in Java, but it's all been closely tied to *how concurrency is implemented*.
      |
      |It would be nice to be able to talk just about the tasks. And to be able to compose them as easily as we can compose functions.
      |
      |""".stripMargin)
  .markdownSlidex("""
      |## Futures
      |
      |`Future`s let us talk about *a computation that will complete at some point in the future*.
      |
      |* Suppose we have a box   `[   ]`
      |
      |* It will receive a value  `[ 7 ]`
      |
      |* But I don't know when  `[ ? ]`
      |
      |I would like to say *"When the box has a value, print it out"*
      |
      |---
      |
      |## Futures
      |
      |```scala
      |val myFuture:Future[Int] = longRunningTask()
      |myFuture.onSuccess({ case x => println(x) })
      |```
      |
      |---
      |
      |## Promises
      |
      |How do we fill the box? For now, let's use a `Promise`
      |
      |```scala
      |val promise = Promise[Int]
      |val future = promise.future
      |
      |// Do something when the promise succeeds
      |promise.onSuccess((value) => println(value))
      |
      |// later
      |promise.success(1)
      |```
      |
      |"I *promise* at some point in the future I will put an `Int` into this box"
      |
      |---
      |
      |## Let's do a network request...
      |
      |```scala
      |  // A little boilerplate - this is needed by the web client we're using
      |  given actorSystem:ActorSystem = ActorSystem()
      |  given materializer:Materializer = SystemMaterializer(actorSystem).materializer
      |
      |  // Now we can create our web client and make a request
      |  val wsClient = StandaloneAhcWSClient()
      |  val f = wsClient
      |    .url("https://api.github.com/zen")
      |    .get()
      |
      |  // When it completes successfully, print the body
      |  f.foreach({ req => println(req.body) })
      |
      |  // Print this statement immediately
      |  println("This prints immediately")
      |```
      |
      |It returns the Future ***immediately***. The future completes ***eventually*** (when the HTTP request succeeds).
      |
      |So, this will print "This prints immediately" before the request for a zen saying from GitHub has returned.
      |---
      |
      |## Map
      |
      |We have a computation that will complete in the future. Maybe we want to do something with it.
      |
      |We can compose a function onto our future using `map`.
      |
      |```scala
      |val wsClient = StandaloneAhcWSClient()
      |val f = wsClient
      |  .url("https://api.github.com/zen")
      |  .map(_.body).map(_.toUpperCase)
      |
      |f.foreach((req) => println(req.body))
      |println("This prints immediately")
      |```
      |
      |*A computation that finishes in the future, with the result then made uppercase,
      |is also a computation that finishes in the future*
      |
      |---
      |
      |## FlatMap
      |
      |Suppose the next bit of processing is also asynchronous.
      |
      |*A computation that finishes in the future, with the result then processed through a computation that finishes in the future, is also a computation that finishes in the future*
      |
      |We can compose these using `flatMap`. Let's put our "zen" fetcher into a function
      |
      |```scala
      |def zen()(using ec:ExecutionContext):Future[String] =
      |  val randomStr = Random.nextString(4) // Makes sure we don't get a cached reply
      |  wsClient.url(s"https://api.github.com/zen?$randomStr").get().map(_.body)
      |```
      |
      |...and let's do two of them
      |
      |```scala
      |val future = zen().flatMap((first) =>
      |  zen().map((second) =>
      |    s"$first, and then $second"
      |  )
      |)
      |
      |future.foreach(println)
      |```
      |
      |*Ok, but that's ugly and unreadable*
      |
      |---
      |
      |## Same thing, using for notation
      |
      |```scala
      |val future = for
      |  first <- zen()
      |  second <- zen()
      |yield s"$first, and then $second"
      |
      |future.foreach(println)
      |```
      |
      |... or even...
      |
      |```scala
      |for
      |  first <- zen()
      |  second <- zen()
      |do
      |  println(s"$first, and then $second")
      |```
      |
      |*I think that's not bad. We've made two HTTP requests, and done something with the results, asynchronously,
      |in 5 lines of code, and it's pretty readable*
      |
      |---
      |
      |## Futures are monoid-like but not pure
      |
      |This "feels" like it's using pure code
      |
      |```scala
      |longRunningCalculation().map(_ * 2)
      |```
      |
      |But the Future *changes state* when it completes (or fails). There is a mutation going on.
      |
      |---
      |
      |## Futures have state
      |
      |```scala
      |val p = Promise[Int]
      |val future = p.future
      |
      |println(s"Has it finished? ${future.isComplete}")
      |p.success(1)
      |println(s"Has it finished? ${future.isComplete}")
      |```
      |
      |That's clearly order-dependent.
      |
      |---
      |
      |## Futures are not referentially transparent
      |
      |**Referential transparency** means an expression can be replaced by its result without changing
      |the outcome of the program.
      |
      |```scala
      |def zen()(using ec:ExecutionContext):Future[String] =
      |  val randomStr = Random.nextString(4) // Makes sure we don't get a cached reply
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
      |## Parallel vs Sequenced
      |
      |Because futures are monoid-like but not referentially transparent, there is a subtle way in which we can
      |alter whether a network call happens sequentially or in parallel.
      |
      |These are conducted in sequence. `a` must complete successfully before `b` is called:
      |
      |```scala
      |for
      |  a <- zen()
      |  b <- zen()
      |do println(s"$a then $b")
      |```
      |
      |These requests may be conducted in parallel:
      |
      |```scala
      |for
      |  af = zen()
      |  bf = zen()
      |  a <- af
      |  b <- bf
      |do println(s"$a then $b")
      |```
      |
      |---
      |
      |## ExecutionContext
      |
      |When you call `map` or `flatMap` on a `Future`, you need an implicit `ExecutionContext`. Why?
      |
      |* A `Future` is a computation. It doesn't say what thread to run it on.
      |* But it runs on the JVM. Eventually, it needs to run on a thread
      |* The ExecutionContext sorts out what thread to run it on.
      |
      |---
      |
      |## Errors
      |
      |In Java, you may used to working with Exceptions. These exist in Scala too.
      |When these occur, they propagate up the thread's call stack until they are caught.
      |
      |But when working with `Future`s, the code that really started this computation may be *on a different thread*
      |with a *different call stack*.
      |
      |Instead, failure is a possible state of a `Future`
      |
      |---
      |
      |## Error handling
      |
      |In an event-handling style (returns `Unit`):
      |
      |```scala
      |f.onFailure {
      |  case NotFoundException(ex) => /* ... */
      |}
      |```
      |
      |In a composable computation style (returns a `Future`):
      |
      |```scala
      |f.recoverWith {
      |  case NotFoundException(ex) => Future.successful(...)
      |}
      |```
      |""".stripMargin)
  .markdownSlidex("""
      |## Errors and sequencing
      |
      |In this code, `a` must complete successfully before request `b` is called
      |
      |```scala
      |val result = for
      |  a <- zen()
      |  b <- zen()
      |yield (s"$a then $b")
      |```
      |
      |If `a` fails, the second network call will never be made.
      |
      |---
      |
      |## Why it has to stop at the first failure
      |
      |Recall that for-comprehensions are combined with `flatMap`, so our code is effectively
      |
      |```scala
      |zen().flatMap((a) =>
      |  zen().map((b) => s"$a then $b")
      |)
      |```
      |
      |We have given `zen().flatMap` a lambda function to call on success, `(a) => ...`, where `a` is expected to be a `String`
      |
      |If the outer `zen()` call *fails*, however, it doesn't have a string `a` to pass into our function.
      |
      |So we know it *can't* call the function we gave it.
      |
      |So the inner `zen()` call contained within our function *can't* be made.
      |
      |Instead, it returns a failed Future with the error it already had.
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
