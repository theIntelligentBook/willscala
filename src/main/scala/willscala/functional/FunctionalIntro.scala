package willscala.functional

import com.wbillingsley.veautiful.html.*
import willscala.templates.Topic
import willscala.Common._
import willscala.Styles._

import scala.scalajs.js

val functionalIntro = <.div(
  chapterHeading(2, "Functional Programming", "images/functional.png"),
  marked("""
           |In this chapter, we're going to introduce *functional programming*, which is a different way of thinking about programming. 
           |Rather than imagining a program as a set of instructions executed in order, changing some state, we think about functions in the same way we might think of a maths function:
           |it takes some arguments, and produces a result.
           |""".stripMargin
  ),
  <.p(^.attr("style") := "text-align: center; margin-top: 8rem;",
    Echo360Video("7aa76c00-8be7-48bc-96b7-4c1ae8847937").embeddedPlayer(720, 480)
  )
)