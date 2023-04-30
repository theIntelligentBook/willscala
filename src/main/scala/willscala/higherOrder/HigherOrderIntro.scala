package willscala.higherOrder


import com.wbillingsley.veautiful.html.*
import willscala.templates.Topic
import willscala.Common._
import willscala.Styles._
import willscala.given

import scala.scalajs.js

val higherOrderIntro = <.div(
  chapterHeading(3, "Functions as Values", "images/lambdagift.png"),
  marked("""
           |In this chapter, we're going to start looking at *higher order functions*: functions that take functions as 
           |arguments. You might already have come across these, for example in JavaScript. 
           |These let us write some remarkably expressive programs: short programs that do a lot, and (after you've seen
           |a few) can be easier to read than the equivalent imperative program.
           |
           |And the really good news is that being able to use higher order functions means we'll very rarely have to 
           |write a recursive function. `map`, `filter`, `exists`, `flatMap`, etc, are your friends...
           |""".stripMargin),
)