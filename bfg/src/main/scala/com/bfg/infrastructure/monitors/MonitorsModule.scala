package com.bfg.infrastructure.monitors

import akka.actor.{ActorSystem, Props}
import com.softwaremill.macwire.wire

/**
  * Created by henke on 2017-05-21.
  */
trait MonitorsModule {

  def actorSystem: ActorSystem

  lazy val getSessionMonitor = actorSystem.actorOf(Props(wire[SessionMonitor]))

}
