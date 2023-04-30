package willscala.actors


import com.wbillingsley.veautiful.doctacular.*
import willscala.Common._
import willscala.given

val akkaClassicDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Akka actors
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |### Akka Classic
      |
      |Akka is a Scala library for actors that used to be open source.
      |
      |In 2022, the company supporting it moved to the [Business Source Licence](https://doc.akka.io/docs/akka/current/project/licenses.html), so versions are
      |
      |* closed source (requiring a commercial licence for production use) for four years
      |* open source after that 
      |
      |The version we're using pre-dates the change, so is still under the Apache 2.0 Licence. 
      |But it makes it a little more awkward teaching the technology, as recent versions aren't free to use.
      |
      |---
      |
      |### Akka "Classic (Untyped) Actors"
      |
      |*Akka* is Scala's actor framework. It's a library, rather than a core part of the language.
      |
      |It has *typed* and *untyped* versions of Actors. The *untyped* version looks similar to Erlang.
      |
      |Defining an Actor:
      |
      |```scala
      |class Hello extends Actor {
      |  def receive = {
      |    case NameMessage(name) =>
      |      println(s"Hello $name")
      |  }
      |}
      |```
      |
      |---
      |
      |### Akka
      |
      |`receive` is defined as a partial function -- that's why we haven't given it an argument in parentheses or a `match`
      |statement
      |
      |Defining an Actor:
      |
      |```scala
      |class Hello extends Actor {
      |  def receive:PartialFunction[Any,Unit] = {
      |    case NameMessage(name) =>
      |      println(s"Hello $name")
      |  }
      |}
      |```
      |
      |
      |
      |---
      |
      |### Akka
      |
      |Creating Actors has a little wrinkle
      |
      |```scala
      |  val system = ActorSystem("PingPongSystem")
      |  val hello = system.actorOf(Props[Hello], name = "hello")
      |```
      |
      |What's `Props`?
      |
      |---
      |
      |### Props
      |
      |* Actors don't have direct references to each other -- the other Actor could be on another machine.
      |
      |* Instead they have an `ActorRef` -- the system will actually handle getting the message to its destination
      |
      |* This also means we *can't directly call the constructor to create the actor* -- Akka has to do it for us
      |
      |* `Props` is the properties (arguments) that need to be passed to the constructor of an actor we want to create
      |
      |---
      |
      |### Props
      |
      |```scala
      |  val system = ActorSystem("PingPongSystem")
      |  val hello = system.actorOf(Props[Hello], name = "hello")
      |```
      |
      |*Create me a Hello actor, called "hello", and its constructor doesn't take any arguments*
      |
      |---
      |
      |## Sending a message
      |
      |If we have an `ActorRef` to the Hello actor sending it a message is very simple:
      |
      |```scala
      |pong.tell(NameMessage("World"), sender)
      |```
      |
      |or, for short:
      |
      |```scala
      |hello ! NameMessage("World")
      |```
      |
      |---
      |
      |## Asking a question
      |
      |* Algernon writes Bertie a letter asking a question
      |* Some time later, Bertie reads the letter. He thinks about it a bit, and then writes a reply
      |* Some time later, Algernon reads the reply
      |
      |We could implement this just using what we have so far, but it's a fairly common *pattern*. 
      |Maybe it could be more concise?
      |
      |---
      |
      |## Asking a question
      |
      |When Algernon sends Bertie the question, *sometime in the future* he'll get a reply
      |
      |```scala
      |val fResponse:Future[Any] = bertie ? QuestionMessage(myQuestion)
      |```
      |
      |---
      |
      |## Concurrency-oriented programming
      |
      |Remember the dilemma we began the unit with -- 
      |
      |> It's hard to think about concurrent threads accessing shared mutable state. What shall we do about that?
      |
      |* (Pure) functional programming: let's not have mutable state
      |
      |* Concurrency-oriented programming: let's not have shared state
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides