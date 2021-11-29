package willscala.higherOrder

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}
import willscala.given

import scala.annotation.tailrec

sealed trait Symbol

enum Nationality extends Symbol:
  case Englishman, Swede, Dane, German, Norwegian

enum Drink extends Symbol:
  case Tea, Water, Coffee, Milk, Beer

enum Colour extends Symbol:
  case Red, Blue, White, Green, Yellow

enum Smoke extends Symbol:
  case Dunhill, PallMall, Blend, BlueMasters, Prince

enum Pet extends Symbol:
  case Bird, Cat, Dog, Horse, Fish

val allSymbols = (Nationality.values ++ Drink.values ++ Colour.values ++ Smoke.values ++ Pet.values).toSeq

type Cell = Seq[Symbol]

case class House(cells: Seq[Cell]):

  def solved = cells.forall(_.length == 1)

  def countPossibilities(s:Symbol) = cells.count(_.contains(s))

  /** Check whether one of this house's cells still contains a symbol as a possibility */
  def couldContain(s:Symbol) = cells.exists(_.contains(s))

  /** Creates a copy of this house, where the given cell only contains one value */
  def withJust(row:Int, s:Symbol) = House(cells.updated(row, Seq(s)))

  /** Creates a copy of this house, where the given value is removed from the given cell */
  def eliminate(row:Int, s:Symbol) = House(cells.updated(row, cells(row).filterNot(_ == s)))

end House

object House:
  def unknown = House(Seq(Nationality.values.toSeq, Drink.values.toSeq, Colour.values.toSeq, Smoke.values.toSeq, Pet.values.toSeq))

type Move = (Int, Int, Symbol)

case class Street(houses:Seq[House] = Seq.fill(5)(House.unknown)):

  def solved = houses.forall(_.solved)

  def countPossibilities(s:Symbol) = houses.map(_.countPossibilities(s)).sum

  def inSequence(pair:(Symbol, Symbol)):Boolean =
    val (left, right) = pair
    val indices = (0 until 4).zip(1 until 5)
    indices.exists((i, j) => houses(i).couldContain(left) && houses(j).couldContain(right))

  /** The constraints broken by this street */
  def breaks:Option[Prop] = propositions.find((_, prop) => !prop(this))

  /** Returns a street, eliminating a possibility */
  def eliminate(move:Move) =
    val (h, r, v) = move
    Street(houses.updated(h, houses(h).eliminate(r, v)))

  /** Returns a street, applying a possibility */
  def applyMove(move:Move) =
    val (h, r, v) = move
    Street(
      for
        (house, num) <- houses.zipWithIndex
      yield
        if num == h then house.withJust(r, v) else house.eliminate(r, v)
    )

  /** Every possible move (setting a value in a cell that is not already solved) */
  def possibleMoves:Iterator[Move] =
    for
      i <- houses.indices.iterator
      j <- houses(i).cells.indices.iterator if houses(i).cells(j).length > 1
      v <- houses(i).cells(j).iterator
    yield (i, j, v)

  def legalMoves:Iterator[(Move, Street)] =
    for
      m <- possibleMoves
      s = applyMove(m) if s.breaks.isEmpty
    yield (m, s)

  /** Every illegal move */
  def illegalMoves:Iterator[(Move, Prop)] =
    for
      m <- possibleMoves
      prop <- applyMove(m).breaks
    yield (m, prop)

  /** If there is an illegal move, the street you get from eliminating that possibility */
  def next:Option[(Prop, Move, Street)] =
    for
      (m, p) <- illegalMoves.nextOption()
    yield (p, m, eliminate(m))

  def eliminateAll(i:Iterator[Move]):Street =
    i.foldLeft(this)((s, mp) => s.eliminate(mp))

  def eliminateAllIllegalMoves:Street =
    eliminateAll(illegalMoves.map(_._1))

  /** Makes any obvious moves */
  @tailrec final def fastForward:Street =
    val e = eliminateAllIllegalMoves
    if e.illegalMoves.isEmpty then e else e.fastForward

  /** A dead end is a move where, if we prune all the illegal moves, we are then left with none */
  def deadEnds:Iterator[Move] =
    for
      (m, s) <- legalMoves
      ff = s.fastForward if !ff.solved && ff.legalMoves.isEmpty
    yield m


end Street


/** A proposition is a tuple containing a name and test to run on a Street */
type Prop = (String, Street => Boolean)

