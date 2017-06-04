package com.bfg.infrastructure.server

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import com.bfg.infrastructure.config.ConfigModule
import com.bfg.infrastructure.server.routes.RouteModule
import com.bfg.infrastructure.server.wrappers.WrapperModule
import com.softwaremill.macwire.wire

import scala.concurrent.ExecutionContext

/**
  * Created by henke on 2017-05-20.
  */
trait ServerModule extends RouteModule with WrapperModule with ConfigModule {

  def actorSystem: ActorSystem
  def dispatcher: ExecutionContext
  def actorMaterializer: ActorMaterializer
  // todo i need tagging here for different monitors
  def sessionMonitor: ActorRef

  lazy val api: Api = wire[Api]
  lazy val server: Server = wire[Server]

}
