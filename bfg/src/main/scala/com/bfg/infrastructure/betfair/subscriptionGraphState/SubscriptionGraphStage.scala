package com.bfg.infrastructure.betfair.subscriptionGraphState

import akka.NotUsed
import akka.stream._
import akka.stream.TLSProtocol.{SslTlsInbound, SslTlsOutbound}
import akka.stream.javadsl.Tcp.OutgoingConnection
import akka.stream.scaladsl.{BidiFlow, Flow}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.util.ByteString
import com.bfg.domain.model._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

/**
  * Created by henke on 2017-05-28.
  */
class SubscriptionGraphStage(token: SessionToken)
  extends GraphStage[BidiShape[BetfairRequest, BetfairRequest, BetfairEvent, MarketChangeMessage]]
    with LazyLogging {

  import com.bfg.infrastructure.betfair.subscriptionGraphState.SubscriptionStates._

  val bytesIn: Inlet[BetfairRequest] = Inlet("OutgoingTCP.in")

  val bytesOut: Outlet[MarketChangeMessage] = Outlet("OutgoingTCP.out")
  val sslIn: Inlet[BetfairEvent] = Inlet("OutgoingSSL.in")
  val sslOut: Outlet[BetfairRequest] = Outlet("OutgoingSSL.out")

  override def shape: BidiShape[BetfairRequest, BetfairRequest, BetfairEvent, MarketChangeMessage] = BidiShape.apply(bytesIn, sslOut, sslIn, bytesOut)

  val authMessage = RequestAuthentication(session = token.sessionToken, id = 1, appKey = "tDwhr80fJKsOW725")
  val marketSubscription: BetfairRequest = RequestMarketSubscription(
    id=123,
    marketFilter = MarketSubscriptionMarketFilter(
      eventTypeIds = List("7"),
      marketTypes = List("WIN"),
      countryCodes = List("GB")
    ),
    marketDataFilter = MarketSubscriptionMarketDataFilter(
      fields = List("EX_ALL_OFFERS","EX_MARKET_DEF"),
      ladderLevels = Some(3)
    )
  )
  def connectionValid(event: BetfairEvent): Boolean = event match {
    case m: ConnectionMessage => true
    case _ => false
  }

  def authenticationValid(event: BetfairEvent): Boolean = event match {
    //todo simplifed need to control id
    case m: StatusMessage if m.statusCode == "SUCCESS" => true
    case _ => false
  }
  def subscriptionValid(event: BetfairEvent): Boolean = event match {
    //todo simplifed need to control id
    case m: StatusMessage if m.statusCode == "SUCCESS" => true
    case _ => false
  }

  def heatbeatValid(event: BetfairEvent): Boolean = event match {
    case m: MarketChangeMessage if m.ct == Some("HEARTBEAT") => true
    case _ => false
  }

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    private var state: State = Connecting

    // I1
    setHandler(sslIn, new InHandler {
      override def onPush() = {
        state match {
          case Connecting =>
            val event = grab(sslIn)
            if (connectionValid(event)) {
              state = Authenticating
              logger.info(state.toString)
              push(sslOut, authMessage)
            } else {
              failStage(new BetfairConnectionFailedException(s"Connection to Betfair failed"))
            }
          case Authenticating =>
            val event = grab(sslIn)
            if (authenticationValid(event)) {
              state = Subscribing
              logger.info(state.toString)
              push(sslOut, marketSubscription)
            } else {
              failStage(new BetfairAuthenticationFailedException(s"Authentication with Betfair failed"))
            }
          case Subscribing =>
            val event = grab(sslIn)
            if (subscriptionValid(event)) {
              state = Subscribed
              //if (isAvailable(sslIn)) {
              //  pull(sslIn)
              //}
              pull(sslIn) // This was the critical part to get it working!
              logger.info(state.toString)
            } else {
              failStage(new BetfairSubscriptionFailedException(s"Subscription with Betfair failed"))
            }
          case Subscribed =>
            val event = grab(sslIn)
            if (heatbeatValid(event)) {
              //logger.info("Heartbeat")
              pull(sslIn)
            } else {
              // todo asInstanceOf code smell
              push(bytesOut, event.asInstanceOf[MarketChangeMessage])
              //logger.info("MarketChange")
            }
        }
      }

      override def onUpstreamFinish(): Unit = complete(bytesOut)
    })

    // I2
    //TODO THIS SHOULD BE REMOVED change fanin shape?
    setHandler(bytesIn, new InHandler {
      override def onPush() = {
        push(sslOut, grab(bytesIn))
      }

      override def onUpstreamFinish(): Unit = complete(sslOut)
    })

    // Called when transport pull for data
    // O1
    setHandler(bytesOut, new OutHandler {
      override def onPull() = state match {
        case Connecting =>
        case Authenticating =>
        case Subscribing =>
        case Subscribed => pull(sslIn)
      }

      override def onDownstreamFinish(): Unit = cancel(sslIn)
    })

    // O2
    setHandler(sslOut, new OutHandler {
      override def onPull() =
        state match {
        case Connecting => pull(sslIn)
        case Authenticating => pull(sslIn)
        case Subscribing => pull(sslIn)
        case _ =>
      }

      override def onDownstreamFinish(): Unit = cancel(bytesIn)
    })

  }
}