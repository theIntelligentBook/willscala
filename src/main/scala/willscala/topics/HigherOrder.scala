package willscala.topics

import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.Level
import willscala.templates.{Echo360Stage, MarkdownStage, Topic, YouTubeStage}
import willscala._

object HigherOrder {

  val topic = Topic(
    name = "Functions as values",
    image = <.div(),
    content = <.div(),
    cssClass = "higher-order",
    completion = () => ""
  )

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(FunctionalRoute(l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(FunctionalRoute(0, 0)), s"Start")
    }
  }

  val levels =Seq(
    Level(name="Higher Order Functions", stages=Seq(
      Echo360Stage("228fa135-887d-4ca4-b483-b6e78929a0df"), // Functional programming
    )),
    Level(name="Case Classes and Pattern Matching", stages=Seq(
      Echo360Stage("e880ccc3-f8d0-4b20-9989-e64207c39de5")
    )),
    Level(name="Tutorial: Sudoku Sensei", stages=Seq(
      MarkdownStage(() =>
        s"""
          |### Tutorial: Sudoku Sensei
          |
          |There's two parts to this tutorial. The first is to revisit tutorial 1 ... When we introduced Scala syntax,
          |I suggested solving those tests imperatively. But they are all much shorter to solve functionally, if you
          |use higher order functions. As a hint, doubling a list is
          |
          |```scala
          |list.map(_ * 2)
          |```
          |
          |As you've already got a download of that project, I've not put in a separate link
          |
          |The second part of the tutorial is Sudoku Sensei - a little set of functions that will tell you if there are
          |squares you can automatically fill in in a Sudoku puzzle
          |
          |* ${Common.cloneGitHubStr("tutorial_sudoku_sensei")}
          |* ${Common.downloadGitHubStr("tutorial_sudoku_sensei")}
          |
          |Time to download the repository. The instructions are in the tests!
          |""".stripMargin),

      YouTubeStage("L3SKMF-fpRA")
    ))


  )

  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(HigherOrderRoute(i, 0)),
    stagePath = (_, i, j) => Router.path(HigherOrderRoute(i, j)),
    homeIcon = Common.symbol,
    scaleToWindow = Main.scaleChallengesToWindow
  )

}
