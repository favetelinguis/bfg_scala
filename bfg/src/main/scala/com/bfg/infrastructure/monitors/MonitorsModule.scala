package com.bfg.infrastructure.monitors

import akka.actor.{ActorSystem, Props}
import com.bfg.domain.model.MarketSnap
import com.softwaremill.macwire.wire
import com.softwaremill.tagging._

/**
  * Created by henke on 2017-05-21.
  */
trait MonitorsModule {

  def actorSystem: ActorSystem

  //lazy val getSessionMonitor = actorSystem.actorOf(Props(wire[SessionMonitor]))
  lazy val marketsSnapMonitor = Monitor.createMonitor[MarketSnap].taggedWith[MarketsSnapMonitor]

}
