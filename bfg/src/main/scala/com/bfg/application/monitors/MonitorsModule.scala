package com.bfg.application.monitors

import akka.actor.{ActorSystem, Props}
import com.bfg.domain.model.MarketSnap
import com.softwaremill.macwire.wire

/**
  * Created by henke on 2017-05-21.
  */
trait MonitorsModule {

  def actorSystem: ActorSystem

}
