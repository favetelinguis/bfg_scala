package com.bfg.infrastructure.betfair

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.stream.scaladsl.{BidiFlow, Flow, Keep, Sink, Source}
import akka.stream.testkit.scaladsl.{TestSink, TestSource}
import akka.stream.testkit.{TestPublisher, TestSubscriber}
import akka.testkit.TestKit
import akka.util.ByteString
import com.bfg.UnitSpec
import com.bfg.domain.model._
import com.bfg.infrastructure.betfair.subscriptionGraphState.SubscriptionGraphStage
import org.scalatest.concurrent.ScalaFutures
import org.scalatest._

/**
  * Created by henke on 2017-05-26.
  */
class SubscriptionGraphStageTest(_system: ActorSystem)
  extends TestKit(_system)
    with FlatSpecLike with Matchers
    with BeforeAndAfterAll
    with ScalaFutures
{
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(_system).withFuzzing(true))

  def this() = this(ActorSystem())

  "SubscriptionGraphStage" should "handle a successful subscription" in new Context {
    source.sendNext(connectionOK)
    val authenticationRequest = sink.requestNext()

    source.sendNext(authenticationOK)
    val subscriptionRequest = sink.requestNext()

    source.sendNext(subscriptionOK)
    sink.request(2)

    source.sendNext(heartbeat)
    source.sendNext(marketChange)
    source.sendNext(heartbeat)
    source.sendNext(marketChange)
    fromBetfairProbe.requestNext(marketChange)
    fromBetfairProbe.requestNext(marketChange)

    val i = 1
    //fromBetfairProbe.requestNext()
    //toBetfairProbe.re
  }
  it should "handle failures in subscription" in {}

  override def afterAll(): Unit = {
    materializer.shutdown()
    _system.terminate()
  }

  trait Context {
    // After materialization of TLS we get connection message
    val connectionOK = ConnectionMessage(op = "connection", connectionId = "050-250517141605-1391626")
    // After authentication message we get
    val authenticationOK = StatusMessage(op = "status", id = Some(1), statusCode = "SUCCESS", connectionClosed = false, errorCode = None, errorMessage = None, connectionId = None)
    // After MarketSubscription
    val subscriptionOK = StatusMessage(op = "status", id = Some(123), statusCode = "SUCCESS", connectionClosed = false, errorCode = None, errorMessage = None, connectionId = None)
    // After connection we get heartbeat
    val heartbeat = MarketChangeMessage(op = "mcm", id = 123, clk = "AAAAAAAA", pt = 1495773941868L, ct = Some("HEARTBEAT"), heartbeatMs = None, initialClk = None, mc = None, conflateMs = None, segmentType = None)
    // Finally we get MarketChangeMessage
    val marketChange = MarketChangeMessage(op = "mcm", id = 123, clk = "AAAAAAAA", pt = 1495773941868L, ct = Some("BOOM"), heartbeatMs = None, initialClk = None, mc = None, conflateMs = None, segmentType = None)

    val proxyStage = new SubscriptionGraphStage(SessionToken("fasdfasdf","SUCCESS"))
    val proxyFlow: BidiFlow[BetfairRequest, BetfairRequest, BetfairEvent, BetfairEvent, NotUsed] = BidiFlow.fromGraph(proxyStage)

    val fromBetfairProbe = TestSubscriber.probe[BetfairEvent]()
    val toBetfairProbe = TestPublisher.probe[BetfairRequest]()

    val transportFlow = Flow.fromSinkAndSource(
      Sink.fromSubscriber(fromBetfairProbe),
      Source.fromPublisher(toBetfairProbe))

    val flowUnderTest = proxyFlow.reversed.join(transportFlow)

    val (source, sink) = TestSource.probe[BetfairEvent]
      .via(flowUnderTest)
      .toMat(TestSink.probe[BetfairRequest])(Keep.both)
      .run()
  }
}
