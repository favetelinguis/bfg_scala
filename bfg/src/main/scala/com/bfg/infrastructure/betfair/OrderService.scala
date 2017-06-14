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

}
