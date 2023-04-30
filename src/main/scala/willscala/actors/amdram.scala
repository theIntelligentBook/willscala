package willscala.actors

import com.wbillingsley.veautiful.*
import html.*
import com.wbillingsley.veautiful.doctacular.*
import willscala.Common._
import willscala.given

import com.wbillingsley.amdram.*
import scala.util.Random

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scala.concurrent.*

/*

extension [M] (a:Recipient[M]) {

    inline def ask[T, R](message: Recipient[R] => M)(using ag:ActorContext[T]):Future[R] = {
        val p = Promise[R]
        val receiver = ag.spawnLoop[R] { reply =>
            reply match {
                case r:R => p.success(reply)
                case other:Any => p.failure(IllegalArgumentException(s"Unexpected reply $other"))
            }
            ag.terminate()
        }
        a.send(message(receiver))
        p.future
    }

    inline def ?[T, R](message: Recipient[R] => M)(using ag:ActorContext[T]):Future[R] = ask(message)(using ag)

    inline def ask[T, R](message: Recipient[R] => M)(using troupe:Troupe):Future[R] = {
        val p = Promise[R]
        val receiver = troupe.spawnLoop[R] { reply =>
            reply match {
                case r:R => p.success(reply)
                case other:Any => p.failure(IllegalArgumentException(s"Unexpected reply $other"))
            }
            summon[ActorContext[_]].terminate()
        }
        a.send(message(receiver))
        p.future
    }

    inline def ?[T, R](message: Recipient[R] => M)(using troupe:Troupe):Future[R] = ask(message)(using troupe)

}

*/

// An area for our ping pong actors to write into
val buffer = PushVariable("")((_) => ())
def writeToBuffer(s:String) = 
    buffer.value += (s + "\n")
def resetBuffer():Unit = 
    buffer.value = ""

// So we can show the output
def output(maxHeight:Int) = <.dynamic.pre(^.attr.style := s"max-height: ${maxHeight}px; overflow-y: auto;", buffer.dynamic)    

val troupe = SingleEcTroupe()

val receiver = troupe.spawnLoop { (s) =>
  println(s)
  writeToBuffer(s"Received $s")
}

val receiver2 = troupe.spawnLoop { (s) =>
  println(s)
  writeToBuffer(s"Receiver 2 received $s")

  if s == "exit" then 
    val context = summon[ActorContext[_]]
    context.terminate()
    writeToBuffer(s"Exited")
}

def ping(n:Int):MessageHandler[String] = n match {
    case 0 => MessageHandler { (message, context) =>
        pong ! ("finished", context.self)
        writeToBuffer("ping finished")
        context.terminate()
    }
    case n => MessageHandler { (message, context) =>
        pong ! ("ping", context.self)
        writeToBuffer("ping")
        ping(n - 1)
    }
}

// The pong actor, as in Erlang, receives a message and ping's address
lazy val pong = troupe.spawnLoop[(String, Recipient[String])] { (message, ping) => 
    writeToBuffer(s"pong received $message")
    if message != "finished" then
      ping ! "pong"
}

