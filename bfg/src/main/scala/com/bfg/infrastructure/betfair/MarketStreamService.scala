package com.bfg.infrastructure.betfair

import javax.net.ssl.SSLContext

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.TLSProtocol.{NegotiateNewSession, SslTlsInbound, SslTlsOutbound}
import akka.stream.scaladsl.Tcp.OutgoingConnection
import akka.stream.scaladsl.{BidiFlow, Flow, Source, TLS, Tcp}
import akka.stream.{ActorMaterializer, Client, OverflowStrategy, TLSProtocol}
import akka.util.ByteString
import com.bfg.domain.model._
import com.bfg.infrastructure.betfair.subscriptionGraphState.SubscriptionGraphStage
import com.bfg.infrastructure.config._
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

/**
  * Created by henke on 2017-05-08.
  */
class MarketStreamService(
                           streamUrl: String @@ StreamEndpoint,
                           streamPort: Int @@ StreamPort,
                         actorSystem: ActorSystem,
                         actorMaterializer: ActorMaterializer,
                         marketCacheService: MarketCacheService
                         ) extends LazyLogging with JsonSupport {

  private implicit val mat = actorMaterializer
  private implicit val sys = actorSystem

  private val pf: PartialFunction[TLSProtocol.SslTlsInbound, ByteString] = {
    case TLSProtocol.SessionBytes(_, sb) => ByteString.fromByteBuffer(sb.asByteBuffer)
  }

  private val codec: BidiFlow[BetfairRequest, TLSProtocol.SendBytes, SslTlsInbound, BetfairEvent, NotUsed] = BidiFlow.fromFlows(
    Flow[BetfairRequest].via(jsonEncoder.map(s => ByteString(s + "\r\n"))).map(TLSProtocol.SendBytes),
    Flow[TLSProtocol.SslTlsInbound].collect(pf).via(jsonDecoder))

  private def getTcpConnection(token: SessionToken, host: String, port: Int) = {
    // This object will be used to create SSLEngine
    val sslContext = SSLContext.getDefault()
    val tls: BidiFlow[SslTlsOutbound, ByteString, ByteString, SslTlsInbound, NotUsed] = TLS(sslContext, NegotiateNewSession(None, None, None, None), Client)

    //When this flow is materialized a connection is opened to server, each time this is materialized a connection is opened
    val connectionFlow: Flow[ByteString, ByteString, Future[OutgoingConnection]] = Tcp().outgoingConnection(host, port)

    val proxyStage = new SubscriptionGraphStage(token)
    val proxyFlow = BidiFlow.fromGraph(proxyStage)
    //By convention protocol stacks are growing to the left, the rightmost is the TCP connection
    proxyFlow.atop(codec).atop(tls).join(connectionFlow)
  }

  private def tcpConnection(token: SessionToken) =
    Source.actorRef[BetfairRequest](1024, OverflowStrategy.fail)
    .via(getTcpConnection(token, streamUrl, streamPort))

  // Each time I run this function I establish a new connection and gets an actor back wich I can send outgoing messages to
  // I need to connect all my trading loginc and connect this to a source and then i can run til graph
  def connect(token: SessionToken): Source[MarketSnap, ActorRef] = tcpConnection(token)
    .via(marketCacheService.marketCache)
    .groupBy(Int.MaxValue, _.id)
    .via(marketCacheService.holdCache).async
    .mergeSubstreams
}
