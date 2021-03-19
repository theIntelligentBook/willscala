package willscala

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular._
import Medium._
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
        "Intro to Scala syntax" -> site.add("introScala", 
          Alternative("Slide deck", Deck(() => imperative.introScala)),
          Alternative("Watch the video", Video(() => PlayableVideo(Echo360Video("92548534-cc2c-4661-99d4-f2dfc1e26309")))),
        ),
        "OO in Scala" -> site.add("objectOrientedScala",
          Alternative("Slide deck", Deck(() => imperative.scalaOO)),
          Alternative("Watch the video", Video(() => PlayableVideo(Echo360Video("302bd7dc-7a82-4d9a-a702-6efb5e94bb41")))),
        ),
        "Practical: Set up" -> site.addPage("tutorial-0", imperative.setupTutorial),
        "Practical: First Steps in Scala" -> site.addPage("tutorial-1", imperative.tutorial)
      ),
      "2. Functional programming" -> site.Toc(
        "Intro" -> site.addPage("functional", functional.functionalIntro),
        "Recursion and the Stack" -> site.add("recursionAndTheStack",
          Alternative("Slide deck", Deck(() => functional.tailRecursion)),
          Alternative("Watch the video", Video(() => PlayableVideo(Echo360Video("bb115215-0f39-4a1f-9e95-2b6060457a16")))),
        ),
        "Patterns and Case Classes" -> site.add("patternsAndCaseClasses",
          Alternative("Slide deck", Deck(() => functional.patternsAndCaseClasses)),
          Alternative("Watch the video", Video(() => PlayableVideo(Echo360Video("71af9d97-a6ed-43aa-ba6b-fdd18a0e197a")))),
        ),
        "Immutable Lists" -> site.add("immutableLists",
          Alternative("Slide deck", Deck(() => functional.immutableLists)),
          Alternative("Watch the video", Video(() => PlayableVideo(Echo360Video("696acc25-5694-4386-8129-90a8c3e492c1")))),
        ),
        "Practical: Recursion" -> site.addPage("tutorial-2", functional.tutorial),
      ),
      "3. Funcitons as values" -> site.Toc(
        "Intro" -> site.addPage("higherOrder", higherOrder.higherOrderIntro), 
        "Higher Order Functions" -> site.add("higherOrderFunctions",
          Alternative("Slide deck", Deck(() => higherOrder.higherOrderDeck)),
          Alternative("Watch the video", Video(() => PlayableVideo(Echo360Video("7f3082fe-7ec7-4ac1-bfd9-73957e60370b")))),
        ),
        "Solving Einstein's Puzzle" -> site.add("einsteinProblem",
          Alternative("Slide deck", Deck(() => higherOrder.einsteinDeck)),
          Alternative("Live-coded output", Page(() => livedemo.einsteinDemo)),
        ),
        "Practical: Sudoku Sensei" -> site.addPage("tutorial-3", higherOrder.tutorial),
      ),
      "4. Types" -> site.Toc(
        "Intro" -> site.addPage("types", types.typesIntro),
        "Einstein's Problem" -> site.add("typeSystems",
          Alternative("Type Systems", Deck(() => types.typeSystems)),
        ),
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
