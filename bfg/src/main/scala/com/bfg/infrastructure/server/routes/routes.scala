package com.bfg.infrastructure.server.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import com.bfg.infrastructure.server.handlers.HandlerModule
import com.softwaremill.macwire.wire

/**
  * Created by henke on 2017-05-20.
  */
trait PartialRoute extends Directives {
  def route: Route
}

trait RouteModule extends HandlerModule {
  def actorSystem: ActorSystem
  def actorMaterializer: ActorMaterializer
  lazy val routes: Seq[PartialRoute] = Seq(
    wire[AuthenticationRoute]
  )
}
