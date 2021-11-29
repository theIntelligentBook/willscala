package willscala.cattheory

import com.wbillingsley.veautiful.html._
import willscala.Common._
import willscala.Styles._
import willscala.templates.Topic
import willscala.given

import scala.scalajs.js

val catTheoryIntro = unique(<.div(
  chapterHeading(5, "Category Theory", "images/typeclasses.png"),
  marked("""
           |Some terms in functional programming come from a branch of mathematics called *Category Theory*.
           |For instance, monoid, monad, functor, and applicative. In this chapter, we're going to take a brief
           |look at what this is and also introduce *typeclasses*. 
           |""".stripMargin
  ),
))