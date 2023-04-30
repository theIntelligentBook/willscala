package willscala

import com.wbillingsley.veautiful.html.{<, SVG, Styling, VHtmlContent, VHtmlElement, DHtmlComponent, ^, Markup}
import com.wbillingsley.veautiful.doctacular.{VideoPlayer, DeckBuilder}
import org.scalajs.dom.{Element, Node}

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSImport("marked", "marked")
object Marked extends js.Object:
  def parse(s:String):String = js.native

given markdown:Markup = new Markup({ (s:String) => Marked.parse(s).asInstanceOf[String] })

@js.native
@JSImport("mermaid", JSImport.Default)
object Mermaid extends js.Object:
  def initialize(config:js.Object):String = js.native
  def mermaidAPI:js.Dynamic = js.native


object MermaidDiagram {
  private var initialized = false

  def initialize():Unit =
    if !initialized then
      Mermaid.initialize(js.Object("startOnLoad" -> false))
      initialized = true
 
    ()

  def render(s:String, callback:String => Unit) = 
    Mermaid.mermaidAPI.render("achart", s, callback)
}

case class MermaidDiagram(source:String) extends VHtmlElement {

  var _domNode:Option[org.scalajs.dom.html.Element] = None
  def domNode = _domNode

  private val el = <.div().build().create()

  override def beforeAttach() = {
    MermaidDiagram.initialize()
    MermaidDiagram.render(source, (s:String) => {
      el.innerHTML = s
    })
  }

  override def attach() = {
    _domNode = Some(el)
    el
  }
  override def detach() = _domNode = None

}


/**
  * Common UI components to all the views
  */
object Common {

  extension (db:DeckBuilder)
    // Allows us to do many slides at once, separated by ---
    def markdownSlidex(s:String) =
      s.split("---").foldLeft(db)( (db, s) => db.markdownSlide(s) )


  def marked(text: => String) = markdown.div(text)

  /** Circuits Up! Logo */
  def symbol = {
    <.span()
  }

  def downloadFromGitHub(project:String, user:String="UNEcosc250"):VHtmlContent = {
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

