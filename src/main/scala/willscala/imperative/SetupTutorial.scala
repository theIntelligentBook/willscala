package willscala.imperative


import com.wbillingsley.veautiful.html.*
import willscala.templates.Topic
import willscala.Common._

val setupTutorial = <.div(
  marked(
    """## Practical: Setting up your development environment
      |
      |Many of the tutorials for this subject are provided as git repositories. 
      |
      |First, though, you're going to need to set up your development environment.
      |You'll need:
      |
      |* **Git**, the version control system.  
      |  You can download this from [https://git-scm.com/](https://git-scm.com/)
      |  
      |  If you're unfamiliar with Git, you can download the tutorials as zips, but it's a very useful tool, and 
      |  if you're taking a Scala course you're probably interested enough in programming to want to use it!
      |  
      |* A **Java Development Kit (JDK)**. OpenJDK 11 is recommended.
      |  *Note: you'll need a JDK not just a JRE, especially if you use Visual Studio Code as your IDE*
      |  
      |  Adoptium provide pre-built binaries for Windows, Linux, and More.
      |  [https://adoptium.net/](https://adoptium.net/)  
      |  When you are installing it, I recommend letting it update your PATH and JAVA_HOME environment variables.
      |  
      |  Or, if you are on a UNIX-like system (e.g. Mac, Linux) then [SDKMAN!](https://sdkman.io/) is very useful for installing software
      |  development environments. 
      |
      |  Or, if you want to use Scala's dedicated (but perhaps less well documented) set-up tool,
      |  [Coursier](https://docs.scala-lang.org/scala3/getting-started.html#using-the-scala-installer-recommended-way)
      |  can ensure you have a JVM and SBT.
      |  
      |* The **Scala Build Tool (SBT)**.
      |  Install from [https://scala-sbt.org/](https://scala-sbt.org/)  
      |  (Or if you're using *SDKMAN!*, it can install it for you - just run `sdk install sbt`)  
      |  (Or if you're using Coursier, its `cs setup` task will ensure you have sbt)
      |
      |* An **IDE** for code editing. There are a couple of choices:
      |
      |  - **IntelliJ IDEA Community Edition**  
      |    Install this from [https://www.jetbrains.com/intellij](https://www.jetbrains.com/intellij).  
      |    Select the Scala plugin when you first run IntelliJ and it asks you which "featured plugins" to install.
      |    (Or you can install it later from the menus)
      |    
      |  - **Visual Studio Code** with the **Scala syntax (official)** plugin.
      |    Install Visual Studio Code from [https://code.visualstudio.com](https://code.visualstudio.com/).
      |    From the extensions pane (on the left) install the **Scala syntax (official)** plugin. 
      |    
      |    Later, you may also wish to install the **Metals** plugin. This uses the "build server protocol"
      |    to talk to SBT so it can do syntax highlighting, intellisense (autocomplete for function names), and give
      |    you quick ways of running main methods and unit test suites.  
      |    For more details on Metals see [https://scalameta.org/metals/](https://scalameta.org/metals/)
      |  
      |""".stripMargin)
)