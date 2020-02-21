package willscala.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlComponent, ^}
import org.scalajs.dom.{Element, Node}

case class KalturaStage(videoId:String, partnerId:String, size:(Int, Int) = (1600, 960)) extends VHtmlComponent {

  override protected def render: DiffNode[Element, Node] = {
    val (width, height) = size

    <("iframe")(
      ^.attr("width") := width, ^.attr("height") := height,
      ^.src := s"https://cdnapisec.kaltura.com/p/$partnerId/sp/42442100/embedIframeJs/uiconf_id/7033932/partner_id/$partnerId?iframeembed=true&playerId=kaltura_player&entry_id=$videoId",
      ^.attr("frameBorder") := "0", ^.attr("allowFullScreen") := "true"
    )
  }

}

object KalturaStage {

  val globalPartnerId:String = "424421"

}

