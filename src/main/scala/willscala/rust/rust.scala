package willscala.rust

import com.wbillingsley.veautiful.doctacular._
import willscala.Common._
import willscala.given

val rustDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Rust
      |""".stripMargin).withClass("center middle")
  .markdownSlides(
    """
      |## What's the big idea
      |
      |Rust is a relatively strongly typed **imperative** language that compiles to native code
      |
      |* No runtime
      |
      |* Memory safety without garbage collection
      |
      |But to do this
      |
      |* A new concept around **ownership** and **lifetimes** of data
      |
      |The syntax might feel like a cross between Scala, Python, and C/C++
      |
      |We know a few programming languages by now, so we'll just do a little syntax by example
      |
      |---
      |
      |### Syntax
      |
      |```rust
      |fn main() {
      |    let x = 5; // Can't be modified
      |    let mut y = 6; // Can be modified
      |    y = 7; 
      |}
      |```
      |
      |* `fn` is to declare a function/method
      |* `let` is like `val` in Scala
      |* `let mut` is like `val` in Scala
      |* Like Scala, it'll do type inference if you don't declare the type
      |
      |---
      |
      |### Syntax
      |
      |```rust
      |struct Rectangle {
      |    width: u32,
      |    height: u32,
      |}
      |
      |impl Rectangle {
      |    fn area(&self) -> u32 {
      |        self.width * self.height
      |    }
      |}
      |```
      |
      |* `struct` to declare a structure
      |* Like Scala, type annotations are `: type`
      |* `->` instead of `=` for showing the type a function evaluates to
      |* methods take `self` as the first argument
      |* but it's `&self` - a reference to self
      |
      |---
      |
      |## Memory management
      |
      |Rust's method of doing automatic memory management is that when a reference type variable 
      |goes out of scope, its memory is freed (unless something else has taken ownership)
      |
      |```rust
      |fun main() {
      |
      |  {  
      |    let a = "I'm a variable"  
      |  } // the memory for a is freed here
      |}
      |```
      |
      |---
      |
      |## An unusual implication
      |
      |That has an unusual implication. If we point two variables to the same value,
      |the language has to have a mechanism to avoid freeing the memory of the value *twice*
      |
      |```rust
      |fun main() {
      |
      |  {  
      |    let a = "I'm a variable"  
      |    let b = a
      |  } // a and b both go out of scope here
      |}
      |```
      |
      |---
      |
      |## An unusual implication
      |
      |To solve this, if we assign a variable using an ordinary assignment, it takes over the ownership.
      |The first value becomes invalid.
      |
      |```rust
      |fun main() {
      |
      |  {  
      |    let a = "I'm a variable"  
      |    let b = a // variable a is no longer valid. b now has the value.
      |  } 
      |}
      |```
      |
      |---
      |
      |## References
      |
      |If we don't want to take ownership (and b's scope to be the lifetime of the memory), we can just take
      |a *reference* to a
      |
      |```rust
      |fun main() {
      |
      |  {  
      |    let a = "I'm a variable"  
      |    let b = &a // b is just a reference to a. It hasn't taken ownership
      |  } 
      |}
      |```
      |
      |---
      |
      |## References
      |
      |References come in two kinds:
      |
      |* **immutable** references, e.g.  
      |  <code>let b = callAFunction(&a)</code>
      |
      |* **mutable references**, e.g. 
      |  <code>let b = callAFunction(&mut a)</code>
      |
      |Note, though, that if you have a **mutable** reference, you cannot simultaneously have any other references
      |to the same variable
      |
      |---
      |
      |## Tying that back in to the unit
      |
      |Going back to our theme that programming languages need to deal with the thorny problem of shared mutable state
      |
      |* Functional programming: let's avoid *mutable* state wherever possible
      |
      |* Concurrency oriented programming: let's avoid *shared* state wherever possible
      |
      |* Rust: Rules against sharing *mutable* state
      |
      |---
      |
      |### Consequences
      |
      |The distinction between ownership of a variable and a reference to a variable has some consquences
      |
      |* There are, for instance, two string types
      |
      |  - `String` if you own the string
      |  - `&str` if you want to reference a string or sequence of characters within it (`&[u8]`)
      |
      |* The same is true of vectors
      |
      |  - `Vec<T>` is a growable vector of some type `T` (a little like `mutable.Buffer` in Scala or `ArrayList` in Java)
      |  - `&[T]` can refer to a slice of it
      |
      |* Occasionally, you will find you have written code that type-checks, except for lifetimes and ownership.
      |
      |  - You can find yourself chasing around to find the combination of turning things into references (prepend `&`) and de-references (append `*`)
      |    that makes the compiler happy
      |
      |  - Passsing ownership and passing it back can also work. e.g.  
      |    <code>a = takesOwnershipButReturnsItAgain(a)</code>
      |
      |---
      |
      |## Coming from a Scala background
      |
      |* Rust doesn't have a lot of the features of Scala. It is more low-level so there are times you'll feel something is missing
      |
      |* Ownership rules mean it can somteimes feel simpler doing something imperatively in a local block. Functional styles create functions and parameter passing
      |  and that means thinking about how ownership transfers. (The cognitive overhead of creating more functions is higher than in Scala.)
      |
      |* In Scala, we're used to the referentially transparent idea that putting an expression into a `val` for clarity shouldn't the behaviour. 
      |  In Rust, though, it can change the *lifetime* of the reference
      |
      |  ```rust
      |  callSomeFunction(&mut my_value)
      |  ```
      |
      |  vs
      |
      |  ```rust
      |  let mut_ref = &mut my_value; // mut_ref's scope is going to continue to the end of the block, 
      |                               // even if you're only using it in the next call
      |  callSomeFunction(mut_ref)
      |  ```
      |
      |  Though note that you can create a block just with curly braces to address this.
      |
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides

