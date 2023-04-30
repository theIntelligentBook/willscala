package willscala.higherOrder

import com.wbillingsley.veautiful.html._
import com.wbillingsley.veautiful.doctacular.*
import willscala.styleSuite
import willscala.Common
import willscala.Common.{marked, willCcBy}
import willscala.given


val einsteinDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Einstein's Puzzle, using higher order functions
    """.stripMargin).withClass("center middle")
  .markdownSlide(
    """## A logic puzzle
      |
      |There is a riddle, that's variously been attributed to Albert Einstein, Lewis Carroll, and others. There are 
      |many variations, but one of them goes:
      |
      |> In my street, there are five houses.
      |The Englishman lives in the red house.
      |> The Swede keeps dogs.
      |> The Dane drinks tea.
      |> The green house is to the left of the white house.
      |> The owner of the green house drinks coffee.
      |> The owner of the yellow house smokes Dunhills.
      |> The man in the middle house drinks milk.
      |> The Norwegian lives in the first house.
      |> The Blends smoker has a neighbour who has a cat.
      |> The man who smokes BlueMasters drinks beer.
      |> The man who keeps horses lives next to the man who smokes Dunhills.
      |> The German smokes Princes.
      |> The Norwegian lives next to the blue house.
      |> The Blends smoker has a neighbour who drinks water.
      |>
      |> So, who owns the fish?
      |
      |We're going to solve it using higher order functions.
      | 
      |This is a complex example, probably much more complex than the ones in the exercises, but hopefully it gives us
      |a chance to put some concepts together and solve something bigger (in a demo at least).
      |""".stripMargin
  )
  .veautifulSlide(<.div(
    marked(
      """## The street
        |
        |We're going to model what each house *could* contain. So, at the start, a model street might look like this:
        |""".stripMargin),
    renderStreet(Street())
  ))
  .veautifulSlide(<.div(
    marked(
      """## The solver
        |
        |Let's show you the solver in action, then let's show you how it was made with higher order functions
        |""".stripMargin),
    StreetWidget(Street())
  ))
  .markdownSlide(
    """## The symbols
      |
      |Let's start by defining `enum`s for each of the possible symbols. We've also given them a common trait
      |`Symbol` that they all implement.
      |
      |```scala
      |sealed trait Symbol
      |enum Nationality extends Symbol:
      |  case Englishman, Swede, Dane, German, Norwegian
      |
      |enum Drink extends Symbol:
      |  case Tea, Water, Coffee, Milk, Beer
      |
      |enum Colour extends Symbol:
      |  case Red, Blue, White, Green, Yellow
      |
      |enum Smoke extends Symbol:
      |  case Dunhill, PallMall, Blend, BlueMasters, Prince
      |
      |enum Pet extends Symbol:
      |  case Bird, Cat, Dog, Horse, Fish
      |```
      |
      |The common trait means we cal also say:
      |
      |```scala
      |val allSymbols:Seq[Symbol] = (Nationality.values ++ Drink.values ++ Colour.values ++ Smoke.values ++ Pet.values).toSeq
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """## A cell and a house
      |
      |We're showing the puzzle as a table, and a cell contains a sequence of `Symbol`s (the possible values for that entry) 
      |so let's define a *type alias*:
      |
      |```scala
      |type Cell = Seq[Symbol]
      |```
      |
      |Then, let's define a house as a case class, containing a sequence of cells (for the nationality, drink, colour, smoke, and pet).
      |
      |```scala
      |case class House(cells: Seq[Cell])
      |```
      |
      |An unknown house, then, starts out with every possibility:
      |
      |```scala
      |object House:
      |  def unknown = House(Seq(Nationality.values.toSeq, Drink.values.toSeq, Colour.values.toSeq, Smoke.values.toSeq, Pet.values.toSeq))
      |```
      |
      |And let's define a `Street` as a sequence of `House`s. They all, by default, start unknown
      |
      |```scala
      |case class Street(houses:Seq[House] = Seq.fill(5)(House.unknown))
      |```
      |
      |""".stripMargin
  )
  .veautifulSlide(<.div(
    marked(
      """## The starting street
        |
        |Now, a starting street, `Street()` should look like this:
        |""".stripMargin),
    renderStreet(Street())
  ))
  .markdownSlide(
    """## Moves
      |
      |Let's model a `Move` as setting a cell to a particular value. We'll need to know the house, row, and symbol.
      |
      |```scala
      |type Move = (Int, Int, Symbol)
      |```
      |
      |What we've used is a *type alias*. We haven't created any new types here - a move is just a tuple 
      |`(Int, Int, Symbol)`. We've just given ourselves an *alias* to refer to that type by.
      |
      |For instance, this would be a `Move` setting the occupant of the leftmost house to be the Norwegian:
      |
      |```scala
      |val m = (0, 0, Nationality.Norwegian)
      |```
      |
      |""".stripMargin)
  .markdownSlide(
    """## Applying a move
      |
      |We'd like to be able to see what a street would look like if we applied a particular move. So, let's 
      |define a method on `Street`
      |
      |```scala
      |case class Street(houses:Seq[House] = Seq.fill(5)(House.unknown)):
      |
      |  def applyMove(m:Move):Street = ???
      |```
      |
      |Well, a street is made up of houses, so we're going to want to process the houses:
      |
      |* For the house we're setting the value in, set the value
      |* For the houses we're *not* setting the value in, remove the value
      |
      |```scala
      |  /** Returns a street, applying a possibility */
      |  def applyMove(move:Move):Street =
      |    val (h, r, v) = move
      |    Street(
      |      for
      |        (house, num) <- houses.zipWithIndex
      |      yield
      |        if num == h then house.withJust(r, v) else house.eliminate(r, v)
      |    )
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## `House.withJust` and `House.eliminate`
      |
      |We're going to need to define those methods on `House`.
      |
      |```scala
      |case class House(cells: Seq[Cell]):
      |
      |  /** Creates a copy of this house, where the given cell only contains one value */
      |  def withJust(row:Int, s:Symbol) = House(cells.updated(row, Seq(s)))
      |
      |  /** Creates a copy of this house, where the given value is removed from the given cell */
      |  def eliminate(row:Int, s:Symbol) = House(cells.updated(row, cells(row).filterNot(_ == s)))
      |```
      |
      |""".stripMargin
  )
  .veautifulSlide(<.div(
    marked("""## Considering hypothetical streets
      |
      |That now gives us enough machinery to model what a hypothetical street would look like after a move.
      |For instance `Street().applyMove((0, 0, Nationality.Englishman))` should produce:
      |
      |""".stripMargin),
    renderStreet(Street().applyMove((0, 0, Nationality.Englishman)))
  ))
  .markdownSlide(
    """## All possible moves...
      |
      |The way we're going to try solving this is we're going to consider every possible "move" (assigning a value to
      |a cell) that we could make. If it would break a constraint, we know we can eliminate that possibility.
      |
      |So for instance, if we try to make the move `(0, 2, Colour.white)`, setting the leftmost house white, we should 
      |break the constraint:
      |
      |> The green house is to the left of the white house
      |
      |So, as we now know setting the first house to white will definitely break a constraint, we can eliminate it from
      |the possibilites for the first house.
      |
      |""".stripMargin)
  .markdownSlide(
    """## All the possible moves
      |
      |To be a little more efficient, I've used an `Iterator` to get the moves. This is something that works very much
      |like Java iterators, but Scala being Scala, we can use our maps and for-comprehensions on it.
      |
      |```scala
      |  def possibleMoves:Iterator[Move] =
      |    for
      |      i <- houses.indices.iterator
      |      j <- houses(i).cells.indices.iterator if houses(i).cells(j).length > 1
      |      v <- houses(i).cells(j).iterator
      |    yield (i, j, v)
      |```
      |
      |Next, we need to work out which of those moves are legal. For that, we'll need to model the propositions in the
      |riddle.
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Modelling the propositions
      |
      |The street we produced with the Englishman in the first house isn't valid, because one of the statements was that
      |the *Norwegian* lives in the first house.
      |
      |Let's start modelling some propositions. We'll keep the English language statement, and a function to apply to
      |a Street to test if it could be true.
      |
      |```scala
      |/** A proposition is a tuple containing a name and test to run on a Street */
      |type Prop = (String, Street => Boolean)
      |```
      |
      |Again, this is a *type alias*. We haven't created a new type - it is still a tuple - we've just given ourselves
      |a more readable alias to refer to it by,
      |
      |""".stripMargin)
  .markdownSlide(
    """## Our first proposition
      |
      |Because our grid contains the *possible* values for that cell, we're going to need propositions that look like
      |this:
      |
      |```scala
      |"Norwegian lives in the first house" -> { (street) => street.houses(0).couldContain(Nationality.Norwegian) },
      |```
      |
      |So let's define `House.couldContain` - is `Norwegian` one of the symbols that's included in that cell?
      |
      |```scala
      |case class House(cells: Seq[Cell]):
      |  
      |  /** Check whether one of this house's cells still contains a symbol as a possibility */
      |  def couldContain(s:Symbol) = cells.exists(_.contains(s))
      |```
      |
      |i.e. *Does there exist a cell within this house, which still contains the symbol you gave me?*
      |""".stripMargin)
  .markdownSlide(
    """## The Englishman lives in the Red house
      |
      |There are a lot of statements in the riddle that go along the lines of *the Englishman lives in the red house*, or
      |*the Swede keeps dogs*. For these constraints, we need to check there's a house that still could contain
      |both symbols.
      |
      |```scala
      |  "Englishman lives in the Red house" -> { (street) =>
      |    street.houses.exists((h) => h.couldContain(Nationality.Englishman) && h.couldContain(Colour.Red))
      |  },
      |```
      |""".stripMargin)
  .markdownSlide(
    """## Orders
      |
      |There are a couple of sentences that go *the green house is just to the left of the white house*. For these,
      |we're going to need to check two symbols could appear in order somewhere across the houses.
      |
      |```scala
      |  "Green house to left of white house" -> { (street) =>
      |    street.inSequence((Colour.Green, Colour.White))
      |  },
      |```
      |
      |We're going to need a new method on `Street`. We'll take advantage of `zip`, to look through a sequence of pairs:
      |`(0, 1)`, `(1, 2)`, `(2, 3)`, and `(3, 4)`:
      |
      |```scala
      |case class Street(houses:Seq[House] = Seq.fill(5)(House.unknown)):
      |
      |  def inSequence(pair:(Symbol, Symbol)):Boolean =
      |    val (left, right) = pair
      |    val indices = (0 until 4).zip(1 until 5)
      |    indices.exists((i, j) => houses(i).couldContain(left) && houses(j).couldContain(right))
      |```
      |""".stripMargin)
  .markdownSlide(
    """## Neighbours
      |
      |There are a few proposition where we're told about someone's neighbour, but not which side. We can test that just
      |by checking for both orders:
      |
      |```scala
      |  "Norwegian lives next to blue house" -> { (street) =>
      |    street.inSequence((Nationality.Norwegian, Colour.Blue)) || street.inSequence((Colour.Blue, Nationality.Norwegian))
      |  },
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Two last unstated propositions...
      |
      |Let's add two proposition that *aren't stated* but are assumed - eventually, every symbol appears exactly once and
      |every cell has a value.
      |
      |```scala
      |  "All values must appear once" -> { (street) =>
      |    allSymbols.forall((s) => street.countPossibilities(s) >= 1)
      |  },
      |
      |  "All cells have a value and no value appears more than once" -> { (street) =>
      |    street.houses.forall((h) => h.cells.forall(_.length >= 1))
      |  },
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## A list of propositions
      |
      |Let's now put our propositions in a big sequence:
      |
      |```scala
      |val propositions:Seq[Prop] = Seq(
      |
      |  "All values must appear once" -> { (street) =>
      |    allSymbols.forall((s) => street.countPossibilities(s) >= 1)
      |  },
      |
      |  "All cells have a value and no value appears more than once" -> { (street) =>
      |    street.houses.forall((h) => h.cells.forall(_.length >= 1))
      |  },
      |
      |  "Englishman lives in the Red house" -> { (street) =>
      |    street.houses.exists((h) => h.couldContain(Nationality.Englishman) && h.couldContain(Colour.Red))
      |  },
      |  
      |  //etc
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Applying the rules
      |
      |A proposition is a tuple, `(String, Street => Boolean)`
      | 
      |So to see if a proposition is broken, we can
      |
      |```scala
      |val (name, rule) = prop
      |prop(street) // true if it holds, false if it's broken
      |```
      |
      |We have a list of propositions, so let's find the first one that breaks. `find` is a higher order function that
      |works much like `filter`, except it returns an `Option`: `Some(item)` if it found one, `None` if it didn't.
      |
      |```scala
      |case class Street(houses:Seq[House]):
      |
      |  /** The first constraint, if any, broken by this street */
      |  def breaks:Option[Prop] = propositions.find((_, prop) => !prop(this))
      |```
      |
      """.stripMargin)
  .markdownSlide(
    """## Finding the illegal moves
      |
      |Now, what we've got is:
      |
      |* A starting condition
      |* A function that'll give us an *iterator* on every "move" (value setting)
      |* A set of constraints to check on the streets we'd have if we set those values.
      |
      |Let's put those together and find all the *illegal* moves to eliminate:
      |
      |```scala
      |  /** Every illegal move */
      |  def illegalMoves:Iterator[(Move, Prop)] =
      |    for
      |      m <- possibleMoves
      |      prop <- applyMove(m).breaks
      |    yield (m, prop)
      |```scala
      |
      |""".stripMargin)
  .markdownSlide(
    """## Our process of elimination
      |
      |For any street, let's just find the "next" illegal move. That is, the first one from the iterator.
      |And let's eliminate it, and return the street it produces.
      |
      |So, we're going to produce `Some((brokenRule, illegalMove, resultOfEliminatingIt))`, if there is one. 
      |
      |```scala
      |  def next:Option[(String, Move, Street)] =
      |    for
      |      (move, (ruleName, _)) <- illegalMoves.nextOption()
      |    yield (ruleName, move, this.eliminate(move))
      |```
      |
      |We'll also need to define `eliminate` on a street, to give us a street without that value. It's 
      |
      |```scala
      |  def eliminate(move:Move):Street =  
      |    val (h, r, symbol) = move
      |    Street(houses.updated(h, houses(h).eliminate(r, symbol))
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(
    """## Our process of elimination...
      |
      |We could do the elimination imperatively
      |
      |```scala
      |var street:Street = Street()
      |var done = false
      |while !done do
      |  street.next match 
      |    case Some((brokenRule, move, afterElimination)) =>
      |      street = afterElimination 
      |    case _ => done = true
      |```
      |
      |Or we could do it tail recursively
      |
      |```scala
      |@tailrec
      |def solve(s:Street):Street = 
      |  s.next match 
      |    case Some(next) => solve(next)
      |    case None => s
      |```
      |
      |""".stripMargin
  )
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides