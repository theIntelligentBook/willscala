package willscala.asyncstreams

import com.wbillingsley.veautiful.doctacular._
import willscala.Common._
import willscala.given

val asyncIteratorsDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Async Iterators
      |
      |(JS has these, so let's explain them)
      |
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |
      |## Iterators
      |
      |Most imperative programmers are familiar with the idea of iterators
      |
      |```scala
      |val list = List(1, 2, 3)
      |val iterator = list.iterator
      |
      |while iterator.hasNext do
      |  // Iterators are imperative
      |  val item = iterator.next()
      |  println(item)
      |````
      |
      |---
      |
      |### Let's put that into a structure
      |
      |First, let's change this so that we have a sort of immutable iterator
      |
      |```scala
      |enum IteratorResult[+T, S, +R]:
      |  case Yield(item:T, state: S, next: () => IteratorResult[T, R])
      |  case Done(result:R)
      |```
      |
      |and lets make them available on Lists
      |
      |```scala
      |extension [T] (l:List[T]) {
      |  def immutableIterator(state:Int = 0):IteratorResult[T, Int, Int] = l match {
      |    case Nil => Done(state)
      |    case h :: t => 
      |      Yield(h, state + 1, () => t.immutableIterator(state  + 1))
      |  }
      |}
      |```
      |
      |Here, state is just holding the count of items iterated over
      |
      |---
      |
      |### foreach
      |
      |We can define foreach on our immutable iterator
      |
      |```scala
      |enum IteratorResult[+T, S, +R]:
      |  case Yield(item:T, state: S, next: () => IteratorResult[T, R])
      |  case Done(result:R) 
      |  
      |  @tailrec
      |  final def foreach(f: T => Unit):Unit = this match {
      |    case Yield(item, _, next) => 
      |      f(item)
      |      next().foreach(f)
      |    case Done(r) => 
      |      ()
      |  }
      |```
      |
      |and it'll work in a `for ... do`
      |
      |```scala
      |for item <- List(1, 2, 3).immutableIterator() do println(item)
      |```
      |
      |---
      |
      |### Map
      |
      |We could also define `map`, for example like this
      |
      |```scala
      |  final def map[B](f: T => B):IteratorResult[B, S, R] = this match {
      |    case Yield(item, s, next) => 
      |      Yield(f(item), s, () => next().map(f))
      |    case Done(r) => 
      |      Done(r)
      |  }
      |```
      |
      |
      |---
      |
      |### Async
      |
      |Because the iterator is immutable and holds its state, it's not too hard to make this asynchronous
      |
      |```scala
      |enum AsyncIterator {
      |  case Yield(item:T, state: S, next: () => Future[AsyncIterator[T, R]])
      |  case Done(result:R)
      |}
      |```
      |
      |Something like this is the basis of JavaScript's async iterables that let you write
      |
      |```js
      |for await (variable of iterable) {
      |  // do something
      |}
      |```
      |
      |In that case, using JavaScript's convention of being able to `await` on a promise
      |
      |---
      |
      |### Lots of microtasks....
      |
      |One potential snag with this is in performance. Let's have a go at defining map for an async iterator
      |
      |```scala
      |  final def map[B](f: T => B)(using ec:ExecutionContext):AsyncIterator[B, S, R] = this match {
      |    case Yield(item, s, next) => 
      |      Yield(f(item), s, () => next().map(_.map(f)))
      |    case Done(r) => 
      |      Done(r)
      |  }
      |```
      |
      |Notice the nested map and the execution context. Because `next()` is returning `Future[T]`, we can't immediately transform the result, but have to
      |*schedule a task* to transform the result at some point in the future.
      |
      |In the example below, each map produces a new task to schedule
      |
      |```scala
      |asyncIterator.map(f).map(g).map(h)
      |```
      |
      |In some streaming frameworks, we'd like to consider *fusion* - how we can eliminate or reduce intermediate values or tasks in how we process the stream.
      |
      |---
      |
      |### Resource management
      |
      |This is a contrived hypothetical example, to try to explain another consideration. 
      |
      |```scala
      |val iterator:AsyncIterator[String] = file.linesIterator()
      |```
      |
      |And (here's the contrived part) suppose the files iterator was *lazy* and trying only to read lines in as we read them.
      |
      |```scala
      |iterator.next() // actually reads the next line of the file
      |```
      |
      |How would we know when to close the file? How does a derived iterator, e.g. `iterator.map(f)` indicate that it didn't want to
      |read the whole stream?
      |
      |For some kinds of stream, we'll also need to consider how we add resource management. 
      |
      |
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
