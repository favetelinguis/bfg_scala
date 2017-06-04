package com.bfg.infrastructure.akkaconf

import akka.actor.ActorSystem
import akka.actor.TypedActor.Supervisor
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

/**
  * Created by henke on 2017-05-20.
  */
trait AkkaModule extends LazyLogging {

  val decider: Supervision.Decider = { e =>
    logger.error("Unhandled exception in stream: ", e)
    Supervision.Stop
  }

  implicit val actorSystem: ActorSystem = ActorSystem()
  private val materializerSettings = ActorMaterializerSettings(actorSystem).withSupervisionStrategy(decider)
  implicit val dispatcher: ExecutionContext = actorSystem.dispatcher
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer(materializerSettings)

}
