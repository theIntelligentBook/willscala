package willscala.asyncstreams

import com.wbillingsley.veautiful.html.{<, ^, unique}
import willscala.Common.{Echo360Video, chapterHeading, marked}
import willscala.templates.{FrontPage}
import willscala.topics.{FunctionalProgramming, HigherOrder, ImperativeProgramming}

val intro = unique(<.div(
  chapterHeading(8, "Asynchronous Streams", "images/alarmclock.jpg"),
  marked("""Many systems have to deal with a stream of events
           |
           |For example, if you consider the back-end of a popular video streaming service, there may be
           |thousands or millions of watch events coming from users at any one time. Although responding
           |to the watch event with video data might be a one-time response (the player asks for some
           |video data and it sends it back), there may be analytics systems that have to receive to
           |*every* viewer's watch event.
           |
           |In this topic, we take a look at some of the ways of managing streams of events that might
           |occur at different rates.
           |""".stripMargin),
))