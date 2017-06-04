package com.bfg.infrastructure.server.handlers

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import com.softwaremill.macwire.wire

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by henke on 2017-05-20.
  */
trait HandlerModule {

  def sessionMonitor: ActorRef
  def dispatcher: ExecutionContext
  val timeout = Timeout(5.seconds)
  lazy val authentincationHandler: AuthenticationHandler = wire[AuthenticationHandler]

}