/** All 17 propositions in the puzzle */
val propositions:Seq[Prop] = Seq(

  "All values must appear once" -> { (street) =>
    allSymbols.forall((s) => street.countPossibilities(s) >= 1)
  },

  "All cells have a value and no value appears more than once" -> { (street) =>
    street.houses.forall((h) => h.cells.forall(_.length >= 1))
  },

  "Englishman lives in the Red house" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.Englishman) && h.couldContain(Colour.Red))
  },

  "Swede keeps dogs" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.Swede) && h.couldContain(Pet.Dog))
  },

  "Dane drinks tea" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.Dane) && h.couldContain(Drink.Tea))
  },

  "Green house to left of white house" -> { (street) =>
    street.inSequence((Colour.Green, Colour.White))
  },

  "Owner of green house drinks coffee" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Colour.Green) && h.couldContain(Drink.Coffee))
  },

  "Pall Mall smoker keeps birds" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Smoke.PallMall) && h.couldContain(Pet.Bird))
  },

  "Owner of yellow house smoke Dunhill" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Colour.Yellow) && h.couldContain(Smoke.Dunhill))
  },

  "Man in centre house drinks milk" -> { (street) => street.houses(2).couldContain(Drink.Milk) },

  "Norwegian lives in the first house" -> { (street) => street.houses(0).couldContain(Nationality.Norwegian) },

  "Blend smoker has a neighbour who keeps cats" -> { (street) =>
    street.inSequence((Smoke.Blend, Pet.Cat)) || street.inSequence((Pet.Cat, Smoke.Blend))
  },

  "Man who smokes BlueMasters drinks beer" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Smoke.BlueMasters) && h.couldContain(Drink.Beer))
  },

  "Man who keeps horses lives next to the Dunhill smoker" -> { (street) =>
    street.inSequence((Pet.Horse, Smoke.Dunhill)) || street.inSequence((Smoke.Dunhill, Pet.Horse))
  },

  "German smokes Prince" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.German) && h.couldContain(Smoke.Prince))
  },

  "Norwegian lives next to blue house" -> { (street) =>
    street.inSequence((Nationality.Norwegian, Colour.Blue)) || street.inSequence((Colour.Blue, Nationality.Norwegian))
  },

  "Blend smoker has a neighbour who drinks water" -> { (street) =>
    street.inSequence((Smoke.Blend, Drink.Water)) || street.inSequence((Drink.Water, Smoke.Blend))
  },

)

val sideBySideStyling = Styling(
  """display: grid;
    |grid-template-columns: auto, 40ch;
    |column-gap: 1rem;
    |""".stripMargin).register()

val streetTableStyling = Styling(
  "background: #eee; width: inherit;"
).modifiedBy(
  " td" ->
    """margin-right: 20px;
      |border-right: 20px solid white;
      |padding: 5px;
      |text-align: center;
      |font-size: 18px;
      |max-width: 12rem;
      |""".stripMargin,
  " tr:nth-child(even)" -> "background: #ddd;"
).register()

def sideBySide(a:VHtmlNode)(b:VHtmlNode) = <.div(^.cls := sideBySideStyling.className, a, b)

val stackPanelStyling = Styling(
  "height: 100%; position: relative; top: 0; width: 20ch;"
).modifiedBy(
  " .frames" -> "margin-top: auto; margin-bottom: 0; width: 100%;",
  " .frame:first-child" -> "background-color: antiquewhite;",
  " .frame" -> "border-top: 1px solid #aaa; width: 20ch; margin: 0;"
).register()


def renderStreet(s:Street):VHtmlNode = <.table(^.cls := streetTableStyling.className,
  for r <- 0 until 5 yield <.tr(
    for h <- 0 until 5 yield <.td(
      s.houses(h).cells(r).mkString(", ")
    )
  )
)


class StreetWidget(s:Street) extends VHtmlComponent {

  var street = s

  def ud(s:Street) = {
    this.street = s
    rerender()
  }

  def render = <.div(^.cls := sideBySideStyling.className,
    renderStreet(street),
    <.div(
      if street.solved then {
        <.span(
          "Solved! ",
          for
          h <- street.houses.find(_.couldContain(Pet.Fish))
            yield s"The ${h.cells(0).head} owns the fish."
        )
      } else
        street.next match {
          case Some((prop, m, ss)) =>
            val (message, _) = prop
            val (h, _, v) = m
            <.div(
              <.div(message, s". House $h cannot contain $v."),
              <.button(^.onClick --> ud(ss), ">"),
              <.button(^.onClick --> ud(ss.fastForward), ">>"),
            )
          case None =>
            for
            m <- street.deadEnds.nextOption()
              yield {
                val (h, _, v) = m
                <.div(
                  <.div(s"If house $h contained $v, it'd lead to a dead end"),
                  <.button(^.onClick --> ud(street.eliminate(m)), "Eliminate this")
                )
              }
        }
    )
  )


}