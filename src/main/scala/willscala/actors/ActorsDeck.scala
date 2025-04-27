package willscala.actors

import com.wbillingsley.veautiful.html.*
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
      |* No avoidable penalty for distribution -- *doesn't matter if the target of your message is on another machine*
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
      |## Concurrent Erlang 
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
      |""".stripMargin)
    .veautifulSlide(<.div(
      marked(
        """|## Actors can be like state machines
           |
           |This is an example from the [Erlang docs](https://www.erlang.org/doc/getting_started/conc_prog.html). 
           |
           |* `pong` does the same thing every time, but
           |* `ping` replaces itself with `ping(n - 1, pong)`, acting like a state machine that counts down to zero
           |
           |""".stripMargin
      ),
      <.div(^.attr.style := "overflow-y: scroll; height: 640px;",
        marked(
          """|
             |```erlang
             |-module(tut15).
             |
             |-export([start/0, ping/2, pong/0]).
             |
             |ping(0, Pong_PID) ->
             |    Pong_PID ! finished,
             |    io:format("ping finished~n", []);
             |
             |ping(N, Pong_PID) ->
             |    Pong_PID ! {ping, self()},
             |    receive
             |        pong ->
             |            io:format("Ping received pong~n", [])
             |    end,
             |    ping(N - 1, Pong_PID).
             |
             |pong() ->
             |    receive
             |        finished ->
             |            io:format("Pong finished~n", []);
             |        {ping, Ping_PID} ->
             |            io:format("Pong received ping~n", []),
             |            Ping_PID ! pong,
             |            pong()
             |    end.
             |
             |start() ->
             |    Pong_PID = spawn(tut15, pong, []),
             |    spawn(tut15, ping, [3, Pong_PID]).
             |
             |```
          """.stripMargin
        )      
      )      
    )
  )
  .markdownSlides(
   """|
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
      |## Scala actor libraries
      |
      |There are a few options for Actors in Scala:
      |
      |* [Akka](https://akka.io) is a popular library for Actors in Scala and is used by some very large companies. 
      |  However, as of 2022, it's not completely open source. It's also very big, so we'll save it for its own slide decks and tutorials.
      |
      |* [Vert.x](https://vertx.io/) implements the "multi-reactor" pattern on the JVM. It's essentially actors.
      |
      |* [Amdram](https://www.wbillingsley.com/amdram) is a little open source one that I've written to try to explain the basics of Actors.
      |  It doesn't have all the bells and whistles of larger libraries, but is hopefully small enough to use in toy applications (and assignments)
      |  without imposing too much learning overhead
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
