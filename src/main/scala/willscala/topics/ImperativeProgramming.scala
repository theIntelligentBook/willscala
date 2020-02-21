package willscala.topics

import com.wbillingsley.veautiful.html.{<, VHtmlNode, ^}
import com.wbillingsley.veautiful.templates.Challenge
import com.wbillingsley.veautiful.templates.Challenge.Level
import willscala.{Common, ImperativeRoute, IntroRoute, Main, Router}
import willscala.templates.{Echo360Stage, KalturaStage, MarkdownStage, Topic}

object ImperativeProgramming {

  val topic = Topic(
    name = "Imperative programming",
    image = <.div(),
    content = <.div(),
    cssClass = "imperative",
    completion = () => ""
  )

  implicit val nextButton: () => VHtmlNode = () => {
    challenge.next match {
      case Some((l, s)) => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ImperativeRoute(l, s)), s"Next")
      case _ => <.a(^.cls := "btn btn-outline-secondary pulse-link", ^.href := Router.path(ImperativeRoute(0, 0)), s"Start")
    }
  }

  val levels =Seq(
    Level(name="Imperative Programming", stages=Seq(
      Echo360Stage("51342d36-7817-47bd-a702-4ec8e49cfe32"), // Thinking about programming
    )),
    Level(name="On-campus intro", stages=Seq(
      Echo360Stage("a925bbf3-1c9d-4004-9325-5b78f20cf02e"), // On-campus Intro
    )),
    Level(name="Object oriented Scala", stages=Seq(
      Echo360Stage("487d1186-abe2-451d-ade6-9e467494c45b"), // OO
    )),
    Level(name="Tutorial: imperative Scala", stages=Seq(
      MarkdownStage(() =>
        """
          |### Setting up your environment
          |
          |Although our student development server is the officially supported programming environment for this unit
          |(I can't debug your home computer installation), Scala projects are very portable and I also recommend
          |setting up a programming environment on your own computer.
          |
          |Here's what you'll need:
          |
          |* Git, the version control system, which we'll use for downloading tutorials and assignment starter projects.
          |  (You can download the projects as zips, but using git is recommended.)
          |
          |  install from [http://git-scm.com/](http://git-scm.com/)
          |
          |* A Java Development Kit (JDK). Either OpenJDK or Oracle JDK is fine.
          |  If you're on Mac, Linux, or using Windows Subsystem for Linux, then [SDKMAN!](https://sdkman.io/) is an
          |  excellent way to install Java and many of the tools you'll need.
          |
          |  Otherwise, [AdoptOpenJDK](https://adoptopenjdk.net/) provide free downloadable pre-built versions of the
          |  JDK for most platforms.
          |
          |* SBT - the Scala Build Tool. If you're using SDKMAN! it can install it for you. If not, get it from
          |  [https://scala-sbt.org](https://scala-sbt.org)
          |
          |* An IDE for coding. I recommend [IntelliJ IDEA Community Edition](https://www.jetbrains.com/intellij) as it
          |  has a Scala plugin that works very well. Visual Studio Code with the "Metals" plugin for Scala is also
          |  possible, but I've tended to find it needs more setting up and it's less mature.
          |
          |The tutorials are provided as Git repositories, usually with the instructions in the tests
          |""".stripMargin),
      MarkdownStage(() =>
        s"""
          |### Downloading the tutorial
          |
          |The tutorial is on GitHub. There are two ways to get the project:
          |
          |* ${Common.cloneGitHubStr("firststeps")}
          |* ${Common.downloadGitHubStr("firststeps")}
          |
          |First, take a look at the files in the directory you've just downloaded.
          |
          |I'm going to assume you're on Linux or OS X for these instructions. If you're on Windows, though, open
          |PowerShell ISE, and most of this should work without too much extra fiddling. (Or, if you're on Windows 10,
          |you could try out Windows Subsystem for Linux - though it needs a little setting up.)
          |
          |cd into the directory where build.sbt is. Run sbt:
          |```sh
          |sbt
          |```
          |
          |This is going to run the Scala Build Tool in interactive mode.
          |
          |First, the launcher will go try to fetch the right version of sbt. So it might spend some time downloading.  Then, it'll read build.sbt and try to understand the project. This will often trigger it to download more libraries it needs just to understand your project.
          |
          |Eventually, it'll give you a command prompt. You are now at the sbt shell prompt. `Ctrl-D` or `Ctrl-C` will let you exit sbt.  As will the "exit" command.
          |
          |Let's run the unit tests:
          |```
          |test
          |```
          |
          |This command will try to compile the code in the project, and then run some automated tests that we've also
          |defined in the project. But first, it'll need to check that it's got all the libraries the project depends on.
          |Yes, it's going to run off to the internet and do some more downloading for you.
          |
          |(Fortunately, it caches the libraries, so it is usually only slow the first time you build a project.)
          |
          |Eventually, the code should compile, but you'll get a lot of failing tests. We'll see why when we open the code.
          |
          |""".stripMargin),
      MarkdownStage(() =>
      """
          |### Exploring the code
          |
          |First, let's quit that sbt shell so you can go back to the Unix shell. There's three ways to do this:
          |
          |* `exit` (sbt command)
          |* `ctrl-d` (key combination for "end of stream")
          |* `ctrl-c` (key combination for quitting a unix process)
          |
          |If you haven't already done it, set up IntelliJ. Don't forget to install the Scala plugin for IntelliJ
          |(from the set-up screen the first time, or you can find plugins under `File` -> `Settings...`)
          |
          |Open IntelliJ, and import the tutorial as an SBT project. Time to code...
          |
          |Expand the `src/main` folder, and its subfolders, until you find `StepOne`. Open it up and have a look at the code.
          |
          |There are a number of challenges written as comments that should get you to try writing some Scala code.
          |For this tutorial, we're not worried about trying to do functional programming yet.
          |The challenges will also ask you to re-run the tests. So you'll get into a cycle of doing some coding, running the tests, seeing why they fail, and then going back to do some more coding.
          |And as you get the code working, the tests will start to pass.
          |
          |**NB**: SBT can only run the tests if your code compiles. If your code won't compile, you'll just get a compile error. If you just need to get something out of the way in Scala - for example, if a compile error in a different test than the one you are working on is getting in the way, there is a useful keyword to know:
          |
          |```
          |???
          |```
          |
          |This shorthand for "throw an unimplemented exception". Throwing an exception is a runtime error rather than a
          |compile error. So for example, this would give a compile error stopping all your tests from running:
          |
          |```
          |def double(x:Int):Int = "Oops, I'm returning a String not an Int"
          |```
          |
          |But this would compile ok, but give a runtime error (just stopping whichever test it is called from):
          |```
          |def double(x:Int):Int = ???
          |```
          |""".stripMargin),
      Echo360Stage("c27caeee-0dd4-459a-b019-5a5c2e97a90c")
    ))
  )

  val challenge = Challenge(
    levels,
    homePath = (_) => Router.path(IntroRoute),
    levelPath = (_, i) => Router.path(ImperativeRoute(i, 0)),
    stagePath = (_, i, j) => Router.path(ImperativeRoute(i, j)),
    homeIcon = Common.symbol,
    scaleToWindow = Main.scaleChallengesToWindow
  )



}
