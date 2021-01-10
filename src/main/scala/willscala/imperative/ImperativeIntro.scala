package willscala.imperative

import com.wbillingsley.veautiful.html.{<, Markup, SVG, VHtmlComponent, VHtmlNode, ^}
import willscala.templates.Topic

import scala.scalajs.js

val imperativeIntro = <.div(
  "This is an intro to the imperative topic"
)


val imperativeTopic = Topic(
  name = "Imperative programming",
  image = <.div(),
  content = <.div(),
  cssClass = "imperative",
  completion = () => ""
)