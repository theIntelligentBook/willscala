package willscala

import com.wbillingsley.veautiful.html.<

object Intro {

  def page = Common.layout(<.div(
    <.h1("Circuits Up!"),
    <.p(
      """
        | This is going to grow into a suite of outreach materials that introduce students to computer architecture,
        | from circuits up...
      """.stripMargin)
  ))

}
