package com.bfg.infrastructure.monitors

import akka.actor.ActorRef
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Source}

/**
  * Created by henke on 2017-06-10.
  */
object Monitor {

  def createMonitor[T]: Source[T, ActorRef] = Source.actorRef[T](Int.MaxValue, OverflowStrategy.fail)

  def logToMonitorAndContinue[T](monitor: ActorRef) = {
    Flow[T].map{ e =>
      monitor ! e
      e
    }
  }
}
