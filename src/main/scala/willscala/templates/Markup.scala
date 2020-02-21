package willscala.templates

import com.wbillingsley.veautiful.html.VHtmlNode
import com.wbillingsley.veautiful.templates.Challenge.{Complete, Stage}


import com.wbillingsley.veautiful.Update
import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import org.scalajs.dom.{Element, Node}

import scala.scalajs.js

class Markup(transform:(String) => String) {

  case class MarkupNode(data: () => String) extends VHtmlNode with Update {

    private var _domNode: Option[Element] = None
    private var lastData: Option[String] = None

    /**
     * The dom node that this is currently attached to.
     *
     * Note that if a VNode uses more than one real node to implement itself, parent.get.domNode.get might not be
     * the same as domNode.get.getParent(), even if the gets were to succeed.
     */
    override def domNode: Option[Node] = _domNode

    /**
     * Called to perform an attach operation -- ie, create the real DOM node and put it into
     * domNode
     */
    override def attach(): Node = {
      val e = <.div(^.cls := "markup-node").create()
      _domNode = Some(e)
      e
    }

    override def afterAttach(): Unit = {
      super.afterAttach()
      lastData = None
      update()
    }

    /**
     * Called to perform a detach operation -- ie, anything necessary to clean up the DOM node,
     * and then remove it from domNode so we know it's gone.
     */
    override def detach(): Unit = {
      // do nothing
    }

    override def update(): Unit = {
      for {e <- _domNode} {
        try {
          val d = data()
          if (!lastData.contains(d)) {
            e.innerHTML = transform(d)
            lastData = Some(d)
          }
        } catch {
          case x: Throwable =>
            x.printStackTrace()
            e.innerHTML = "ERROR: " + x.getMessage
        }
      }
    }
  }

}

object Markup {
  lazy val marked = new Markup({ s:String => js.Dynamic.global.marked(s).asInstanceOf[String] })
}