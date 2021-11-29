package willscala

import com.wbillingsley.veautiful.PathDSL
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.HistoryRouter
import willscala.templates.{FrontPage}
import willscala.topics.{FunctionalProgramming, HigherOrder, ImperativeProgramming}

sealed trait Route
case object IntroRoute extends Route
case class ImperativeRoute(l:Int, s:Int) extends Route
case class FunctionalRoute(l:Int, s:Int) extends Route
case class HigherOrderRoute(l:Int, s:Int) extends Route

object Router extends HistoryRouter[Route] {

  var route:Route = IntroRoute
  
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
