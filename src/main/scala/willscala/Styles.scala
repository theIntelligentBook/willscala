package willscala

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular._
import com.wbillingsley.veautiful.templates.VSlides

given styleSuite:StyleSuite = StyleSuite()

object Styles {
  
  def installStyles():Unit = {
    site.pageLayout.leftSideBarStyle.addRules(Map(
      "" -> """
              |background: #373a3c;
              |color: rgb(186, 186, 186);
              |border: none;
              |""".stripMargin,
      " a" ->
        """color: rgb(186, 186, 186);
          |""".stripMargin,
    ))

    site.pageLayout.tocItemStyles(-1).addRules(Map(
      ".active" -> """
                     |background: #ffffff10;
                     |""".stripMargin,
      "" -> """
              |transition: background 0.25s;
              |""".stripMargin,
    ))

    site.pageLayout.sideBarToggleStyle.addRules(
      """
        |background: #373a3c;
        |border: none;
        |""".stripMargin)
    
    styleSuite.addGlobalRules(
      """@import url(http://fonts.googleapis.com/css?family=Fira+Mono|Fira+Sans|Lato);
        |
        |body {
        | font-family: 'Lato', sans-serif;
        |}
        |""".stripMargin)
    
    VSlides.defaultTheme.addRules(Map(
      "" -> "font-family: 'Lato', sans-serif;",
      " h1" -> "font-family: 'Fira Sans', sans-serif;",
      " h2" -> "font-family: 'Fira Sans', sans-serif;",
      " h3" -> "font-family: 'Fira Sans', sans-serif;",
      " h4" -> "font-family: 'Fira Sans', sans-serif;",
      " h5" -> "font-family: 'Fira Sans', sans-serif;",
      " code" -> "font-family: 'Fira Mono', monospace;",
      " pre" -> "font-family: 'Fira Mono', monospace;",
    ))
    
    styleSuite.install()
  }
  
}