package willscala.topics

import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.Level
import willscala.{Common, FunctionalRoute, ImperativeRoute, IntroRoute, Main, Router}
import willscala.templates.{Echo360Stage, MarkdownStage, Topic}

object FunctionalProgramming {

  val topic = Topic(
    name = "Functional programming",
    image = <.div(),
    content = <.div(),
    cssClass = "functional",
    completion = () => ""
  )

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(FunctionalRoute(l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(FunctionalRoute(0, 0)), s"Start")
    }
  }

  val levels =Seq(
    Level(name="Functional Programming", stages=Seq(
      Echo360Stage("7aa76c00-8be7-48bc-96b7-4c1ae8847937"), // Functional programming
    )),
    Level(name="Patterns, Recursion, and Lists", stages=Seq(
      Echo360Stage("4753ffae-2e52-4358-a375-e0e52d67e78e")
    )),
    Level(name="Tutorial: Tail recursive madness!", stages=Seq(
      MarkdownStage(() =>
        s"""
          |### Tutorial: Tail Recursive Madness!
          |
          |Once again, the tutorial is provided as a GitHub repository.
          |
          |* ${Common.cloneGitHubStr("tailrecursivemadness")}
          |* ${Common.downloadGitHubStr("tailrecursivemadness")}
          |
          |As is usually the case, there's a master branch
          |containing the problem, and a solution branch containing a pre-worked solution. There's also a solution video
          |(next in this sequence)
          |
          |This week, it's a set of exercises that are all about writing recursive functions, and trying to make them
          |tail-recursive. It might feel like solving Sudoku puzzles - recursion is something students who are new
          |to functional programming find tricky. It's something you should know - although in future weeks we're going
          |to come across some other approaches to writing programs that might make more intuitive sense.
          |
          |Time to download the repository. The instructions are in the tests!
          |""".stripMargin),

      Echo360Stage("3b3a445f-c068-43cf-a5d2-4a18d24a1bf7")
    ))


  )

  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(FunctionalRoute(i, 0)),
    stagePath = (_, i, j) => Router.path(FunctionalRoute(i, j)),
    homeIcon = Common.symbol,
    scaleToWindow = Main.scaleChallengesToWindow
  )

}
