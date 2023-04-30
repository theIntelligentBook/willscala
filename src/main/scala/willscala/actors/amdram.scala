package willscala.actors


import com.wbillingsley.veautiful.doctacular.*
import willscala.Common._
import willscala.given

val amdramDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Amdram
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |### Amdram
      |
      |Amdram is a little open source library for Actors that I wrote - and have imported into this site.
      |
      |It's intended to be small. Not too many bells and whistles, but also not very much overhead so it can be used in toy projects (and assignments)
      |without taking too long to learn.
      |
      |---
      |
      |### Recipients
      |
      |A `Recipient[-T]` is something you can send a message to.
      |
      |Most actor frameworks don't let you directly access the actor object - it might be on another machine. Instead, you're given some kind of
      |reference to them.
      |
      |In Amdram, there is a little trait `Recipient` that is defined like this:
      |
      |```scala
      |trait Recipient[-T] {
      |    def send(message:T):Unit           
      |
      |    def tell(message:T):Unit = send(message)
      |
      |    def !(message:T):Unit = send(message)
      |}
      |```
      |
      |e.g.
      |
      |```scala
      |destination ! "This is my message" // Assuming you have a Recipient[String]
      |```
      |
      |---
      |
      |### Troupes
      |
      |In Amdram, a system of actors is punningly called a `Troupe`. The troupe is what our actors join, and what's going to spawn our actors.
      |
      |```scala
      |import scala.concurrent.ExecutionContext.Implicits.global
      |
      |val 
      |```
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides