package com.bfg.application.trader

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import com.bfg.application.monitors.MarketSnapMonitor
import com.bfg.infrastructure.config.ConfigModule
import com.softwaremill.macwire.wire
import com.softwaremill.tagging.@@

/**
  * Created by henke on 2017-06-07.
  */
trait TraderModule extends ConfigModule {

  def actorMaterializer: ActorMaterializer
  def actorSystem: ActorSystem
  def marketSnapMonitor: ActorRef @@ MarketSnapMonitor

  lazy val traderService = wire[TraderService]
}
