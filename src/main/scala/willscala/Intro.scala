package willscala

import com.wbillingsley.veautiful.html.{<, ^}

val frontPage = <.div(
  <.div(
    <.img(^.src := "images/willscala.jpg", ^.alt := "The Adventures of Will Scala and his Merry Programs"),
    <.div(^.cls := "abs-bottom-right white-translucent-bg",
      Common.marked("[Will Billingsley](https://www.wbillingsley.com)'s Scala course")
    )
  ),
  <.div(^.cls := "lead",
    Common.marked(
      """
        | This is an experimental attempt to put up a public site around [one of my teaching courses](https://handbook.une.edu.au/units/2021/COSC250).
        | The course teaches Scala, functional programming, and some asynchronous and reactive programming. It's designed for second year
        | undergraduate students, who mostly have experience of imperative languages (Python and Java). 
        | 
        | Right now, I'm altering the course for Scala 3 and altering the site to use Doctacular, so this site may 
        | appear a litle bare.
        |""".stripMargin
    ),
  ),
  Seq(
  )
)
