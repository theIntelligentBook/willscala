package willscala.livedemo

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.templates._
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}

import <._
import ^._

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

val allSymbols:Seq[Symbol] = (Nationality.values ++ Drink.values ++ Colour.values ++ Smoke.values ++ Pet.values).toSeq

type Cell = Seq[Symbol]

case class House(cells:Seq[Cell]):
  
  def withJust(row:Int, symbol:Symbol):House =
    House(cells = cells.updated(row, Seq(symbol)))
    
  def eliminate(row:Int, symbol:Symbol):House =
    House(cells = cells.updated(row, cells(row).filterNot(_ == symbol)))

  def couldContain(symbol:Symbol):Boolean = cells.exists(_.contains(symbol))

object House:
  def unknown:House = House(Seq(
    Nationality.values.toSeq,
    Drink.values.toSeq,
    Colour.values.toSeq,
    Smoke.values.toSeq,
    Pet.values.toSeq
  ))

type Move = (Int, Int, Symbol)

type Prop = (String, Street => Boolean)

case class Street(houses:Seq[House] = Seq.fill(5)(House.unknown)):

  @tailrec
  final def solve:Street =
    next match
      case Some((brokenRule, move, nextS)) => 
        buffer.append(li(
          s"Because $brokenRule we can eliminate $move giving us:",
          streetHtml(nextS)
        ))
        nextS.solve
      case None => this

  def next:Option[(String, Move, Street)] =
    for 
      (move, (brokenRule, _)) <- illegalMoves.nextOption()
    yield (brokenRule, move, eliminateMove(move:Move))
  
  def illegalMoves:Iterator[(Move, Prop)] =
    for
      m <- possibleMoves
      prop <- applyMove(m).breaks 
    yield (m, prop)
  
  def breaks:Option[Prop] = propositions.find((name, rule) => !rule(this))
  
  def countPossibilities(s:Symbol):Int =
    houses.filter(_.couldContain(s)).length

  def applyMove(m:Move):Street =
    val (h, r, symbol) = m
    Street(
      for 
        (house, num) <- houses.zipWithIndex
      yield
        if num == h then house.withJust(r, symbol) else house.eliminate(r, symbol)
    )
  
  def eliminateMove(move:Move):Street =
    val (h, r, symbol) = move
    Street(houses.updated(h, houses(h).eliminate(r, symbol)))
  
  def possibleMoves:Iterator[Move] =
    for 
      i <- houses.indices.iterator
      j <- houses(i).cells.indices.iterator if houses(i).cells(j).length > 1
      v <- houses(i).cells(j).iterator
    yield (i, j, v)

  def inSequence(tuple:(Symbol, Symbol)):Boolean = { 
    val (left, right) = tuple
    val indices = (0 until 4).zip(1 until 5)
    indices.exists((l, r) => houses(l).couldContain(left) && houses(r).couldContain(right))
  }

val propositions:Seq[Prop] = Seq(
  "Norwegian lives in the first house" -> { (street) => street.houses(0).couldContain(Nationality.Norwegian) },

  "The man in the middle house drinks milk" -> { (street) => street.houses(2).couldContain(Drink.Milk) },

  "Englishman lives in the Red house" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.Englishman) && h.couldContain(Colour.Red))
  },

  "Swede keeps dogs" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.Swede) && h.couldContain(Pet.Dog))
  },

  "Dane drinks tea" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.Dane) && h.couldContain(Drink.Tea))
  },

  "The owner of the green house drinks coffee" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Colour.Green) && h.couldContain(Drink.Coffee))
  },

  "The owner of the yellow house smokes Dunhills" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Colour.Yellow) && h.couldContain(Smoke.Dunhill))
  },

  "The man who smokes BlueMasters drinks Beer" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Smoke.BlueMasters) && h.couldContain(Drink.Beer))
  },

  "The German smokes Princes" -> { (street) =>
    street.houses.exists((h) => h.couldContain(Nationality.German) && h.couldContain(Smoke.Prince))
  },
  
  "The green house is just to the left of the white house" -> { (street) => street.inSequence((Colour.Green, Colour.White))},
  
  "The Blends smoker has a neighbour who has a cat" -> { (street) => 
    street.inSequence((Smoke.Blend, Pet.Cat)) || street.inSequence((Pet.Cat, Smoke.Blend))
  },

  "The man who keeps horses lives next to the man who smokes Dunhills" -> { (street) =>
    street.inSequence((Pet.Horse, Smoke.Dunhill)) || street.inSequence((Smoke.Dunhill, Pet.Horse))
  },

  "The Norwegian lives next to the blue house" -> { (street) =>
    street.inSequence((Nationality.Norwegian, Colour.Blue)) || street.inSequence((Colour.Blue, Nationality.Norwegian))
  },

  "The Blends smoker has a neighbour who drinks water" -> { (street) =>
    street.inSequence((Smoke.Blend, Drink.Water)) || street.inSequence((Drink.Water, Smoke.Blend))
  },

  "All values must appear once" -> { (street) =>
    allSymbols.forall((s) => street.countPossibilities(s) >= 1)
  },

  "All cells have a value and no value appears more than once" -> { (street) =>
    street.houses.forall((h) => h.cells.forall(_.length >= 1))
  },

)



def streetHtml(street:Street):VHtmlNode = table(
  for row <- 0 to 4 yield tr(style := "border-bottom: 1px solid #aaa;",
    for house <- street.houses yield td(house.cells(row).mkString(", "))
  )
)


import scala.collection.mutable
val buffer:mutable.Buffer[VHtmlNode] = mutable.Buffer.empty


def einsteinDemo = unique(div(
  h2("Live-coded solution to Einstein's riddle"),
  p("We'll build up the solution here"),
  
  p("Our symbols:"),
  ul(
    for s <- allSymbols yield li(s.toString)
  ),
  
  h3("Our unknown street:"),
  streetHtml(Street()),

  h3("Next one to eliminate"),
  ul(
    (for (broken, move, result) <- Street().next yield li(
      s"$broken so we can eliminate $move giving us",
      streetHtml(result)
    )).toSeq
  ),

  h3("Eventual found solution was"),
  streetHtml(Street().solve),

  h3("And here's the working we wrote out along the way"),
  ul(
    buffer.toSeq
  )
  
))

