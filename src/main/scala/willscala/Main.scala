package willscala

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular._
import org.scalajs.dom

import Common._

val site = Site()


object Main {

  val scaleChallengesToWindow:Boolean = {
    !dom.window.location.search.contains("scale=off")
  }

  def main(args:Array[String]): Unit = {
    val n = dom.document.getElementById("render-here")
    n.innerHTML = ""

    Styles.installStyles()
        
    site.toc = site.Toc(
      "Home" -> site.HomeRoute,
      
      "1. Imperative programming" -> site.Toc(
        "Intro" -> site.addPage("imperative", imperative.imperativeIntro),
        "Slides: Intro to Scala syntax" -> site.addDeck("introScala", imperative.introScala),
        "Slides: OO in Scala" -> site.addDeck("objectOrientedScala", imperative.scalaOO),
        "Practical: Set up" -> site.addPage("tutorial-0", imperative.setupTutorial),
        "Practical: First Steps in Scala" -> site.addPage("tutorial-1", imperative.tutorial)
      ),
      "2. Functional programming" -> site.Toc(
        "Intro" -> site.addPage("functional", functional.functionalIntro),
        "Slides: Recursion and the Stack" -> site.addDeck("recursionAndTheStack", functional.tailRecursion),
        "Slides: Patterns and Lists" -> site.addDeck("patternsAndLists", functional.patternsAndLists),
        "Practical: Recursion" -> site.addPage("tutorial-2", functional.tutorial),
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
