package willscala.laziness

import com.wbillingsley.veautiful.html.{<, ^, unique}
import willscala.Common.{Echo360Video, chapterHeading, marked}
import willscala.templates.{FrontPage, Markup}
import willscala.topics.{FunctionalProgramming, HigherOrder, ImperativeProgramming}

val lazyIntro = unique(<.div(
  chapterHeading(7, "Laziness", "images/lazy.jpg"),
  marked("""Why put off until tomorrow what you might never have to do at all?
           |
           |Put that way, the answer is obvious. If there's a chance you might *never* need to do something, then
           |doing it straight away might be a waste of resources. That sounds like a good reason to put it off.
           |In human terms, we call this laziness and it's common to argue about whether laziness can be constructive
           |or not.
           |
           |Many programming languages that you may be familiar with are *strict*. If you write down an expression, it
           |is evaluated immediately. Some, however, are *lazy*. If you write down an expression, they'll remember it's
           |an expression, but they won't get around to evaluating it until you use the result.
           |
           |Scala is mostly a strict language. But it does have a couple of ways in which we can write lazy
           |expressions: `lazy val`s and *by-name* arguments. In this chapter, we're going to see how a little
           |constructive laziness can let us write programs that are about infinite streams (which we can't evaluate
           |strictly because it would take forever).
           |""".stripMargin),
))