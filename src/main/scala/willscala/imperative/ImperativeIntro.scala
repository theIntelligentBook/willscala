package willscala.imperative

import com.wbillingsley.veautiful.html.{<, Markup, SVG, VHtmlComponent, VHtmlNode, ^, unique}
import willscala.templates.Topic
import willscala.Common._

import scala.scalajs.js

val imperativeIntro = unique(marked(
  """# 1. Imperative Programming
    |
    |If you're learning Scala, the chances are you've seen one or two different programming languages before. 
    |And for most students, the languages you've encountered will have been imperative languages (eg, Python, Java). 
    |So let's begin by taking a look at the paradigm you've seen earlier, and how modern compilers and runtimes mean 
    |there can be some unexpected things going on under the hood. 
    |
    |We'll also introduce you to Scala syntax in this chapter, but we'll do it from an imperative perspective.
    |Scala is a *mixed paradigm* language, which means you can work in different ways within the one language. 
    |For us, that's useful because we can look at a lot of topics without having to switch programming language. 
    |For this chapter, though, let's just get over that initial hurdle of the syntax of a new language being less
    |familiar.
    |""".stripMargin
))


val imperativeTopic = Topic(
  name = "Imperative programming",
  image = <.div(),
  content = <.div(),
  cssClass = "imperative",
  completion = () => ""
)