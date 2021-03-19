package willscala.types

import com.wbillingsley.veautiful.html._
import willscala.Common._
import willscala.Styles._
import willscala.templates.Topic

import scala.scalajs.js

val typesIntro = unique(<.div(
  chapterHeading(4, "Types", "images/types/babyrabbits.jpg"),
  marked("""
           |We've already met several types of data, ranging from `Int`s to `Strings` to traits and classes.
           |We've even snuck in a *type alias* or two. 
           |In this chapter, we're going to look at some more advanced concepts around types. 
           |""".stripMargin
  ),
))