package com.bfg.infrastructure

import com.bfg.domain.model._
import com.bfg.domain.model.DomainEvents.SessionEvent
import com.bfg.infrastructure.akkaconf.AkkaModule
import com.bfg.infrastructure.betfair.BetfairModule
import com.bfg.infrastructure.monitors.{MonitorsModule, SessionMonitorProtocol}
import com.bfg.infrastructure.server.ServerModule
import com.typesafe.scalalogging.LazyLogging
import akka.pattern.ask
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import akka.util.Timeout

import scala.util.{Failure, Success}
import scala.concurrent.duration._

/**
  * Created by henke on 2017-05-20.
  */
object Application
  extends App
    with BetfairModule
    with ServerModule
    with AkkaModule
    with MonitorsModule
    with LazyLogging {


  // Wire dependecies
  override val sessionMonitor = getSessionMonitor

  // Setup all monitors
  actorSystem.eventStream.subscribe(sessionMonitor, classOf[SessionEvent])

  import com.bfg.domain.model.DomainEvents.SessionEvent.SessionCreated
  val stopKeepAlive = for {
    session <- sessionService.getSession()
    _ = marketStreamService.connect(session)
    //todo getMarketStreamActor ? SetupSubscription(session)
    _ = actorSystem.eventStream.publish(SessionCreated(session.sessionToken))
  //cancelKeepAlive = keepSessionAlive(session)
  } yield session
  //stopKeepAlive.foreach(m=>logger.info(m.toString))
  // Send status events from stream like this
  //system.eventStream.publish(SessionMonitorProtocol.SessionCreated("BAJA"))

  // Start server
  stopKeepAlive
    .onComplete{
    case Success(_) => {
      server.bind.foreach { binding =>
        server.afterStart(binding)
        sys.addShutdownHook {
          //todo logout from bf and close tcp sockets
          server.beforeStop(binding)
        }
      }
    }
    case Failure(e) => logger.error(s"Failed to setup to betfair: $e")
  }
}
