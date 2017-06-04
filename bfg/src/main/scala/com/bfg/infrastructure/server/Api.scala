package com.bfg.infrastructure.server

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.settings.RoutingSettings
import akka.stream.ActorMaterializer
import com.bfg.infrastructure.server.routes.{PartialRoute}
import com.bfg.infrastructure.server.wrappers.Wrapper
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import scala.concurrent.ExecutionContext

/**
  * Created by henke on 2017-05-20.
  */
class Api(partialRoutes: Seq[PartialRoute], wrappers: Seq[Wrapper],
          sys: ActorSystem, mat: ActorMaterializer, ec: ExecutionContext) {
  implicit private val system = sys
  implicit private val materializer = mat
  implicit private val exec = ec
  implicit private val routingSettings = RoutingSettings.default

  private val allWrappers = {
    wrappers.foldLeft[Route => Route](Route.apply) { (builder, wrapper) =>
      builder.compose(wrapper.wrap)
    }
  }

  private val allRoutes = {
    partialRoutes.foldRight[Route](reject) { (partial, builder) =>
      partial.route ~ builder
    }
  }

  val route: Route = allWrappers(allRoutes)

  val routeFlow: Flow[HttpRequest, HttpResponse, NotUsed] = Route.handlerFlow(route)
}
