package willscala

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular._
import com.wbillingsley.veautiful.templates.VSlides

given styleSuite:StyleSuite = StyleSuite()

object Styles {
  
  val imageSlide = Styling(
    """position: relative;
      |top: 0;
      |width: calc(100% + 100px);
      |height: calc(100% + 100px);
      |margin: -50px;
      |""".stripMargin
  ).modifiedBy(
    ".cover" -> "object-fit: cover;",
    " figcaption" ->
      """font-size: 3rem;
        |position: absolute;
        |bottom: 10px;
        |left: 10px;
        |background: rgba(255, 255, 255, 0.7);
        |padding: 5px;
        |border-radius: 5px;
        |""".stripMargin
  ).register()
  
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
      """@import url(https://fonts.googleapis.com/css?family=Fira+Mono|Fira+Sans|Lato|Playfair+Display:ital@1);
        |
        |body {
        |  font-family: 'Lato', sans-serif;
        |}
        |
        |h1, h2, h3, h4, h5 {
        |  font-family: 'Playfair Design', serif;  margin-top: 2rem;
        |}
        |""".stripMargin)
    
    VSlides.defaultTheme.addRules(Map(
      "" -> "font-family: 'Lato', sans-serif; font-size: 30px;",
      " td" -> "font-family: 'Lato', sans-serif; font-size: 30px;",
      " h1" -> "font-family: 'Playfair Design', serif; font-size: 60px; font-style: italic; color: #5a074f;",
      " h2" -> "font-family: 'Playfair Design', serif; font-size: 48px; font-style: italic; color: #5a074f;",
      " h3" -> "font-family: 'Playfair Design', serif; font-size: 36px; font-style: italic; color: #5a074f;",
      " h4" -> "font-family: 'Playfair Design', serif; font-style: italic; color: #5a074f;",
      " h5" -> "font-family: 'Playfair Design', serif; font-style: italic; color: #5a074f;",
      " code" -> "font-family: 'Fira Mono', monospace;",
      " pre" -> "font-family: 'Fira Mono', monospace;",
      " .bottom" -> "margin-top: auto;"
    ))
    
    styleSuite.install()
  }
  
}