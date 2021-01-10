package willscala

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular._
import org.scalajs.dom

given styleSuite:StyleSuite = StyleSuite()
val site = Site()


object Main {

  val scaleChallengesToWindow:Boolean = {
    !dom.window.location.search.contains("scale=off")
  }

  def main(args:Array[String]): Unit = {
    val n = dom.document.getElementById("render-here")
    n.innerHTML = ""

    site.pageLayout.leftSideBarStyle.addRules(Map(
      "" -> """
          |background: #373a3c;
          |color: rgb(186, 186, 186);
          |border: none;
          |""".stripMargin,
      " a" ->
        """color: rgb(186, 186, 186);
          |""".stripMargin,
    ))

    site.pageLayout.tocItemStyles(-1).addRules(Map(
      ".active" -> """
          |background: #ffffff10;
          |""".stripMargin,
      "" -> """
          |transition: background 0.25s;
          |""".stripMargin,
    ))
    
    site.pageLayout.sideBarToggleStyle.addRules(
      """
        |background: #373a3c;
        |border: none;
        |""".stripMargin)
    
    site.toc = site.Toc(
      "Home" -> site.HomeRoute,
      
      "1. Imperative programming" -> site.Toc(
        "Intro" -> site.addPage("imperative", imperative.imperativeIntro)
      ),
      "2. Functional programming" -> site.Toc(
      ),
      "3. Funcitons as values" -> site.Toc(
      ),
      "4. Types" -> site.Toc(
      ),
      "5. Category theory" -> site.Toc(
      ),
      "6. Futures and effects" -> site.Toc(
      ),
      "7. Lazy, strict, and by-name" -> site.Toc(
      ),
      "8. Reactive programming" -> site.Toc(
      ),
      "9. Asynchronous streams" -> site.Toc(
      ),
      "10. Fast data" -> site.Toc(
      ),
      "11. Scala.js and Veautiful" -> site.Toc(
      ),
    )
    
    site.home = () => site.renderPage(frontPage.layout())
    site.attachTo(n)

  }

}
