package com.bfg.infrastructure.betfair

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.bfg.infrastructure.config.ConfigModule
import com.softwaremill.macwire.wire

import scala.concurrent.ExecutionContext

/**
  * Created by henke on 2017-05-25.
  */
trait BetfairModule extends ConfigModule {

  def actorMaterializer: ActorMaterializer
  def dispatcher: ExecutionContext
  def actorSystem: ActorSystem

  lazy val sessionService = wire[SessionService]
  lazy val marketCacheService = wire[MarketCacheService]
  lazy val keepAliveService = wire[KeepAliveService]
  lazy val marketStreamService = wire[MarketStreamService]
  lazy val orderService = wire[OrderService]

}
