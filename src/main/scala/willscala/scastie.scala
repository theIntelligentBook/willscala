package willscala

import scalajs.js
import scalajs.js.annotation.JSGlobal 
import com.wbillingsley.veautiful.Decorator

import com.wbillingsley.veautiful.html.*

@js.native
@JSGlobal
object scastie extends js.Object {
    def Embedded(id:String, config:js.Any):Unit = js.native
}

/** An element to show a Scastie snippet. Note: doesn't work in slides due to zoom. */
case class ScastieSnippet(hash:String) extends Decorator(<.div(<.div(^.attr.id := s"s$hash")).build()) {
    override def afterAttach(): Unit = 
        super.afterAttach()

        scastie.Embedded(s"#s$hash", js.Dictionary(
            "base64UUID" -> hash
        ))
}