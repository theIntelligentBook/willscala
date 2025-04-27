package willscala.actors

import com.wbillingsley.veautiful.doctacular.*
import com.wbillingsley.veautiful.html.<
import willscala.Common._
import willscala.MermaidDiagram
import willscala.given

val pekkoHttp = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Pekko-HTTP (and Akka-HTTP)
      |""".stripMargin).withClass("center middle")
  .markdownSlides(
    """
      |## Similar to structures in other languages
      |
      |Although Akka/Pekko is *Scala's* actor system, it's not the only language to have something similar
      |
      |* Node.js is also based on an event loop - except that it only has a single thread
      |
      |* Vert.x is a "multi-reactor" - lots of event loops, designed for Java
      |
      |A place where we often want to respond to incoming messages is in web application servers, so each of these
      |has one (or more) HTTP frameworks.
      |
      |**pekko-http** is an HTTP server based on Pekko, and similar in feel to **Express.js** on Node and **Vert.x-Web** for Java.
      |
      |---
      |
      |## Hello world
      |
      |This is using a typed actor system, but we could also do it using the untyped version
      |
      |```
      |// Create the actor system
      |given actorSystem:ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")
      |
      |// Define the routes
      |val route = {
      |    path("hello") {                                                                      // On the URL path /hello...
      |        get {                                                                            // ...when a GET request comes in ...    
      |            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Hello world"))         // ...complete it with the string Hello world.
      |        }
      |    }
      |}
      |
      |// Start the server
      |val server = Http().newServerAt("localhost", 8080).bind(route)
      |```
      |
      |---
      |
      |## Imports
      |
      |Being a JVM language, there's a lot of imports at the top
      |
      |(But realistically, no worse than a `package.json` file for Node)
      |
      |```
      |//> using dep "org.apache.pekko::pekko-stream::1.1.3"
      |//> using dep "org.apache.pekko::pekko-actor-typed::1.1.3"
      |//> using dep "org.apache.pekko::pekko-http::1.1.0"
      |
      |import org.apache.pekko
      |import pekko.http.scaladsl.Http
      |import pekko.http.scaladsl.server.Directives.*
      |import pekko.http.scaladsl.model.*
      |import pekko.actor.typed.scaladsl.*
      |import pekko.actor.typed.ActorSystem
      |```
      |
      |---
      |
      |
      |
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
