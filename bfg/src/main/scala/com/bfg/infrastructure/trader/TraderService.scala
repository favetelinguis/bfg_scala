package com.bfg.infrastructure.trader

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import com.bfg.domain.model.{Decision, MarketSnap}
import com.bfg.infrastructure.config.WindowSize
import com.softwaremill.tagging.@@

import scala.concurrent.Future

/**
  * Created by henke on 2017-06-07.
  */
class TraderService(
                     windowSize: Int @@ WindowSize,
                     actorSystem: ActorSystem,
                     actorMaterializer: ActorMaterializer
                   ) {

  private val window = Flow[MarketSnap].sliding(windowSize)

  private val decision = Flow[Seq[MarketSnap]]
  .mapAsync(1) { mss =>
    val r = scala.util.Random
    if (r.nextFloat > 0.5) Future.successful(Some(Decision())) else Future.successful(None)
  }

  val system: Flow[MarketSnap, Option[Decision], NotUsed] =
    window
    .via(decision)

}
