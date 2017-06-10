package com.bfg.infrastructure

import akka.http.scaladsl.Http
import akka.stream.scaladsl.Sink
import com.bfg.domain.model._
import com.bfg.infrastructure.akkaconf.AkkaModule
import com.bfg.infrastructure.betfair.BetfairModule
import com.bfg.infrastructure.monitors.MonitorsModule
import com.bfg.infrastructure.server.ServerModule
import com.bfg.infrastructure.trader.TraderModule
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

/**
  * Created by henke on 2017-05-20.
  */
object Application
  extends App
    with BetfairModule
    with TraderModule
    with ServerModule
    with AkkaModule
    with MonitorsModule
    with LazyLogging {


  // Wire dependecies
  //override val sessionMonitor = getSessionMonitor

  // Setup all monitors
  //actorSystem.eventStream.subscribe(sessionMonitor, classOf[SessionEvent])

  //import com.bfg.domain.model.DomainEvents.SessionEvent.SessionCreated
  //val stopKeepAlive = for {
  //  session <-
  //  _ =
  //  //todo getMarketStreamActor ? SetupSubscription(session)
  //  _ = actorSystem.eventStream.publish(SessionCreated(session.sessionToken))
  //cancelKeepAlive = keepSessionAlive(session)
  //} yield session
  //stopKeepAlive.foreach(m=>logger.info(m.toString))
  // Send status events from stream like this
  //system.eventStream.publish(SessionMonitorProtocol.SessionCreated("BAJA"))

  val subStream =
    marketCacheService.holdCache
      .via(traderService.system)

  def stream(st: SessionToken) = {
    marketStreamService.connect(st)
      .groupBy(Int.MaxValue, _.id)
      .via(subStream).async
      .mergeSubstreams
      .via(orderService.executeOrders)
      .to(Sink.foreach(m => logger.info(s"Incoming market data: $m")))
      .run()
  }

  // Start server
  sessionService.getSession()
    .onComplete{
    case Success(st: SessionToken) => {
      //Start stream
      stream(st)
      //Start server
      server.bind.foreach { binding =>
        server.afterStart(binding)
        sys.addShutdownHook {
          //todo logout from bf and close tcp sockets
          Http().shutdownAllConnectionPools().onComplete{_ =>
            server.beforeStop(binding)
          }
        }
      }
    }
    case Failure(e) => logger.error(s"Failed to setup to betfair: $e")
  }
}
