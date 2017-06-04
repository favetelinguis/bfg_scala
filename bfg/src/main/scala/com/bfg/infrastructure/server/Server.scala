package com.bfg.infrastructure.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.bfg.infrastructure.config.{ServerHostConfig, ServerPortConfig}
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

/**
  * Created by henke on 2017-05-20.
  */
class Server(
              api: Api,
              host: String @@ ServerHostConfig,
              port: Int @@ ServerPortConfig,
              actorSystem: ActorSystem,
              actorMaterializer: ActorMaterializer,
              dispatcher: ExecutionContext
            ) extends LazyLogging {

  implicit private val system = actorSystem
  implicit private val materializer = actorMaterializer
  implicit private val exec = dispatcher

  def bind: Future[ServerBinding] = {
    Http(actorSystem).bindAndHandle(api.routeFlow, host, port)
  }

  def afterStart(binding: ServerBinding): Unit = {
    logger.info(s"Server started on ${binding.localAddress.toString}")
  }

  def beforeStop(binding: ServerBinding): Unit = {
    Await.ready({
      binding.unbind().map { _ =>
        actorSystem.terminate()
        logger.info("Shutting down")
        actorSystem.whenTerminated
      }
    }, 1.minute)
  }
}
