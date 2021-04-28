package willscala

import willscala.templates.Markup
import com.wbillingsley.veautiful.html.{<, SVG, Styling, VHtmlNode, VHtmlComponent, ^}
import com.wbillingsley.veautiful.doctacular.VideoPlayer
import com.wbillingsley.veautiful.templates.DeckBuilder
import org.scalajs.dom.{Element, Node}

import scala.scalajs.js

/**
  * Common UI components to all the views
  */
object Common {

  extension (db:DeckBuilder)
    // Allows us to do many slides at once, separated by ---
    def markdownSlidex(s:String) =
      s.split("---").foldLeft(db)( (db, s) => db.markdownSlide(s) )

  val routes:Seq[(Route, String)] = Seq(
    IntroRoute -> "Hello world",
    ImperativeRoute(0, 0) -> "ThinkingAboutProgramming",
  )

  def linkToRoute(r:Route, s:String):VHtmlNode = <.a(
    ^.href := Router.path(r),
    ^.cls := (if (Router.route == r) "nav-link active" else "nav-link"),
    s
  )

  def leftMenu:VHtmlNode = <("nav")(^.cls := "d-none d-md-block bg-light sidebar",
    <.div(^.cls := "sidebar-sticky",
      <.ul(^.cls := "nav nav-pills flex-column",
        for { (r, t) <- routes } yield <.li(
          ^.cls := "nav-item",
          linkToRoute(r, t)
        )
      )
    )
  )

  def layout(ch:VHtmlNode) = shell(<.div(^.cls := "move-content-down",
    <.div(^.cls := "row",
      <.div(^.cls := "col-sm-3", leftMenu),
      <.div(^.cls := "col-sm-9", ch)
    )
  ))

  def shell(ch:VHtmlNode) = <.div(
    <("nav")(^.cls := "navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow",
      <.div(^.cls := "container",
        <.a(^.cls := "navbar-brand col-sm-3 col-md-2 mr-0", ^.href := "#", "")
      )
    ),

    <.div(^.cls := "container", ch)
  )

  def marked(text: => String) = Markup.marked.MarkupNode(() => text)

  /** Circuits Up! Logo */
  def symbol = {
    <.span()
  }

  def downloadFromGitHub(project:String, user:String="UNEcosc250"):VHtmlNode = {
    <.a(
      ^.cls := "btn btn-secondary",
      ^.href := s"https://github.com/$user/$project/archive/master.zip",
      ^.attr("aria-label") := s"Download $project as zip",
      <("i")(^.cls := "material-con", "cloud_download"), "Download"
    )
  }

  def downloadGitHubStr(project:String, user:String="UNEcosc250"):String = {
    s"<a href='https://github.com/$user/$project/archive/master.zip' aria-label='Download $project as zip'>Download the project as a zip file</i></a>"
  }

  def cloneGitHubStr(project:String, user:String="UNEcosc250"):String = {
    s"`git clone https://github.com/$user/$project.git`"
  }

  val chapterHeadingStyle = Styling(
    """
      |text-align: center;
      |margin: 100px;
      |""".stripMargin)
    .modifiedBy(
      " img" -> "height: 200px; margin: 50px;",
      " h1" -> "font-family: 'Playfair Design', serif; font-style: italic; font-size: 48px;",
      " .chap-num" -> "font-family: 'Playfair Design', serif; font-style: italic; font-size: 24px; color: #888; font-variant-numeric: oldstyle-nums;"
    )
    .register()

  def chapterHeading(number:Int, title:String, image:String) = <.div(^.cls := chapterHeadingStyle.className,
    <.div(<.img(^.src := image)),
    <.div(^.cls := "chap-num", s"Chapter $number"),
    <.h1(title)
  )
  
  
  val willCcBy:String =
    """
      |<p>Written by Will Billingsley</p>
      |
      |<a rel="license" href="http://creativecommons.org/licenses/by/3.0/au/">
      |  <img alt="Creative Commons Licence" style="border-width:0" src="https://i.creativecommons.org/l/by/3.0/au/88x31.png" /></a><br />
      |  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/au/">Creative Commons Attribution 3.0 Australia License</a>.
      |""".stripMargin


  case class Echo360Video(videoId:String)

  given VideoPlayer[Echo360Video] with
    extension (v:Echo360Video) def embeddedPlayer(width:Int, height:Int) =
    <.div(
      <("iframe")(
        ^.attr("height") := height, ^.attr("width") := width, ^.attr("allowfullscreen") := "true", ^.attr("frameborder") := "0",
        ^.src := s"https://echo360.org.au/media/${v.videoId}/public?autoplay=false&automute=false"
      )
    )

}

