package com.bfg.application.trader

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink}
import com.bfg.application.monitors.{MarketSnapMonitor, Monitor}
import com.bfg.domain.model.{Decision, MarketSnap}
import com.bfg.infrastructure.config.WindowSize
import com.softwaremill.tagging.@@

import scala.concurrent.Future

/**
  * Created by henke on 2017-06-07.
  */
class TraderService(
                     windowSize: Int @@ WindowSize,
                     marketSnapMonitor: ActorRef @@ MarketSnapMonitor,
                     actorSystem: ActorSystem,
                     actorMaterializer: ActorMaterializer
                   ) {

  private val marketDataEntry = Flow[MarketSnap].via(Monitor.logToMonitorAndContinue(marketSnapMonitor))

  private val window = marketDataEntry.sliding(windowSize)

  private val decision = Flow[Seq[MarketSnap]]
  .mapAsync(1) { mss =>
    val r = scala.util.Random
    if (r.nextFloat > 0.5) Future.successful(Some(Decision())) else Future.successful(None)
  }

  val system: Sink[MarketSnap, NotUsed] =
    window
    .via(decision)
    .to(Sink.foreach(println))

}
