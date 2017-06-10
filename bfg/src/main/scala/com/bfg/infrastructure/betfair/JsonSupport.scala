package com.bfg.infrastructure.betfair

import com.bfg.domain.model._
import com.bfg.domain.model.order.request.Side
import com.typesafe.scalalogging.LazyLogging
import de.knutwalker.akka.http.support.CirceHttpSupport
import de.knutwalker.akka.stream.support.CirceStreamSupport
import io.circe._
import io.circe.generic.auto._
import io.circe.generic.extras.semiauto._
import io.circe.java8.time.TimeInstances
import io.circe.syntax._

/**
  * Created by henke on 2017-05-13.
  */
trait JsonSupport extends CirceHttpSupport with TimeInstances with LazyLogging {
  implicit val decodeResponseMessage: Decoder[BetfairEvent] = Decoder.instance(c => {
    c.get[ResponseType]("op").flatMap {
      case Connection => c.as[ConnectionMessage]
      case Status => c.as[StatusMessage]
      case MCM => c.as[MarketChangeMessage]
    }
  })

  // Custom encoder needed since by default circe include type name for generic types
  implicit val encodeFoo: Encoder[BetfairRequest] = new Encoder[BetfairRequest] {
    final def apply(a: BetfairRequest): Json = a match {
      case m: RequestAuthentication => m.asJson
      case m: RequestMarketSubscription => m.asJson
    }
  }

  implicit val decoderPriceVolume: Decoder[PriceVolume] =
    Decoder[(Double, Double)].map(p => PriceVolume(p._1, p._2))

  implicit val decoderLevelPriceVolume: Decoder[LevelPriceVolume] =
    Decoder[(Double, Double, Double)].map(p => LevelPriceVolume(p._1.toInt, p._2, p._3))

  implicit val sideDecoder: Decoder[Side] = deriveEnumerationDecoder[Side]
  implicit val sideEncoder: Encoder[Side] = deriveEnumerationEncoder[Side]

  private val dropNullPrinter = Printer.noSpaces.copy(dropNullKeys = true)
  def requestToJsonDropNullKeys(request: BetfairRequest) = dropNullPrinter.pretty(request.asJson)

  val jsonDecoder = CirceStreamSupport.decode[BetfairEvent].map{ e =>
    //logger.info(e.toString)
    e
  }
  val jsonEncoder = CirceStreamSupport.encode[BetfairRequest]
}
