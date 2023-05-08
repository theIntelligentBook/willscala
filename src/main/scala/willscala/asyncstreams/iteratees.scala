package willscala.asyncstreams

import com.wbillingsley.veautiful.doctacular._
import willscala.Common._
import willscala.given

val iterateesDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Iteratees
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |## Fold on a list
      |
      |Let's start explaining iteratees from the idea of a fold over a list.
      |
      |```scala
      |List(1, 2, 3).foldLeft(initialState) { (state, element) => state }
      |```
      |
      |Suppose instead of passing in an initial state, we wrapped the state and the function up into an object. E.g.
      |
      |
      |---
      |
      |### Something that gets iterated on
      |
      |```scala
      |trait SyncIteratee[-T]:
      |  def accept(element:T):IteratedOn[T]
      |
      |extension [A] (iterable:IterableOnce[A]):
      |
      |  def foldOver[B](iteratee:SyncIteratee[B]) 
      |```
      |
      |...
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides



  /*

    enum Machine:
        case Emit(out)
        case Await((in) => Machine)
        case Stop(result)

  */