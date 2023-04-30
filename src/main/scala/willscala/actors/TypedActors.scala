package willscala.actors

import com.wbillingsley.veautiful.doctacular.*
import com.wbillingsley.veautiful.html.<
import willscala.Common._
import willscala.MermaidDiagram
import willscala.given

val typedActors = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Typed Actors and Behaviours
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |## The trouble with Akka Classic Actors
      |
      |Classic (untyped) Actors have two aspects that functional programmers wouldn't be keen on:
      |
      |1. They're untyped. There's no way of knowing what messages an actor can receive
      |
      |2. They're mutable, whereas functional programmers prefer working with immutable data types
      |
      |*Typed Actors* try to fix both these problems.
      |
      |1. Actors are typed, which means the compiler can check you are sending a valid message
      |
      |2. The changes in state of an actor are described in a more functional way, using a succession of immutable states
      |
      |""".stripMargin)
  .veautifulSlide(<.div(
    marked("""
      |## Behaviors
      |
      |Imagine our Actor like it's a state machine. Here's an imagimary guard in a game: 
      |
    """.stripMargin),
    MermaidDiagram("""
      |stateDiagram-v2
      | %%{init: {'theme': 'forest', 'themeVariables': { 'fontSize': '26px' }}}%%
      | direction LR
      | [*] --> Patrolling
      | Patrolling --> Searching : Noise
      | Searching --> Patrolling : TimeOut
      | Searching --> Chasing : SeesPlayer
      | Chasing --> Searching : LostPlayer
      |""".stripMargin),
    marked("""
      |
      |When our Actor is in a particular state, it is listening for particular messages. 
      |
      |When it receives a message, it'll take some action and transition to a new state. 
      |
      |With typed actors, a `Behavior` describes one of those states: listening for particular messages.
      |
      |When it receives a message, it'll take some action and return a new `Behavior` that should be used when listening 
      |for the next message.
    """.stripMargin),
  ))
  .markdownSlidex(
    """
    |## Behaviors are typed
    |
    |Unlike classic actors, typed actors declare what kind of message they receive.
    | 
    |Let's define a `Logger` behaviour that'll accept `String` messages and `println` anything it receives: 
    |
    |```scala
    |def loggerBehavior: Behavior[String] = Behaviors.receive { (context, message) =>
    |  println(message)
    |  Behaviors.same
    |}
    |```
    |
    |""".stripMargin
  )
.markdownSlidex(
    """
    |## Replies are also typed
    |
    |With classic actors, you could just ask Akka for who sent you the message, and send them a reply.
    | 
    |With typed actors, this becomes a little more complex because we need to know what type of message the sender
    |can receive. Otherwise we'll get a type error trying to send them a reply.
    |
    |To solve this, we include the sender in the message. Think of it like writing your own address on the back of 
    |an envelope you send.
    |
    |Here's the Akka documentation's version of a Greeter (slightly adapted)
    |
    |```scala
    |case class Greet(whom:String, replyTo:ActorRef[Greeted])
    |case class Greeted(whom:String, from:ActorRef[Greet])
    |
    |def greeter:Behavior[Greet] = Behaviors.receive { (context, message) => 
    |  println("Hello " + message.whom)
    |  message.replyTo ! Greeted(message.whom, context.self)
    |  Behaviors.same
    |}
    |```
    |
    |---
    |
    |## Keeping data
    |
    |So far, we've just returned `Behaviors.same` to say to use the same behavior for the next message.
    |
    |If we have some data, though, we could keep this as an argument to our function. Let's change
    |our greeter so it remembers how many greetings its handed out:
    |
    |```scala
    |def greeter(count:Int):Behavior[Greet] = Behaviors.receive { (context, message) => 
    |  println(s"Greeting $count: Hello ${message.whom}")
    |  message.replyTo ! Greeted(message.whom, context.self)
    |  greeter(count + 1)
    |}
    |```
    |
    |By returning `greeter(count + 1) returned a new `Behavior` to represent our next state, including a change to its
    |data. There are no mutable variables in this description though - we've just described our Actor's state
    |transition functionally.
    |
    |---
    |
    |## Spawning the actors
    |
    |To spawn an actor, we no longer need to worry about `Props`. We just need to spawn a behaviour
    |
    |```scala
    |Behaviors.setup { context => 
    |  val greetActor:ActorRef[Greet] = context.spawn(greeter(0), "greeter")
    |
    |  // The actor's created. What woud we like to send it... 
    |}
    |``` 
    |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
