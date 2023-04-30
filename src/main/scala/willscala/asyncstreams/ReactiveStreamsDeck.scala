package willscala.asyncstreams

import com.wbillingsley.veautiful.doctacular._
import willscala.Common._
import willscala.given

val reactiveStreamsDeck = DeckBuilder(1920, 1080)
  .markdownSlide(
    """
      |# Reactive Streams and Akka Streams
      |""".stripMargin).withClass("center middle")
  .markdownSlidex(
    """
      |## Back to the Futures...
      |
      |Previously, we've talked about `Future[T]`: at some point in the *future* this will contain a single `T`.
      |
      |But what if we want more than one `T`?  
      |eg, request all tweets with hashtag #scala
      |  
      |What do we return? `Future[Seq[Tweet]]`?  
      |that could be a pretty big Seq...
      |
      |---
      |
      |## What is the plural of `Future`?
      |
      |* `Future[Seq[T]]` would have to all become available at once
      |
      |* `Seq[Future[T]]` would imply we know how many there are going to be
      |
      |* `LazyList[Future[T]]` is problematic because we don't want to cache every previous value
      |
      |---
      |
      |## When we say stream...
      |
      |"Stream" might evoke the idea of a calm serene babbling brook.
      |
      |But imagine you're running a global video streaming service, and every time someone watches
      |a tv episode or a movie, you want to send an event to your analytics service.
      |
      |Your "stream" could look more like Niagara Falls.
      |
      |<img alt="Niagara Falls by Frederic Edwin Church" 
      |     src="https://upload.wikimedia.org/wikipedia/commons/f/fb/Frederic_Edwin_Church_-_Niagara_Falls%2C_from_the_American_Side_-_Google_Art_Project.jpg"
      |     height="600px" /> 
      |<br /> *Niagara Falls from the American Side, by Frederic Edwin Church*
      |
      |---
      |
      |## The Reactive Manifesto
      |
      |http://www.reactivemanifesto.org/
      |
      |> application requirements have changed dramatically in recent years. Only a few years ago a large application had tens of servers, 
      |> seconds of response time, hours of offline maintenance and gigabytes of data. 
      |> Today applications are deployed on everything from mobile devices to cloud-based clusters running thousands of multi-core processors. 
      |> Users expect millisecond response times and 100% uptime. Data is measured in Petabytes. 
      |> Today's demands are simply not met by yesterday's software architectures.
      |
      |> we want systems that are **Responsive**, **Resilient**, **Elastic** and **Message Driven**. We call these Reactive Systems.
      |
      |
      |---
      |
      |## Rivers can flood...
      |
      |Every now and then, Australia suffers floods. The rate of water molecules flowing *into* a catchment is
      |faster than the rate that the rivers can process them *out* of the catchment.
      |
      |In an asynchronous system, we could see something similar happen
      |
      |* Suppose we make a request for data to a really fast back-end API, over the network
      |
      |* ...it might start writing out data faster than we can process it...
      |
      |* ...in which case we're going to need to buffer or drop the data...
      |
      |* ...but our buffers could get bigger and bigger until we run out of memory...
      |
      |* *CRASH!*
      |
      |---
      |
      |### Throw me some rope!
      |
      |Imagine if I asked you to throw me some rope
      |
      |* *Maybe you've already thought of the cartoon where the poor hapless victim gets knocked over by a great coil of rope flung at him*
      |
      |Imagine instead I asked you to *feed* me some rope
      |
      |* *Now whenever I pull on the rope, you feed me some more. I control the rate at which the rope arrives.*
      |
      |---
      |
      |### Backpressure
      |
      |*Backpressure* in streaming APIs is the mechanism by which the subscriber tells the publisher it's ready to receive more data.
      |
      |Typically:
      |
      |* The subscriber asks the publisher *give me X more items*
      |
      |* The publisher writes them out and *stops until the subscriber asks for more*
      |
      |* Supply is never allowed to be greater than demand
      |
      |---
      |
      |### Java API
      |
      |Reactive streams was originally published as a library, but became the `java.util.concurrent.Flow` API in JDK 9
      |
      |Most methods in the interfaces return `void`. This is because it is a Java-style of asynchronicity. The publisher will call you back later...
      |
      |```java
      |interface Publisher<T> {
      |  void subscribe(Subscriber<? super T> s);
      |}
      |```
      |
      |---
      |
      |### Subscriber
      |
      |Once a subscriber has subscribed, the publisher will send it a `Subscription`
      |
      |```java
      |interface Subscriber<T> {
      |
      |  void onSubscription(Subscription s);
      |
      |  void onComplete();
      |  
      |  void onError(Throwable t);
      |  
      |  void onNext(T item);    
      |}
      |```
      |
      |---
      |
      |### Subscription
      |
      |The subscription is how the subscriber controls the backpressure
      |
      |```java
      |interafce Subscription {
      |
      |  void request(long n);
      |
      |  void cancel();
      |  
      |}
      |```
      |
      |---
      |
      |### An assembly line analogy
      |
      |We now have Publishers that can produce data, and Subscribers that can consume them. But often we want to do things to the data along the way. So we need something that can Subscribe to data, process it, and then Publish the processed version
      |
      |```java
      |interface Processor<T, R> extends Subscriber<T>, Publisher<R>
      |```
      |
      |---
      |
      |### Akka Streams
      |
      |Akka Streams is a streaming framework built in the Akka ecosystem. 
      |
      |It has its own API, based on the idea of describing the *graph* of how Sources connect to Sinks 
      |
      |* `Source[T, NotUsed]` represents a source of data
      |
      |* `Sink[T, NotUsed]` represents where data can flow to
      |
      |* `Flow[In, Out, NotUsed]` can sit in the middle
      |
      |(We'll come to what `NotUsed` is about later)
      |
      |This means we can *describe a flow without running it yet*
      |
      |---
      |
      |### Akka streams example
      |
      |This describes a computation on streams, but we haven't tied it to a sink, and we haven't started it running
      |
      |```scala
      |val authors: Source[Author, NotUsed] =
      |  tweets
      |    .filter(_.hashtags.contains(myTag))
      |    .map(_.author)
      |```
      |
      |---
      |
      |### Materialising streams
      |
      |* Attach it to a sink
      |
      |* "Materialise" it over an actor system. (Because it's got to be processed somewhere!)
      |
      |---
      |
      |### Materialisation example
      |
      |From the Akka docs
      |
      |```scala
      |val sumSink = Sink.fold[Int, Int](0)(_ + _)
      |val counterRunnableGraph: RunnableGraph[Future[Int]] =
      |  tweetsInMinuteFromNow
      |    .filter(_.hashtags contains akkaTag)
      |    .map(t => 1)
      |    .toMat(sumSink)(Keep.right)
      | 
      |// materialize the stream once in the morning
      |val morningTweetsCount: Future[Int] = counterRunnableGraph.run()
      |// and once in the evening, reusing the flow
      |val eveningTweetsCount: Future[Int] = counterRunnableGraph.run()
      |```
      |
      |---
      |
      |### `Keep.left`, `Keep.right`, and `NotUsed`
      |
      |Our stream represents sources of data and sinks of data. There are a lot of values flowing.
      |But when we run it, our Scala expression needs to resolve to a value. What value do we want?
      |
      |```scala
      |// Look, we said RunnableGraph[Future[Int]] - 
      |// we have to produce an Int at the end
      |val counterRunnableGraph: RunnableGraph[Future[Int]] = ???
      |```
      |
      |If you consider any particular junction in the network, we could ask ourselves:
      |
      |> Do we want to keep the value from the source on the left, the value from the sink on the right, or don't we care?
      |
      |Which gives us three possiblities: `Keep.left`, `Keep.right`, or `NotUsed`.
      |
      |So in this code
      |
      |```scala
      |val counterRunnableGraph: RunnableGraph[Future[Int]] =
      |  tweetsInMinuteFromNow
      |  .filter(_.hashtags contains akkaTag)
      |  .map(t => 1)
      |  .toMat(sumSink)(Keep.right)
      |```
      |
      |We had to `Keep.right` because we wanted to return the value from `sumSink` at the end (on the right)
      |""".stripMargin)
  .markdownSlide(willCcBy).withClass("bottom")
  .renderSlides
