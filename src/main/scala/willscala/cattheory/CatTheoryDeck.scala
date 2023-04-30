package willscala.cattheory

import com.wbillingsley.veautiful.doctacular.*
import willscala.Common._
import willscala.given

val cattheorydeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Typeclasses
      |
      |(And monoids)
    """.stripMargin).withClass("center middle")
  .markdownSlidex(
    """### Have you noticed...
      |
      |* `Option[T]` has `map[B](f: T => B):Option[B]`
      |* `List[T]` has `map[B](f: T => B):List[B]`
      |* `Future[T]` has `map[B](f: T => B):Future[B]`
      |
      |What's their relationship?
      |
      |---
      |
      |### Abstracting across type constructors
      |
      |* `Option[T]` has `map[B](f: T => B):Option[B]`
      |* `List[T]` has `map[B](f: T => B):List[B]`
      |* `Future[T]` has `map[B](f: T => B):Future[B]`
      |
      |Suppose we want to say
      |
      |* `F[T]` is a *Mappable* if there is `map[B](f: T => B):F[B]`
      |
      |---
      |
      |### Type Constructors
      |
      |* `List` isn't a complete type.
      |
      |```scala
      |def foo(l: List) = ??? // Compiler error: List takes type parameters
      |```
      |
      |* `List` is a *type constructor* -- if you give it another type, you get a type
      |
      |```scala
      |def foo(l: List[Int]) = ??? // happy.
      |```
      |
      |---
      |
      |### Kinds
      |
      |> Although
      |the first-order variant of generics is very useful, it also imposes
      |some restrictions: it is possible to abstract over a type,
      |but the resulting type constructor cannot be abstracted over.
      |This can lead to code duplication. We removed this restriction
      |in Scala, by allowing type constructors as type parameters
      |and abstract type members
      |
      |Moors, Piessens, & Odersky, *Generics of a Higher Kind*, OOPSLA 2008
      |
      |
      |---
      |
      |### Kinds (*important*)
      |
      |* If a *type* is an abstraction across values, a *kind* is an abstraction across types.
      |
      |  List[_] takes a single type parameter and has kind `(* -> *)`
      |  Map[A, B] takes two type parameters and has kind `(* -> * -> *)`
      |
      |* Remember, a *higher order function* was a function that took another function as a parameter
      |
      |* A *higher kinded type* is a type constructor that takes another type constructor as a parameter. For example:
      |
      |  `( (* -> *) -> *)`
      |
      |---
      |
      |### Odersky's example from *Generics of a Higher Kind*
      |
      |```scala
      |trait Iterable[T, Container[X]] {
      |  def filter(p: T => Boolean): Container[T]
      |  def remove(p: T => Boolean): Container[T] = filter (x => !p(x))
      |}
      |
      |trait List[T] extends Iterable[T, List]
      |```
      |
      |---
      |
      |### Back to map...
      |
      |We could conceptually do this:
      |
      |```scala
      |trait Mappable[T, F[_]] {
      |  def map[B](f: T => B):F[B]
      |}
      |
      |trait Option[T] extends Mappable[T, Option]
      |```
      |
      |In practice, Scala itself doesn't do that. But *scalaz*, *shapeless*, and other libraries do...
      |
      |---
      |
      |## Category theory
      |
      |* Some programming languages use terms like *Monad*, *Applicative*, *Functor* that come from a branch of mathematics called Category Theory.
      |
      |* Punchline: You've already been using monads since week 2.
      |
      |* Here's a quick recap from the perspective of a programmer who used to be good at maths but hasn't done it for a while...
      |
      |* The short answer: we don't usually "do" category theory, but we rely on it to know our programs compose well. *By the power of Category Theory, it works...*
      |
      |---
      |
      |### Time for a comic
      |
      |![XKCD hypotheticals](https://imgs.xkcd.com/comics/hypotheticals.png)
      |https://xkcd.com/248/
      |
      |---
      |
      |### What the blazes is category theory?
      |
      |--
      |
      |Once upon a time, mathematicians realised they were drawing a lot of node-and-arrow diagrams:
      |
      |<svg width="242pt" height="67pt" viewBox="0.00 0.00 242.00 67.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 63)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-63 239,-63 239,5 -4,5"></polygon>
      |<!-- A -->
      |<g id="node1" class="node"><title>A</title>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">A</text>
      |</g>
      |<!-- B -->
      |<g id="node2" class="node"><title>B</title>
      |<text text-anchor="middle" x="117" y="-36.8" font-family="Times,serif" font-size="14.00">B</text>
      |</g>
      |<!-- A&#45;&gt;B -->
      |<g id="edge2" class="edge"><title>A-&gt;B</title>
      |<path fill="none" stroke="black" d="M54.4029,-24.9008C62.481,-27.0121 71.5067,-29.3711 80.1046,-31.6182"></path>
      |<polygon fill="black" stroke="black" points="79.3589,-35.0409 89.919,-34.1834 81.1291,-28.2684 79.3589,-35.0409"></polygon>
      |</g>
      |<!-- C -->
      |<g id="node3" class="node"><title>C</title>
      |<text text-anchor="middle" x="207" y="-13.8" font-family="Times,serif" font-size="14.00">C</text>
      |</g>
      |<!-- A&#45;&gt;C -->
      |<g id="edge6" class="edge"><title>A-&gt;C</title>
      |<path fill="none" stroke="black" d="M54.2278,-15.9325C65.2648,-15.16 78.2491,-14.3787 90,-14 113.988,-13.227 120.012,-13.227 144,-14 152.354,-14.2692 161.332,-14.742 169.79,-15.2703"></path>
      |<polygon fill="black" stroke="black" points="169.562,-18.7629 179.772,-15.9325 170.026,-11.7782 169.562,-18.7629"></polygon>
      |</g>
      |<!-- B&#45;&gt;C -->
      |<g id="edge4" class="edge"><title>B-&gt;C</title>
      |<path fill="none" stroke="black" d="M144.403,-34.0992C152.481,-31.9879 161.507,-29.6289 170.105,-27.3818"></path>
      |<polygon fill="black" stroke="black" points="171.129,-30.7316 179.919,-24.8166 169.359,-23.9591 171.129,-30.7316"></polygon>
      |</g>
      |</g>
      |</svg>
      |
      |--
      |
      |so they decided
      |
      |*"let's work out the **maths of that sort of diagram**. Then we can apply everything we prove to any of the stuff we draw node-and-arrow diagrams for"*
      |
      |(so long as the arrows compose associatively)
      |
      |---
      |
      |### So...
      |
      |* If we can define our classes to fit into that sort of diagram
      |
      |* And make them follow "the rules" of that sort of diagram (generally about associativity)
      |
      |* Then by the power of mathematics, we can **know** those parts of our program are well-behaved.
      |
      |(Generally, that some higher order things like chaining map and flatMap calls together work like we think they should, even if we do long tangly chains of arbitrarily complex stuff.)
      |
      |---
      |
      |### Category theory drawings...
      |
      |<svg width="242pt" height="67pt" viewBox="0.00 0.00 242.00 67.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 63)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-63 239,-63 239,5 -4,5"></polygon>
      |<!-- A -->
      |<g id="node1" class="node"><title>A</title>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">A</text>
      |</g>
      |<!-- B -->
      |<g id="node2" class="node"><title>B</title>
      |<text text-anchor="middle" x="117" y="-36.8" font-family="Times,serif" font-size="14.00">B</text>
      |</g>
      |<!-- A&#45;&gt;B -->
      |<g id="edge2" class="edge"><title>A-&gt;B</title>
      |<path fill="none" stroke="black" d="M54.4029,-24.9008C62.481,-27.0121 71.5067,-29.3711 80.1046,-31.6182"></path>
      |<polygon fill="black" stroke="black" points="79.3589,-35.0409 89.919,-34.1834 81.1291,-28.2684 79.3589,-35.0409"></polygon>
      |</g>
      |<!-- C -->
      |<g id="node3" class="node"><title>C</title>
      |<text text-anchor="middle" x="207" y="-13.8" font-family="Times,serif" font-size="14.00">C</text>
      |</g>
      |<!-- A&#45;&gt;C -->
      |<g id="edge6" class="edge"><title>A-&gt;C</title>
      |<path fill="none" stroke="black" d="M54.2278,-15.9325C65.2648,-15.16 78.2491,-14.3787 90,-14 113.988,-13.227 120.012,-13.227 144,-14 152.354,-14.2692 161.332,-14.742 169.79,-15.2703"></path>
      |<polygon fill="black" stroke="black" points="169.562,-18.7629 179.772,-15.9325 170.026,-11.7782 169.562,-18.7629"></polygon>
      |</g>
      |<!-- B&#45;&gt;C -->
      |<g id="edge4" class="edge"><title>B-&gt;C</title>
      |<path fill="none" stroke="black" d="M144.403,-34.0992C152.481,-31.9879 161.507,-29.6289 170.105,-27.3818"></path>
      |<polygon fill="black" stroke="black" points="171.129,-30.7316 179.919,-24.8166 169.359,-23.9591 171.129,-30.7316"></polygon>
      |</g>
      |</g>
      |</svg>
      |
      |* Naked labels are *objects*
      |
      |* Arrows are *morphisms* (or *maps*)
      |
      |* Morphisms can be composed.
      |
      |  if there's an arrow from `A` to `B`, and an arrow from `B` to `C`, we can work out an arrow from `A` to `C`.
      |
      |* And it's associative so *f ∘ (g ∘ h)* = *(f ∘ g) ∘ h*
      |
      |---
      |
      |### Let's demonstrate function composition
      |
      |--
      |
      |* We'll make types (`Int`, `String`, etc) our *objects*
      |
      |* The functions are our arrows (*morphisms*)
      |
      |--
      |
      |<svg width="256pt" height="76pt" viewBox="0.00 0.00 256.00 76.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 72)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-72 253,-72 253,5 -4,5"></polygon>
      |<!-- A -->
      |<g id="node1" class="node"><title>A</title>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">A</text>
      |</g>
      |<!-- B -->
      |<g id="node2" class="node"><title>B</title>
      |<text text-anchor="middle" x="123" y="-45.8" font-family="Times,serif" font-size="14.00">B</text>
      |</g>
      |<!-- A&#45;&gt;B -->
      |<g id="edge2" class="edge"><title>A-&gt;B</title>
      |<path fill="none" stroke="black" d="M54.1842,-26.9138C64.1663,-30.3119 75.7181,-34.2445 86.4273,-37.8901"></path>
      |<polygon fill="black" stroke="black" points="85.3617,-41.2246 95.9562,-41.134 87.6176,-34.598 85.3617,-41.2246"></polygon>
      |<text text-anchor="middle" x="75" y="-38.2" font-family="Times,serif" font-size="14.00">f</text>
      |</g>
      |<!-- C -->
      |<g id="node3" class="node"><title>C</title>
      |<text text-anchor="middle" x="221" y="-13.8" font-family="Times,serif" font-size="14.00">C</text>
      |</g>
      |<!-- A&#45;&gt;C -->
      |<g id="edge6" class="edge"><title>A-&gt;C</title>
      |<path fill="none" stroke="black" d="M54.0188,-12.4052C66.6288,-9.971 82.0328,-7.40287 96,-6.2 119.911,-4.14071 126.084,-4.19831 150,-6.2 161.075,-7.12695 173.034,-8.8651 183.879,-10.7279"></path>
      |<polygon fill="black" stroke="black" points="183.409,-14.1994 193.871,-12.5297 184.651,-7.31054 183.409,-14.1994"></polygon>
      |<text text-anchor="middle" x="123" y="-11.2" font-family="Times,serif" font-size="14.00">g ∘ f</text>
      |</g>
      |<!-- B&#45;&gt;C -->
      |<g id="edge4" class="edge"><title>B-&gt;C</title>
      |<path fill="none" stroke="black" d="M150.232,-41.2559C160.709,-37.7636 172.941,-33.6862 184.211,-29.9296"></path>
      |<polygon fill="black" stroke="black" points="185.419,-33.2163 193.799,-26.7336 183.206,-26.5755 185.419,-33.2163"></polygon>
      |<text text-anchor="middle" x="172" y="-38.2" font-family="Times,serif" font-size="14.00">g</text>
      |</g>
      |</g>
      |</svg>
      |
      |--
      |
      |Let's call that the Category of types and functions.
      |
      |(We'll leave proving it's a valid Category as one of those things you "can do")
      |
      |---
      |
      |### Let's draw three Categories
      |
      |(We give them names, and put circles around them)
      |
      |--
      |
      |<svg width="206pt" height="44pt" viewBox="0.00 0.00 206.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 203,-40 203,5 -4,5"></polygon>
      |<!-- X -->
      |<g id="node1" class="node"><title>X</title>
      |<ellipse fill="none" stroke="black" cx="27" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">X</text>
      |</g>
      |<!-- Y -->
      |<g id="node2" class="node"><title>Y</title>
      |<ellipse fill="none" stroke="black" cx="99" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="99" y="-13.8" font-family="Times,serif" font-size="14.00">Y</text>
      |</g>
      |<!-- Z -->
      |<g id="node3" class="node"><title>Z</title>
      |<ellipse fill="none" stroke="black" cx="171" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="171" y="-13.8" font-family="Times,serif" font-size="14.00">Z</text>
      |</g>
      |</g>
      |</svg>
      |
      |--
      |
      |*That was easy!*
      |
      |--
      |
      |*But... oooh... what if we could treat those like nodes, and draw arrows between them?*
      |
      |---
      |
      |### Categories and Functors
      |
      |<svg width="260pt" height="44pt" viewBox="0.00 0.00 260.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 257,-40 257,5 -4,5"></polygon>
      |<!-- X -->
      |<g id="node1" class="node"><title>X</title>
      |<ellipse fill="none" stroke="black" cx="27" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">X</text>
      |</g>
      |<!-- Y -->
      |<g id="node2" class="node"><title>Y</title>
      |<ellipse fill="none" stroke="black" cx="125" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="125" y="-13.8" font-family="Times,serif" font-size="14.00">Y</text>
      |</g>
      |<!-- X&#45;&gt;Y -->
      |<g id="edge2" class="edge"><title>X-&gt;Y</title>
      |<path fill="none" stroke="black" d="M54.2324,-18C64.4975,-18 76.4478,-18 87.5271,-18"></path>
      |<polygon fill="black" stroke="black" points="87.7993,-21.5001 97.7993,-18 87.7993,-14.5001 87.7993,-21.5001"></polygon>
      |<text text-anchor="middle" x="76" y="-22.2" font-family="Times,serif" font-size="14.00">F</text>
      |</g>
      |<!-- Z -->
      |<g id="node3" class="node"><title>Z</title>
      |<ellipse fill="none" stroke="black" cx="225" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="225" y="-13.8" font-family="Times,serif" font-size="14.00">Z</text>
      |</g>
      |<!-- Y&#45;&gt;Z -->
      |<g id="edge4" class="edge"><title>Y-&gt;Z</title>
      |<path fill="none" stroke="black" d="M152.005,-18C162.969,-18 175.92,-18 187.794,-18"></path>
      |<polygon fill="black" stroke="black" points="187.874,-21.5001 197.874,-18 187.874,-14.5001 187.874,-21.5001"></polygon>
      |<text text-anchor="middle" x="175" y="-22.2" font-family="Times,serif" font-size="14.00">G</text>
      |</g>
      |</g>
      |</svg>
      |
      |--
      |
      |**Functors map a bunch of nodes and arrows in Category X into a bunch of nodes and arrows in Category Y**
      |
      |--
      |
      |For it to be a Functor, in Category Y, the mapped arrows still have to be well-behaved (compose, be associative, etc) -- the Functor can't "break stuff".
      |
      |---
      |
      |### A Functor from X to X?
      |
      |*Let's be playful ... what if we draw an arrow from category X to category X?*
      |
      |--
      |
      |No problem. It's called an *endofunctor*
      |
      |<svg width="62pt" height="79pt" viewBox="0.00 0.00 62.00 78.80" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 74.8)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-74.8 59,-74.8 59,5 -4,5"></polygon>
      |<!-- X -->
      |<g id="node1" class="node"><title>X</title>
      |<ellipse fill="none" stroke="black" cx="27" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">X</text>
      |</g>
      |<!-- X&#45;&gt;X -->
      |<g id="edge2" class="edge"><title>X-&gt;X</title>
      |<path fill="none" stroke="black" d="M17.4312,-35.0373C15.4784,-44.8579 18.668,-54 27,-54 32.2075,-54 35.4063,-50.4289 36.5963,-45.3529"></path>
      |<polygon fill="black" stroke="black" points="40.0955,-45.0279 36.5688,-35.0373 33.0955,-45.0466 40.0955,-45.0279"></polygon>
      |<text text-anchor="middle" x="27" y="-58.2" font-family="Times,serif" font-size="14.00">F</text>
      |</g>
      |</g>
      |</svg>
      |
      |A fancy word for a functor from a category to the same category
      |
      |--
      |
      |But note, it might change a set of objects and arrows in that category to a set of *different* objects and arrows in that category.
      |
      |---
      |
      |### Endofunctors
      |
      |Actually, in programming that's pretty handy, because we're usually working in "the category of types and functions".
      |
      |Let's call our category `Scala` for Scala types and functions
      |
      |<svg width="62pt" height="79pt" viewBox="0.00 0.00 62.00 78.80" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 74.8)">
      |<title>_anonymous_0</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-74.8 59,-74.8 59,5 -4,5"></polygon>
      |<!-- X -->
      |<g id="node1" class="node"><title>X</title>
      |<ellipse fill="none" stroke="black" cx="27" cy="-18" rx="27" ry="18"></ellipse>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">Scala</text>
      |</g>
      |<!-- X&#45;&gt;X -->
      |<g id="edge2" class="edge"><title>X-&gt;X</title>
      |<path fill="none" stroke="black" d="M17.4312,-35.0373C15.4784,-44.8579 18.668,-54 27,-54 32.2075,-54 35.4063,-50.4289 36.5963,-45.3529"></path>
      |<polygon fill="black" stroke="black" points="40.0955,-45.0279 36.5688,-35.0373 33.0955,-45.0466 40.0955,-45.0279"></polygon>
      |<text text-anchor="middle" x="27" y="-58.2" font-family="Times,serif" font-size="14.00">F</text>
      |</g>
      |</g>
      |</svg>
      |
      |Since we're not leaving Scala, the functors we care about are endofunctors.
      |
      |And what we care about is that these Functors are going to transform the arrows between objects (functions between types) to arrows between different objects (functions between different types!)
      |
      |---
      |
      |### Here's one you've seen earlier
      |
      |`List` is a Functor
      |
      |--
      |
      |Let's go from this:
      |
      |<svg width="198pt" height="44pt" viewBox="0.00 0.00 198.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>top</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 195,-40 195,5 -4,5"></polygon>
      |<!-- Int -->
      |<g id="node1" class="node"><title>A</title>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">A</text>
      |</g>
      |<!-- String -->
      |<g id="node2" class="node"><title>B</title>
      |<text text-anchor="middle" x="163" y="-13.8" font-family="Times,serif" font-size="14.00">B</text>
      |</g>
      |<!-- Int&#45;&gt;String -->
      |<g id="edge2" class="edge"><title>Int-&gt;String</title>
      |<path fill="none" stroke="black" d="M54.1547,-18C74.5377,-18 103.077,-18 125.677,-18"></path>
      |<polygon fill="black" stroke="black" points="125.911,-21.5001 135.911,-18 125.911,-14.5001 125.911,-21.5001"></polygon>
      |<text text-anchor="middle" x="95" y="-22.2" font-family="Times,serif" font-size="14.00">f</text>
      |</g>
      |</g>
      |</svg>
      |
      |to this:
      |
      |<svg width="190pt" height="44pt" viewBox="0.00 0.00 190.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>top</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 187,-40 187,5 -4,5"></polygon>
      |<!-- Int -->
      |<g id="node1" class="node"><title>Int</title>
      |<text text-anchor="middle" x="32" y="-13.8" font-family="Times,serif" font-size="14.00">List[A]</text>
      |</g>
      |<!-- String -->
      |<g id="node2" class="node"><title>String</title>
      |<text text-anchor="middle" x="141" y="-13.8" font-family="Times,serif" font-size="14.00">List[B]</text>
      |</g>
      |<!-- Int&#45;&gt;String -->
      |<g id="edge2" class="edge"><title>Int-&gt;String</title>
      |<path fill="none" stroke="black" d="M63.3757,-18C71.6356,-18 80.8061,-18 89.8485,-18"></path>
      |<polygon fill="black" stroke="black" points="89.9479,-21.5001 99.9479,-18 89.9479,-14.5001 89.9479,-21.5001"></polygon>
      |</g>
      |</g>
      |</svg>
      |
      |And remember, the objects in this category are **types** (eg, `Int`) not values (eg, `1`)
      |
      |---
      |
      |<svg width="198pt" height="44pt" viewBox="0.00 0.00 198.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>top</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 195,-40 195,5 -4,5"></polygon>
      |<!-- Int -->
      |<g id="node1" class="node"><title>A</title>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">A</text>
      |</g>
      |<!-- String -->
      |<g id="node2" class="node"><title>B</title>
      |<text text-anchor="middle" x="163" y="-13.8" font-family="Times,serif" font-size="14.00">B</text>
      |</g>
      |<!-- Int&#45;&gt;String -->
      |<g id="edge2" class="edge"><title>Int-&gt;String</title>
      |<path fill="none" stroke="black" d="M54.1547,-18C74.5377,-18 103.077,-18 125.677,-18"></path>
      |<polygon fill="black" stroke="black" points="125.911,-21.5001 135.911,-18 125.911,-14.5001 125.911,-21.5001"></polygon>
      |<text text-anchor="middle" x="95" y="-22.2" font-family="Times,serif" font-size="14.00">f</text>
      |</g>
      |</g>
      |</svg>
      |&nbsp;
      |<svg width="190pt" height="44pt" viewBox="0.00 0.00 190.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>top</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 187,-40 187,5 -4,5"></polygon>
      |<!-- Int -->
      |<g id="node1" class="node"><title>Int</title>
      |<text text-anchor="middle" x="32" y="-13.8" font-family="Times,serif" font-size="14.00">List[A]</text>
      |</g>
      |<!-- String -->
      |<g id="node2" class="node"><title>String</title>
      |<text text-anchor="middle" x="141" y="-13.8" font-family="Times,serif" font-size="14.00">List[B]</text>
      |</g>
      |<!-- Int&#45;&gt;String -->
      |<g id="edge2" class="edge"><title>Int-&gt;String</title>
      |<path fill="none" stroke="black" d="M63.3757,-18C71.6356,-18 80.8061,-18 89.8485,-18"></path>
      |<polygon fill="black" stroke="black" points="89.9479,-21.5001 99.9479,-18 89.9479,-14.5001 89.9479,-21.5001"></polygon>
      |</g>
      |</g>
      |</svg>
      |
      |
      |* Morphing the nodes is just the type constructor itself
      |
      |  `Int` is `Int`, `List[Int]` is `List[Int]`
      |
      |  Remember, `List` isn't a type. `List[Int]` is a type. `List[String]` is a type. `List` is a "type constructor" (it needs to be a list *of some type*).
      |
      |--
      |
      |* Morphing the arrows needs (`T` &rArr; `B`) &rArr; (`List[T]` &rArr; `List[B]`).
      |
      |  `List.map` does that
      |
      |  `1.toString` goes from `Int` to `String`
      |
      |  `List(1).map(_.toString)` goes from `List[Int]` to `List[String]`
      |
      |---
      |
      |### Let's do another one...
      |
      |<svg width="198pt" height="44pt" viewBox="0.00 0.00 198.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>top</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 195,-40 195,5 -4,5"></polygon>
      |<!-- Int -->
      |<g id="node1" class="node"><title>A</title>
      |<text text-anchor="middle" x="27" y="-13.8" font-family="Times,serif" font-size="14.00">A</text>
      |</g>
      |<!-- String -->
      |<g id="node2" class="node"><title>B</title>
      |<text text-anchor="middle" x="163" y="-13.8" font-family="Times,serif" font-size="14.00">B</text>
      |</g>
      |<!-- Int&#45;&gt;String -->
      |<g id="edge2" class="edge"><title>Int-&gt;String</title>
      |<path fill="none" stroke="black" d="M54.1547,-18C74.5377,-18 103.077,-18 125.677,-18"></path>
      |<polygon fill="black" stroke="black" points="125.911,-21.5001 135.911,-18 125.911,-14.5001 125.911,-21.5001"></polygon>
      |<text text-anchor="middle" x="95" y="-22.2" font-family="Times,serif" font-size="14.00">f</text>
      |</g>
      |</g>
      |</svg>
      |&nbsp;
      |<svg width="190pt" height="44pt" viewBox="0.00 0.00 190.00 44.00" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      |<g id="graph1" class="graph" transform="scale(1 1) rotate(0) translate(4 40)">
      |<title>top</title>
      |<polygon fill="white" stroke="white" points="-4,5 -4,-40 187,-40 187,5 -4,5"></polygon>
      |<!-- Int -->
      |<g id="node1" class="node"><title>Int</title>
      |<text text-anchor="middle" x="32" y="-13.8" font-family="Times,serif" font-size="14.00">Future[A]</text>
      |</g>
      |<!-- String -->
      |<g id="node2" class="node"><title>String</title>
      |<text text-anchor="middle" x="141" y="-13.8" font-family="Times,serif" font-size="14.00">Future[B]</text>
      |</g>
      |<!-- Int&#45;&gt;String -->
      |<g id="edge2" class="edge"><title>Int-&gt;String</title>
      |<path fill="none" stroke="black" d="M63.3757,-18C71.6356,-18 80.8061,-18 89.8485,-18"></path>
      |<polygon fill="black" stroke="black" points="89.9479,-21.5001 99.9479,-18 89.9479,-14.5001 89.9479,-21.5001"></polygon>
      |</g>
      |</g>
      |</svg>
      |
      |--
      |
      |* Morphing the nodes is the `Future` type constructor
      |
      |  `Int` is `Int`; `Future[Int]` is `Future[Int]`
      |
      |* Morphing the arrows is `map` again:
      |
      |  `1.toString` goes from `Int` to `String`
      |
      |  `Future.successful(1).map(_.toString)` goes from `Future[Int]` to `Future[String]`
      |
      |
      |---
      |
      |### So in Scala terms...
      |
      |If class `F[T]` has
      |
      |* `F.map[B](f:T=>B):F[B]`
      |
      |then it's a Functor
      |
      |examples: `List`, `Set`, `Seq`, `Option`, `Future`, `Try`, ...
      |
      |---
      |
      |### Whoa...
      |
      |* This isn't subclassing. There is no type called `Functor` in Scala's standard library<sup>1</sup>.
      |
      |* This is a way we can think about properties of *otherwise very different* classes and know they are well-behaved. `List` and `Future` are very different... but they have `map` and are well-behaved
      |
      |  ```scala
      |  // This
      |  List(1, 2, 3).map(double).map(triple)
      |
      |  // Is the same as
      |  List(1, 2, 3).map(double.compose(triple))
      |  ```
      |
      |*<sup>1</sup> if you want a library where Functor really is a type, try scalaz*
      |
      |---
      |
      |### Let's go a bit further
      |
      |* Having a well-behaved `map` function was what made `List[T]` a Functor.
      |
      |* Having a well-behaved `flatMap` too means it's also a `Monad`
      |
      |---
      |
      |## Monad
      |
      |Roughly, there's four functions that Monads have:
      |
      |* A unit function, for getting a value into the monad `T` &rArr; `F[T]`.
      |  eg, `List.apply` or `Future.successful`
      |* **map**  (`T` &rArr; `B`) &rArr; (`F[T]` &rArr; `F[B]`)
      |* **flatMap**  (`T` &rArr; `F[B]`) &rArr; (`F[T]` &rArr; `F[B]`)
      |* **flatten**  `F[F[T]]` &rArr; `F[T]`
      |
      |--
      |
      |But you can derive flatten from flatMap and the identity function
      |
      |```
      |val l = List(List(1), List(2, 3))
      |
      |l.flatMap { x:List[Int] => x }  // List(1, 2, 3)
      |```
      |
      |(or can derive flatMap from map and flatten, or map from flatMap and the unit function)
      |
      |---
      |
      |## Monad laws
      |
      |Monads have to obey three laws. These aren't about type signatures, but about how it behaves...
      |
      |Let's call the "unit function" `apply` for these examples, so we can just write `F(a)` to get a monad...
      |
      |---
      |
      |### Left identity
      |
      |```scala
      |F(a).flatMap(f)
      |```
      |
      |is equivalent to
      |
      |```scala
      |f(a)
      |```
      |
      |--
      |
      |eg:
      |```scala
      |List(a).flatMap({ x => List(x + 1) }) == List(a + 1)
      |```
      |
      |---
      |
      |### Right identity
      |
      |```scala
      |F(a).flatMap(F.apply)
      |```
      |
      |is equivalent to
      |
      |```scala
      |F(a)
      |```
      |
      |eg:
      |```scala
      |Option(1).flatMap(Option.apply) == Option(1)
      |```
      |
      |---
      |
      |### Associativity
      |
      |```scala
      |F(a).flatMap(f).flatMap(g)
      |```
      |
      |is equivalent to
      |
      |```scala
      |F(a).flatMap({ x => f(x).flatMap(g) })
      |```
      |
      |--
      |eg:
      |
      |```scala
      |val f:Int => List[Int] = { x => List(x, x + 1) }
      |val g:Int => List[Int] = { x => List(x, x * 2) }
      |List(1, 2, 3).flatMap(f).flatMap(g)
      |List(1, 2, 3).flatMap({ x:Int => f(x).flatMap(g) })
      |```
      |
      |---
      |
      |### What's it all mean?...
      |
      |For Monads, it means they compose nicely. Suppose I have five functions that all return `Future[String]`.
      |
      |```scala
      |for {
      |  a <- asyncTask1()
      |  b <- asyncTask2()
      |  c <- asyncTask3()
      |  d <- asyncTask4()
      |  e <- asyncTask5()
      |} yield e
      |```
      |
      |Those asynchronous tasks will be executed *in order* -- ie, asyncTask2 won't be called until asyncTask1 has completed successfully.
      |
      |And that is true *no matter* how deeply or how complicatedly I nest asyncTasks.
      |
      |---
      |
      |### In other languages...
      |
      |Haskell is a pure language (**no** side-effects allowed).
      |
      |But even printing something is an effect. How do you do println?
      |
      |--
      |
      |Haskell has an `IO` monad.
      |
      |
      |
      |---
      |
      |### Haskell's IO monad
      |
      |```hs
      |do a <- readLn
      |   print a
      |```
      |
      |--
      |
      |* `readLn` doesn't return a String. It returns (effectively) `IO[String]`. The effect is in the return type
      |
      |--
      |
      |* From `IO[String]` there is **no way** to convert it just to a String. So *any* chain of function calls that does IO, however deeply, has an `IO[T]` type. You can't hide the IO effect.
      |
      |--
      |
      |* To chain IO operations together, you use `bind` (Haskell's name for `flatMap`). `do` in the example above is syntactic sugar for calling `bind` -- similar to Scala's `for` notation.
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
