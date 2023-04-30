package willscala.actors

import com.wbillingsley.veautiful.doctacular.*
import willscala.Common._
import willscala.given

val actorsDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Concurreny Oriented Programming
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |## Futures
      |
      |When we were talking about `Future[T]`, we talked about *asynchronous* computing. We sent a web request to another machine, and at some point in the future, it would give us a result.
      |
      |```scala 
      |wsClient
      |  .url("https://api.github.com/zen")
      |  .get()
      |  .map(_.body)  // returns a Future[String]
      |```
      |
      |For a moment, let's just think about *both* computers as part of one system: the one sending the message and the one receiving and responding to it -- what does that look like?
      |
      |---
      |
      |## If we think about both computers 
      |
      |* We have two machines, with two processes. They share no data whatsoever.
      |
      |* One process (our client) sends a message (a web request) to the other process (GitHub's server)
      |
      |* The other machine is waiting to receive and respond to messages. When it receives the message, it works out a response to 
      |  send back.
      |
      |What if we did that, but the processes just happened to be on the same computer, in the same program?
      |
      |---
      |
      |## Concurrency-oriented programming
      |
      |Popularised by Erlang, co-created by Joe Armstrong
      |
      |> In 1998 Ericsson announced the AXD301 switch, containing over a million lines of Erlang and reported to achieve a high availability of nine "9"s
      |
      |The description we'll use is extracted from Joe Armstrong's slides: https://www.slideshare.net/vishnu/concurrency-oriented-programming-in-erlang
      |
      |and from his PhD: http://erlang.org/download/armstrong_thesis_2003.pdf
      |
      |---
      |
      |## Concurrency-oriented programming
      |
      |1. Processes are totally independent -- *imagine they run on different machines*
      |
      |2. Each process has an unforgettable name. If you know a process's name, you can send it a message
      |
      |3. Messages are "send and pray" -- you send a message and pray it gets there
      |
      |4. You can monitor a remote process.  
      |   (Error handling is non-local. Processes do what they are supposed to do or fail.)
      |
      |---
      |
      |## Concurrency-oriented programming language
      |
      |* No penalty for massive parallelism -- *it has to be really cheap to spawn new processes*
      |
      |* No unavoidable penalty for distribution -- *doesn't matter if the target of your message is on another machine*
      |
      |* Concurrent behaviour of program same on all OSs
      |
      |* Can deal with failure
      |
      |---
      |
      |## "Why is COP nice"
      |
      |* The world is parallel
      |
      |* The world is distributed
      |
      |* Things fail
      |
      |* Our brains intuitively understand parallelism
      |
      |* Automatic scalability. Automatic fault-tolerance
      |
      |---
      |
      |## Concurrent Erlang in 3 examples
      |
      |Spawning a process:
      |
      |```erlang
      |Pid = spawn(fun() -> loop(0) end)
      |```
      |
      |Sending a message:
      |
      |```erlang
      |Pid ! Message,
      |```
      |
      |Receiving a message:
      |
      |```erlang
      |receive
      |  Message1 -> 
      |    Actions1;
      |  Message2 -> 
      |    Actions2;
      |```
      |
      |---
      |
      |## Actors
      |
      |Turns out, that kind of thing already had a name: the *Actor* model.
      |
      |> *A Universal Modular ACTOR Formalism for Artificial Intelligence*  
      |  -- Carl Hewitt, Peter Bishop, and Richard Steiger (1973)
      |
      |---
      |
      |## Actors (in their modern form)
      |
      |* An *Actor* has an inbox -- you can post it *messages*
      |
      |* It responds to *one message at a time*
      |
      |* Depending on what the message is, it acts on it. It can:
      |
      |  * Send messages
      |
      |  * Create new actors
      |
      |  * Update mutable state
      |
      |* When it's done, it goes back to its mailbox to see if it's received any messages. It takes the next message from the queue.
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
