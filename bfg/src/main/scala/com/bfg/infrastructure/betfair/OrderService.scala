package com.bfg.infrastructure.betfair

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.FormData
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import com.bfg.domain.model.Decision
import com.bfg.domain.model.order.request.PlaceOrders._
import com.bfg.infrastructure.config.BetingRestEndpoint
import com.softwaremill.tagging.@@

/**
  * Created by henke on 2017-06-07.
  */
class OrderService(
                    bettingEndpoint: String @@ BetingRestEndpoint,
                    actorSystem: ActorSystem,
                    actorMaterializer: ActorMaterializer
                  ) extends JsonSupport {

  val createOrder = Flow[Decision].map(orderFromDecision andThen(d => d.))

  val connectionPool = Http().superPool[Int](url = bettingEndpoint)
  val cp =  Http().cachedHostConnectionPool[T](bettingEndpoint)

  private val requestBody = FormData(Map(
    "password" -> betfairPwd, "username" -> betfairUser
  )).toEntity

  private val loginRequest = HttpRequest(
    uri = Uri(sessionEndpoint),
    method = HttpMethods.POST,
    entity = requestBody
  ).withHeaders(
    RawHeader("X-Application", appKey)
  )
  val createRequest = F

  val executeOrders = createOrder.via(connectionPool)
}