def pingActor:Recipient[String] = troupe.spawn(ping(3))


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
      |// We need an ExecutionContext to schedule the actors' work loops. 
      |// This one is for Scala.js, but we could use scala.concurrent.ExecutionContext.Implicits.global on the JVM
      |import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
      |
      |// The import with all the Amdram classes
      |import com.wbillingsley.amdram.*
      |
      |// A troupe that will use the same execution context for all its actors
      |val troupe = SingleEcTroupe()
      |```
      |
      |""".stripMargin)
  .veautifulSlide(<.div(
    marked(
      """|## Spawning a simple actor
         |
         |Let's start just by spawning an actor that prints out whatever it receives
         |
         |```scala
         |val receiver = troupe.spawnLoop { (s) =>
         |  writeToBuffer(s"Received $s")
         |}
         |```
         |
         |And have a button send it messages
         |
         |```scala
         |<.button("Send a random string to receiver", 
         |  ^.on.click --> { receiver ! s"A string containing random number ${Random.nextInt(100)}" }
         |),
         |```
         |
         |""".stripMargin
    ),
    <.div(
      <.div(
        <.button("Send a random string to receiver", ^.on.click --> { receiver ! s"A string containing random number ${Random.nextInt(100)}" }),
        <.button("Reset buffer", ^.on.click --> resetBuffer()),
      ),
      output(400),
    )
  ))
  .veautifulSlide(<.div(
    marked(
       """|## Actor Context
          |
          |In our simple example, we didn't do anything that required us to know about the troupe, or terminate, etc.
          |However, if we want to get our context we can *summon* it. 
          |
          |```scala
          |val receiver2 = troupe.spawnLoop { (s) =>
          |  writeToBuffer(s"Receiver 2 received $s")
          |
          |  // This will stop this actor if we send it the word "exit"
          |  if s == "exit" then 
          |    val context = summon[ActorContext[_]]
          |    context.terminate()
          |    writeToBuffer(s"Exited")
          |}
          |```
          |
          |""".stripMargin
    ),
    <.div(
      <.div(
        <.button("Send a random string to receiver2", ^.on.click --> { receiver2 ! s"A string containing random number ${Random.nextInt(100)}" }),
        <.button("Tell receiver2 to exit", ^.on.click --> { receiver2 ! "exit" }),
        <.button("Reset buffer", ^.on.click --> resetBuffer()),
      ),
      output(500),
    )
  ))
  .markdownSlides(
    """|## Let's do ping pong
       |
       |Let's do the [Ping pong](https://www.erlang.org/doc/getting_started/conc_prog.html) example from Erlang.
       |Although there are a few differences in how I've implemented it.      
       |
       |We're going to need two actors:
       |
       |* Pong just loops, sending ping a "pong" every time it receives a message
       |* Ping is a finite state machine. It starts as ping(3), sending back a pong. But it counts down... after 3 pings it exits
       |
       |---
       |
       |### MessageHandler
       |
       |In Amdram, there's a `MessageHandler[T]` trait defined to let us return a different handler for the *next* message. It's defined like this:
       |
       |```scala
       |trait MessageHandler[T] {
       |    def receive(message:T)(using ac: ActorContext[T]): MessageHandler[T] | Unit
       |}
       |```
       |
       |* If we finish with `Unit`, the actor will keep going with the same message handler
       |* If we finish with a different `MessageHandler[T]`, the actor will use that instead
       |
       |Note that is a little different from the Erlang example, where it was *always* returning the next handler to use.
       |
       |There are also a couple of apply methods in the `MessageHandler` companion objects so that we can write our code in a slightly more Erlang-like way
       |
       |---
       |
       |### Implementing Ping
       |
       |This is how I've implemented ping:
       |
       |```scala
       |def ping(n:Int):MessageHandler[String] = n match {
       |    case 0 => MessageHandler { (message, context) =>
       |        pong ! ("finished", context.self)
       |        writeToBuffer("ping finished")
       |        context.terminate()
       |    }
       |    case n => MessageHandler { (message, context) =>
       |        pong ! ("ping", context.self)
       |        writeToBuffer("ping")
       |        ping(n - 1)
       |    }
       |}
       |
       |// I've done this as a `def` just so we can launch a new one when we click a button
       |// You might want a lazy val
       |def pingActor:Recipient[String] = troupe.spawn(ping(3))
       |```
       |
       |Notice that `ping` is sending `pong` a tuple: the message and a reference to itself (for the reply)
       |
       |""".stripMargin
  )
  .veautifulSlide(<.div(
    marked(
      """|## Defining pong
         |
         |Pong doesn't change its behaviour. However, it does demonstrate *receiving a reference to the sender*.
         |
         |Rather than just receive a `String`, poing is defined as receiving a `(String, Recipient[String])`, where the second entry in the tuple is the sender.
         |
         |```scala
         |// The pong actor, as in Erlang, receives a message and ping's address
         |lazy val pong = troupe.spawnLoop[(String, Recipient[String])] { (message, ping) => 
         |    writeToBuffer(s"pong received $message")
         |    if message != "finished" then
         |      ping ! "pong"
         |}
         |```
         |
         |As we never shut this actor down, I've just put it in a `lazy val`
         |
         |""".stripMargin
    ),
  ))
  .veautifulSlide(<.div(
    marked(
      """|## Let's try it out
         |
         |The widgets on this slide should launch our ping-pong:
         |
         |* The "Launch ping" button spawns a new ping actor, and sends it a first message to get it started
         |
         |  ```scala
         |  <.button("Launch ping(3)", ^.on.click --> { troupe.spawn(ping(3)) ! "start" }),
         |  ```
         |
         |* Because `pong` is a `lazy val`, it'll get launched the first time `ping` tries to send it a message
         |
         |* `pong` will stick around, but we should be able to launch new `ping`s by hitting the button again
         |
         |""".stripMargin
    ),
    <.div(
      <.div(
        <.button("Launch ping(3)", ^.on.click --> { troupe.spawn(ping(3)) ! "start" }),
        <.button("Reset buffer", ^.on.click --> resetBuffer()),
      ),
      output(300),      
    )
  ))
  .veautifulSlide(<.div(
    marked(
      """|## Asking an actor
         |
         |Suppose you want to ask an actor a question and get the answer as a `Future[Reply]`
         |A common way to do this is to:
         |
         |1. Create an empty `Promise` for the reply
         |2. Create a temporary actor to receive the reply
         |3. Fill the `Promise` from inside the temporary actor
         |
         |```scala
         |<.button("Ask pong", ^.on.click --> { 
         |  val promise = Promise[String]
         |  val replyActor = troupe.spawnLoop[String] { (msg) => 
         |    promise.success(msg)
         |    summon[ActorContext[_]].terminate()            
         |  }
         |
         |  pong ! ("Hello?", replyActor)
         |
         |  promise.future.foreach(writeToBuffer) 
         |}),
         |```
         |
         |""".stripMargin
    ),
    <.div(
      <.div(
        <.button("Ask pong", ^.on.click --> { 
          val promise = Promise[String]
          val replyActor = troupe.spawnLoop[String] { (msg) => 
            promise.success(msg)
            summon[ActorContext[_]].terminate()            
          }

          pong ! ("Hello?", replyActor)

          promise.future.foreach(writeToBuffer) 
        }),
        <.button("Reset buffer", ^.on.click --> resetBuffer()),
      ),
      output(100),      
    )
  ))
  .veautifulSlide(<.div(
    marked(
      """|## Asking an actor, more succinctly
         |
         |To help make that a little more succinct, there's a MessageHandler `OneTime[R]` that can do the promise part for us
         |
         |```scala
         |<.button("Ask pong", ^.on.click --> { 
         |  val oneTime = OneTime[String]
         |  val replyActor = troupe.spawn(oneTime)
         |
         |  pong ! ("Hello again?", replyActor)
         |
         |  oneTime.future.foreach(writeToBuffer) 
         |}),
         |```
         |
         |""".stripMargin
    ),
    <.div(
      <.div(
        <.button("Ask pong", ^.on.click --> { 
          val oneTime = OneTime[String]
          val replyActor = troupe.spawn(oneTime)

          pong ! ("Hello again?", replyActor)

          oneTime.future.foreach(writeToBuffer) 
        }),
        <.button("Reset buffer", ^.on.click --> resetBuffer()),
      ),
      output(100),      
    )
  ))
  .veautifulSlide(<.div(
    marked(
      """|## The ask pattern
         |
         |This is common enough that there's an extension method, `ask`, for it.
         |
         |`ask` takes a method because it needs to give you the reply actor, so that you can wrap it up in the massage.
         |(Otherwise how would the destination know where to send the reply to?)
         |
         |```scala
         |<.button("Ask pong", ^.on.click --> { 
         |  given Troupe = troupe
         |  val reply:Future[String] = pong.ask((replyActor) => ("Hello at last!", replyActor))
         |
         |  reply.foreach(writeToBuffer) 
         |}),
         |```
         |
         |We add the type annotation to the reply
         |
         |```scala
         |val reply:Future[String] = // ...
         |```
         |
         |so that the compiler's type inference can work out the type of the message on the right hand side.
         |
         |
         |""".stripMargin
    ),
    <.div(
      <.div(
        <.button("Ask pong", ^.on.click --> { 
          given Troupe = troupe

          val reply:Future[String] = pong.ask((replyActor) => ("Hello at last!", replyActor))

          reply.foreach(writeToBuffer) 
        }),
        <.button("Reset buffer", ^.on.click --> resetBuffer()),
      ),
      output(200),      
    )
  ))
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides