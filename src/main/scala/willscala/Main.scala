package willscala

import com.wbillingsley.veautiful.html.Attacher
import org.scalajs.dom

object Main {

  val scaleChallengesToWindow:Boolean = {
    !dom.window.location.search.contains("scale=off")
  }

  def main(args:Array[String]): Unit = {
    val n = dom.document.getElementById("render-here")
    n.innerHTML = ""
    val root = Attacher.newRoot(n)
    root.render(Router)
  }

}
