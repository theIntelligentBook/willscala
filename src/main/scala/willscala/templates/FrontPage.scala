package willscala.templates

import willscala.{Common, site}
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}

case class FrontPage(banner:VHtmlNode, topMessage:VHtmlNode, topics:Seq[(site.Route, Topic)]) {

  def layout = {
    <.div(^.cls := "front-page",
      <.div(^.cls := "course-banner", banner),
      <.div(^.cls := "top-message", topMessage),
      <.div(^.cls := "topic-container",
        for { (r, t) <- topics } yield t.block(site.router.path(r))
      )
    )
  }


}
