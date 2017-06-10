package com.bfg.infrastructure.trader

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.bfg.infrastructure.config.ConfigModule
import com.softwaremill.macwire.wire

/**
  * Created by henke on 2017-06-07.
  */
trait TraderModule extends ConfigModule {

  def actorMaterializer: ActorMaterializer
  def actorSystem: ActorSystem

  lazy val traderService = wire[TraderService]
}
