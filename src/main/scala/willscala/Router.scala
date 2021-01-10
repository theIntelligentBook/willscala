package willscala

import com.wbillingsley.veautiful.PathDSL
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.HistoryRouter
import willscala.templates.{FrontPage, Markup}
import willscala.topics.{FunctionalProgramming, HigherOrder, ImperativeProgramming}

sealed trait Route
case object IntroRoute extends Route
case class ImperativeRoute(l:Int, s:Int) extends Route
case class FunctionalRoute(l:Int, s:Int) extends Route
case class HigherOrderRoute(l:Int, s:Int) extends Route

object Router extends HistoryRouter[Route] {

  var route:Route = IntroRoute

  val frontPage = new FrontPage(
    <.div(
      <.img(^.src := "images/willscala.jpg", ^.alt := "The Adventures of Will Scala and his Merry Programs"),
      <.div(^.cls := "abs-bottom-right white-translucent-bg",
        Markup.marked.MarkupNode(() => "[Will Billingsley](https://www.wbillingsley.com)'s Scala course")
      )
    ),
    <.div(^.cls := "lead",
      Markup.marked.MarkupNode(() =>
         """
          | This is an experimental attempt to put up a public site around [one of my teaching courses](https://my.une.edu.au/courses/2020/units/COSC250).
          | The course teaches
          | Scala, functional programming, and some asynchronous and reactive programming. It's designed for second year
          | undergraduate students, who mostly have experience of imperative languages (Python and Java). The embedded
          | videos are mostly from the 2018 edition of the course, teaching Scala 2.13. Currently, only the first two weeks are up.
          | That will gradually get updated.
          |""".stripMargin
      ),
    ),
    Seq(
      ImperativeRoute(0, 0) -> ImperativeProgramming.topic,
      FunctionalRoute(0, 0) -> FunctionalProgramming.topic,
      HigherOrderRoute(0, 0) -> HigherOrder.topic
    )
  )

  def rerender() = renderElements(render)

  def render = {
    route match {
      case IntroRoute => frontPage.layout
      case ImperativeRoute(l, s) => ImperativeProgramming.challenge.show(l, s)
      case FunctionalRoute(l, s) => FunctionalProgramming.challenge.show(l, s)
      case HigherOrderRoute(l, s) => HigherOrder.challenge.show(l, s)
    }
  }

  override def path(route: Route): String = {
    import com.wbillingsley.veautiful.PathDSL.Compose._

    route match {
      case IntroRoute => (/# / "").stringify
      case ImperativeRoute(l, s) => (/# / "imperative" / l.toString / s.toString).stringify
      case FunctionalRoute(l, s) => (/# / "functional" / l.toString / s.toString).stringify
      case HigherOrderRoute(l, s) => (/# / "higherorder" / l.toString / s.toString).stringify
    }
  }


  def parseInt(s:String, or:Int):Int = {
    try {
      s.toInt
    } catch {
      case n:NumberFormatException => or
    }
  }
  override def routeFromLocation(): Route = PathDSL.hashPathArray() match {
    case Array("") => IntroRoute
    case Array("imperative", l, s) => ImperativeRoute(parseInt(l, 0), parseInt(s, 0))
    case Array("functional", l, s) => FunctionalRoute(parseInt(l, 0), parseInt(s, 0))
    case Array("higherorder", l, s) => HigherOrderRoute(parseInt(l, 0), parseInt(s, 0))
    case x =>
      println(s"path was ${x}")
      IntroRoute
  }

}
