package com.bfg.infrastructure.server.handlers

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import com.bfg.infrastructure.monitors.SessionMonitorProtocol
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henke on 2017-05-20.
  */
class AuthenticationHandler(authenticationActor: ActorRef,
                            dispatcher: ExecutionContext,
                            timeout: Timeout) extends LazyLogging
{
  implicit val d = dispatcher
  implicit val t = timeout

  val authenticationStatus = {
    authenticationActor ? SessionMonitorProtocol.GetSessionStatus()
  }

}
