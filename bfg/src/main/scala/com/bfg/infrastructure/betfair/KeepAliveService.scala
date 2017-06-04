package com.bfg.infrastructure.betfair

import akka.actor.{ActorSystem, Cancellable}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.bfg.domain.model.SessionToken
import com.typesafe.scalalogging.LazyLogging

import com.softwaremill.tagging._
import com.bfg.infrastructure.config._
import scala.concurrent.duration._
import scala.concurrent.Future

/**
  * Created by henke on 2017-05-13.
  */
class KeepAliveService(
                        keepAliveEndpoint: String @@ KeepAliveEndpoint,
                        appKey: String @@ AppKey,
                      actorSystem: ActorSystem,
                      actorMaterializer: ActorMaterializer
                      ) extends LazyLogging {

  private implicit val mat = actorMaterializer
  private implicit val sys = actorSystem

  //todo inject delay for testability
  private def scheduler(token: String): Source[String, Cancellable] = Source.tick(initialDelay = 210 minutes, interval = 210 minutes, token)

  private def createReguest(token: String) = HttpRequest(
    uri = Uri(keepAliveEndpoint),
    method = HttpMethods.GET
  ).withHeaders(
    RawHeader("X-Application", appKey),
    RawHeader("X-Authentication", token)
  )

  private def keepAliveRequest(request: HttpRequest): Future[HttpResponse] = {
    Http().singleRequest(request = request)
  }

  //todo this method require error handling
  def keepSessionAlive(token: SessionToken): Cancellable = {
    scheduler(token.sessionToken)
    .map(createReguest)
    .mapAsync(1)(keepAliveRequest)
    .to(Sink.ignore)
    .run()
  }

}
