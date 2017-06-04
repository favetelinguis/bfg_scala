package com.bfg.infrastructure.betfair

import java.time.Instant

import akka.util.ByteString
import cats.syntax.either._
import com.bfg.domain.model._
import com.typesafe.scalalogging.LazyLogging
import de.knutwalker.akka.http.support.CirceHttpSupport
import de.knutwalker.akka.stream.support.CirceStreamSupport
import jawn.AsyncParser
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

/**
  * Created by henke on 2017-05-13.
  */
trait JsonSupport extends CirceHttpSupport with LazyLogging {
  implicit val decodeResponseMessage: Decoder[BetfairEvent] = Decoder.instance(c => {
    c.get[ResponseType]("op").flatMap {
      case Connection => c.as[ConnectionMessage]
      case Status => c.as[StatusMessage]
      case MCM => c.as[MarketChangeMessage]
    }
  }
  )

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

  implicit val encodeInstant: Encoder[Instant] = Encoder.encodeString.contramap[Instant](_.toString)
  implicit val decodeInstant: Decoder[Instant] = Decoder.decodeString.emap { str =>
    Either.catchNonFatal(Instant.parse(str)).leftMap(t => "Instant")
  }

  private val dropNullPrinter = Printer.noSpaces.copy(dropNullKeys = true)
  def requestToJsonDropNullKeys(request: BetfairRequest) = dropNullPrinter.pretty(request.asJson)

  val jsonDecoder = CirceStreamSupport.decode[BetfairEvent].map{ e =>
    //logger.info(e.toString)
    e
  }
  val jsonEncoder = CirceStreamSupport.encode[BetfairRequest]
}
