package willscala.functional

import com.wbillingsley.veautiful.html.{<, Markup, SVG, VHtmlComponent, VHtmlNode, ^, unique}
import willscala.templates.Topic
import willscala.Common._

val tutorial = unique(<.div(
  marked(
    s"""## Practical: Tail recursive madness
      |
      |This practical a set of exercises that are all about writing recursive functions, and trying to make them
      |tail-recursive. This might feel like solving Sudoku puzzles - recursion is something students who are new
      |to functional programming find tricky. It's something you should know - although in future weeks we're going
      |to come across some other approaches to writing programs that might make more intuitive sense.
      |      
      |Once again, the practical is provided as a GitHub repository.
      |
      |* ${cloneGitHubStr("tailrecursivemadness")}
      |* ${downloadGitHubStr("tailrecursivemadness")}
      |
      |As is usually the case, there's a master branch containing the problem, and a solution branch containing a pre-worked solution. 
      |There's also a solution video.
      |
      |Time to download the repository. The instructions are in the tests!
      |""".stripMargin)))