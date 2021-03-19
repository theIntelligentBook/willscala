package willscala.types


import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.{Common, Styles, styleSuite}
import Common.{marked, willCcBy}

import coderunner.JSCodable
import lavamaze.Maze

val variance = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Variance
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """## Still writing...
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
