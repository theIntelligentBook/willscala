package willscala.imperative

import com.wbillingsley.veautiful.html.{<, Markup, SVG, VHtmlComponent, VHtmlNode, ^, unique}
import willscala.templates.Topic
import willscala.Common._

val tutorial = unique(<.div(
  marked(
    """## Practical: First steps in Scala
      |
      |The first tutorial just tries to get you over some basic Scala syntax and get you set up in your development environment. 
      |We'll get to more idiomatic ways of writing Scala very soon, but for the moment, there are often enough hiccups just in getting set up and knowing some different keywords first.
      |
      |#### 1. Set up your development environment.
      |
      |  If you haven't already done so, follow the steps in the set up tutorial, to download git, a JDK, SBT, and an IDE.
      |   
      |#### 2. Get the tutorial repository. 
      |
      |Either:
      |
      |  * `git clone https://github.com/UNEcosc250/firststeps.git`
      |  * or open a browser, go to [https://github.com/UNEcosc250/firststeps](https://github.com/UNEcosc250/firststeps) and download and extract the zip
      |   
      |In either case, you should end up with a folder containing a tree of files 
      |
      |#### 3. Take a look at the folder you've just downloaded 
      |
      |There are a few files we'll point out to you for now:
      |   
      |   * `build.sbt` contains the project definition. This tells SBT (the Scala Build Tool) details like the version of Scala we're using
      |     and any libraries we want it to download for us.
      |     
      |   * `project/build.properties` contains a property setting what version of SBT we're using. When we open the project in SBT, the
      |     first thing the launcher will do is go and get the version of SBT we say we need.
      |     
      |   * `project/plugins.sbt` tells SBT what plugins to load. Because Scala 3 is still in "developer preview", the Scala 3 compiler (codename Dotty) is provided via a plugin.
      |     
      |   * `src/main/scala` contains the source code for our project. This is where you're going to do most of your work.
      |   
      |   * `src/test/scala` contains some unit tests. These are going to test your code and see if it works
      |
      |#### 4. Run `sbt` from the command line
      |
      |Although we can get the IDE to do a lot for us, it's a good idea to get to know SBT from the command-line.
      |That way there are slighltly fewer complex tools between you and what you're doing.
      |
      |From a terminal (e.g. PowerShell or bash), `cd` into the project directory (the one containing `build.sbt`).
      |Assuming sbt is on your path, run it using 
      |
      |```sh
      |sbt
      |```
      |
      |This is going to run sbt in interactive mode. It'll load the project, and then initially spend quite a bit of time downloading
      |components it needs such as the right version of SBT and the right version of the compiler.
      |
      |Eventually, it'll give you a command prompt. You are now at the sbt shell prompt. Ctrl-C or Ctrl-D will let you exit. As will the `exit` command.
      |
      |Let's run the tests. From the sbt prompt:
      |
      |```sbt
      |test
      |```
      |
      |This command will try to compile the code in the project, and then run some automated tests that we've also defined in the project. 
      |But first, it'll need to check that it's got all the libraries the project depends on. Yes, it's going to run off to the internet and do some more downloading for you.
      |(Fortunately, it caches the libraries, so it is usually only slow the first time you build a project.)
      |
      |Eventually, the code should compile, but you'll get a lot of failing tests. We'll see why when we open the code.
      |
      |First, let's quit that sbt shell so you can go back to the Unix shell. There's three ways to do this:
      |
      |* exit (sbt command)
      |* ctrl-d (key combination for "end of stream")
      |* ctrl-c (key combination for quitting a unix process)
      |
      |#### 4. Time to code
      |
      |Open the project in your IDE. You can open the folder from the menus (open the folder containing `build.sbt`) and
      |if you have the plugins set up, it should spot that it's an SBT project to import. Or, if you're using Visual
      |Studio code, you can often run `code .` (note the space and the dot) from the command line to open the project.
      |
      |Expand the `src/main/scala` folder in your IDE, until you find `StepOne`. Open it up and have a look at the code.
      |
      |There are a number of challenges written as comments that should get you to try writing some Scala code. 
      |For this tutorial, we're not worried about trying to do functional programming yet. 
      |
      |The challenges will also ask you to re-run the tests, so you'll get into a cycle of doing some coding, running the tests, seeing why they fail, and then going back to do some more coding.
      |And as you get the code working, the tests will start to pass.
      |
      |**NB**: SBT can only run the tests if your code compiles. If your code won't compile, you'll just get a compile error. 
      |If you just need to get something out of the way in Scala - for example, if a compile error in a different test than the one you are working on is getting in the way, there is a useful keyword to know:
      |
      |```scala
      |???
      |```
      |
      |This shorthand for "throw an unimplemented exception". Throwing an exception is a runtime error rather than a compile error.
      |
      |So for example, this would give a compile error stopping all your tests from running:
      |
      |```scala
      |def double(x:Int):Int = "Oops, I'm returning a String not an Int"
      |```
      |
      |But this would compile ok, but give a runtime error (just stopping whichever test it is called from):
      |
      |```scala
      |def double(x:Int):Int = ???
      |```
      |
      |""".stripMargin)
))