package com.bfg.infrastructure.monitors

import akka.actor.{Actor, ActorLogging}

/**
  * Created by henke on 2017-05-21.
  */
object SessionMonitorProtocol {

  // Query from API
  case class GetSessionStatus()

}

class SessionMonitor extends Actor with ActorLogging {
  import SessionMonitorProtocol._
  import com.bfg.domain.model.DomainEvents.SessionEvent._

  // Actor state
  var session: SessionCreated = null
  /////////

  override def receive: Receive = {
    // Event
    case s: SessionCreated => session = s

    //Query
    case m: GetSessionStatus => {
      sender ! session
    }

    //Catch all
    case m => log.warning(s"UNKNOWN MESSAGE: ${m.toString}")
  }
}
