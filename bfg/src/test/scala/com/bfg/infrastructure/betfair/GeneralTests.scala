package com.bfg.infrastructure.betfair

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep}
import akka.stream.testkit.scaladsl.{TestSink, TestSource}
import com.bfg.UnitSpec

/**
  * Created by henke on 2017-05-14.
  */
class GeneralTests extends UnitSpec {

  implicit val system = ActorSystem("Test-System")
  implicit val materializer = ActorMaterializer()

  case class Wid(id: Int, v: String)

  val flow = Flow[Wid]
    .map { s ⇒ println(Thread.currentThread().getName() + " ASYNC " + s); s }
    .scan("")((a: String, b: Wid) => a + b.v)
    .drop(1) // Drops the first empty value from scan
    .sliding(2, step = 1)

  "Parallel group-by with state" should "work" in {
    val (pub, sub) = TestSource.probe[Wid]
    .map { s ⇒ println(Thread.currentThread().getName() + " BEFORE " + s); s }
    .groupBy(Int.MaxValue, _.id)
      .via(flow).async
    .mergeSubstreams
    .map { s ⇒ println(Thread.currentThread().getName() + " AFTER " + s); s }
    .toMat(TestSink.probe[Seq[String]])(Keep.both)
    .run()

    sub.request(n = 4)
    pub.sendNext(Wid(1,"1"))
    pub.sendNext(Wid(2,"2"))
    pub.sendNext(Wid(1,"3"))
    println(sub.requestNext())
    pub.sendNext(Wid(2,"4"))
    println(sub.requestNext())
  }

  it should "List[Int] -> Int" in {
    val (pub, sub) = TestSource.probe[List[Int]]
      .mapConcat(identity)
      .toMat(TestSink.probe[Int])(Keep.both)
      .run()

    sub.request(3)
    pub.sendNext(List(1,2,3))
    sub.requestNext() shouldBe 1
    sub.requestNext() shouldBe 2
    sub.requestNext() shouldBe 3
  }
}
