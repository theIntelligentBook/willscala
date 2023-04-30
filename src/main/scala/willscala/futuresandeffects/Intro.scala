package willscala.futuresandeffects

import com.wbillingsley.veautiful.html.{<, ^, unique}
import willscala.Common.{Echo360Video, chapterHeading, marked}

val futuresIntro = <.div(
  chapterHeading(6, "Futures and Effects", "images/alarmclock.jpg"),
  marked("""Now that we have the concept of `monad`s and can use for-comprehensions across any monad-like type
           |(rather than only across collections), we can use these structures to describe complex situations in a
           |simple manner. 
           |
           |For instance, we can describe flows of events that will happen in the future.
           |At 7am, wake me up, turn the kettle on, and when it's boiled I'll make a cup of tea.
           |These are flows that we are used to considering conceptually in every-day language, but in programming
           |we need a mechanism to talk about these future events.
           |
           |Often, the reason we want to talk about something asynchronously is because it's happening outside our
           |program. So, libraries and structures that describe asynchronicity are often related to the idea of 
           |describing having an *effect* - changing something outside the program.
           |""".stripMargin),
)
