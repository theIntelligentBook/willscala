package willscala.templates

import com.wbillingsley.veautiful.DiffNode
import com.wbillingsley.veautiful.html.{<, VHtmlContent}
import com.wbillingsley.veautiful.doctacular.Challenge
import org.scalajs.dom.{Element, Node}
import willscala.Common

case class MarkdownStage(t: () => String)(implicit val nextButton: () => VHtmlContent) extends Challenge.Stage {

  val content = Common.marked(t())

  override def completion: Challenge.Completion = Challenge.Open

  override def kind: String = "text"

  override protected def render = <.div(
    Challenge.textAndEx(<.div(content, nextButton()))(<.div())
  )

}
