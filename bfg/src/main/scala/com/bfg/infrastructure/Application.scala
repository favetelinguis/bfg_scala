package com.bfg.infrastructure

import akka.actor.ActorRef
import akka.stream.scaladsl.Sink
import com.bfg.application.monitors
import com.bfg.application.monitors.{MarketSnapMonitor, Monitor, MonitorsModule}
import com.bfg.application.trader.TraderModule
import com.bfg.domain.model.{MarketSnap, SessionToken}
import com.bfg.infrastructure.akkaconf.AkkaModule
import com.bfg.infrastructure.betfair.BetfairModule
import com.bfg.infrastructure.gui.GUI
import com.softwaremill.tagging.@@
import com.softwaremill.tagging._

import scala.util.{Failure, Success}
import scalafx.application.JFXApp

/**
  * Created by henke on 2017-06-11.
  */
object Application
  extends JFXApp
  with AkkaModule
  with MonitorsModule
  with TraderModule
  with BetfairModule
  with GUI {

  // Start GUI
  stage = new JFXApp.PrimaryStage {
    title = "BFG6"
    scene = mainStageView.scene
  }

  // Cleanup after program exit
  override def stopApp(): Unit = {
    actorSystem.terminate()
    // todo properly logout and quit from betfair
  }

  // Setup Monitors
  override val marketSnapMonitor =
    Monitor.createMonitor[MarketSnap]
      .to(Sink.foreach(mainStageController.handleMarketSnapMonitor))
      .run().taggedWith[MarketSnapMonitor]

  // Request session
  sessionService.getSession()
    .onComplete{
      case Success(st: SessionToken) => {
        //Start stream
        marketStreamService.connect(st)
          .to(traderService.system)
          .run()
      }
      case Failure(e) => {
        //todo call controller to update state in gui
        logger.error(s"Failed to get session from betfair: $e")
      }
    }

}
