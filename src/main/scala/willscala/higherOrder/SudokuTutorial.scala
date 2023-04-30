package willscala.higherOrder

import com.wbillingsley.veautiful.html.*
import willscala.templates.Topic
import willscala.Common._

val tutorial = <.div(
  marked(
    s"""## Practical: Sudoku sensei
       |
       |There are a couple of parts to this chapter. In the first part, we're going to take some of the exercises from
       |Chapter 1 that we solved imperatively, and solve them with much less code using higher order functions.
       |The second part is a set of functions that use `map`, `exists`, and `filter` to help solve some Sudoku puzzles.
       |      
       |Once again, the practical is provided as a GitHub repository.
       |
       |* ${cloneGitHubStr("tutorial_sudoku_sensei")}
       |* ${downloadGitHubStr("tutorial_sudoku_sensei")}
       |
       |As is usually the case, there's a master branch containing the problem, and a solution branch containing a pre-worked solution. 
       |There's also a solution video.
       |
       |Time to download the repository. The instructions are in the tests!
       |""".stripMargin))