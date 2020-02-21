package willscala.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.Open
import org.scalajs.dom.{Element, Node}

case class YouTubeStage(yt:String) extends Challenge.Stage {

  override def kind: String = "video"

  var completion: Challenge.Completion = Open

  override protected def render: DiffNode[Element, Node] = {
    <.div(
      <("iframe")(
        ^.attr("width") :="1600", ^.attr("height") := "960", ^.src := s"https://www.youtube.com/embed/$yt",
        ^.attr("frameborder") := "0", ^.attr("allow") :="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture",
        ^.attr("allowfullscreen") := "allowfullscreen")
    )
  }
}