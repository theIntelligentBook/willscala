package willscala.actors

import com.wbillingsley.veautiful.html.{<, ^, unique}
import willscala.Common.{Echo360Video, chapterHeading, marked}
import willscala.templates.{FrontPage}
import willscala.topics.{FunctionalProgramming, HigherOrder, ImperativeProgramming}

val intro = unique(<.div(
  chapterHeading(8, "Actors", "images/alarmclock.jpg"),
  marked("""Let's take our revenge and make the computer a slave to its inbox.
           |
           |In imperative programming, if we have shared mutable state between processes, we can run into concurrency problems.
           |In functional programming, we can have shared state because we don't have mutable state.
           |In *Concurrency-oriented Programming*, we can have mutable state because we don't have shared state.
           |
           |Erlang is the language most associated with the Actor model. It's also famous for a "let it crash" philosophy, where
           |if you've got a process that gets into an unknown state, rather than try to recover it and possibly stumble on from
           |an unknown state with unexpected errors, you just let it crash and re-start it cleanly.
           |
           |Akka is Scala's most famous Actor library. Its "Classic Actors" (untyped actors) are similar to Erlang. 
           |Its "Typed Actors" take a more functional programming approach to actors. We'll take a look at both.
           |""".stripMargin),
))