package com.bfg.infrastructure.betfair

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import com.bfg.UnitSpec
import com.bfg.domain.model._
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.scalatest.{EitherValues, FlatSpec, Matchers}
import akka.stream.scaladsl._
import akka.stream.testkit.scaladsl.{TestSink, TestSource}
import akka.util.ByteString
import de.knutwalker.akka.stream.support.CirceStreamSupport
import jawn.AsyncParser

import scala.concurrent.Future

/**
  * Created by henke on 2017-05-18.
  */
class JsonSupportTest extends UnitSpec with EitherValues with JsonSupport {

  "A ResponseMessage" should "know ConnectionMessage" in {
    //val connectionMessage = ConnectionMessage("connection", "Bla")
    val connectionEvent = """{"op":"connection","connectionId":"050-250517141605-1391626"}"""
    val either = decode[BetfairEvent](connectionEvent)
    either.right.value shouldBe a [ConnectionMessage]
  }
  it should "know statusMessage" in {
    val authEvent = """{"op":"status","id":1,"statusCode":"SUCCESS","connectionClosed":false}"""
    val either = decode[BetfairEvent](authEvent)
    either.right.value shouldBe a [StatusMessage]
  }

  it should "know failed statusMessage" in {
    val failMsg = """{"op":"status","statusCode":"FAILURE","errorCode":"TIMEOUT","errorMessage":"Connection is not subscribed and is idle: 15000 ms","connectionClosed":true,"connectionId":"050-250517141605-1391626"}"""
    val either = decode[BetfairEvent](failMsg)
    either.right.value shouldBe a [StatusMessage]
  }

  it should "know heatbeatmessage as MarketChangeMessage" in {
    val hrt = """{"op":"mcm","id":123,"clk":"AAAAAAAA","pt":1495773941868,"ct":"HEARTBEAT"}"""
    val either = decode[BetfairEvent](hrt)
    either.right.value shouldBe a [MarketChangeMessage]
  }

  "A RequestMessage" should "know AuthenticationMessage" in {
    val json = """
      |{
      |"op":"authentication",
      |"id":1,
      |"session":"A",
      |"appKey":"B"
      |}
    """.stripMargin.replaceAll("[\n\r]","").trim
    val authenticationMessage: BetfairRequest = RequestAuthentication(id=1, session="A", appKey="B")
    val authenticationMessageJson = authenticationMessage.asJson.noSpaces
    authenticationMessageJson shouldEqual json
  }

  // If connection success
  //"{"op":"connection","connectionId":"050-250517141605-1391626"}"
  // If autentication message is ok with uniqe id
  //"{"op":"status","id":1,"statusCode":"SUCCESS","connectionClosed":false}"
  // Timeout if I dont subscribe in time
  //"{"op":"status","statusCode":"FAILURE","errorCode":"TIMEOUT","errorMessage":"Connection is not subscribed and is idle: 15000 ms","connectionClosed":true,"connectionId":"050-250517141605-1391626"}"
  // Send market subscription with UNIQE id
  //{"op":"status","id":123,"statusCode":"SUCCESS","connectionClosed":false}
  // Initial subscription
  // Heatbeat after subscription
  // {"op":"mcm","id":123,"clk":"AAAAAAAA","pt":1495773941868,"ct":"HEARTBEAT"}
  // Update message

  it should "encode marketSubscription and drop any Option field that are None" in {
    val json = """{"op":"marketSubscription","id":2,"marketFilter":{"marketIds":["1.120684740"],"bspMarket":true,"bettingTypes":["ODDS"],"eventTypeIds":["1"],"eventIds":["27540841"],"turnInPlayEnabled":true,"marketTypes":["MATCH_ODDS"],"countryCodes":["ES"]},"marketDataFilter":{"fields":["EX_BEST_OFFERS_DISP","EX_BEST_OFFERS","EX_ALL_OFFERS","EX_TRADED","EX_TRADED_VOL","EX_LTP","EX_MARKET_DEF","SP_TRADED","SP_PROJECTED"]}}""".stripMargin.replaceAll("[\n\r]","").trim
    val marketSubscription: BetfairRequest = RequestMarketSubscription(
      id=2,
      marketFilter = MarketSubscriptionMarketFilter(
        marketIds = Some(List("1.120684740")),
        bspMarket = Some(true),
        bettingTypes = Some(List("ODDS")),
        eventTypeIds = List("1"),
        eventIds = Some(List("27540841")),
        turnInPlayEnabled = Some(true),
        marketTypes = List("MATCH_ODDS"),
        countryCodes = List("ES")
      ),
      marketDataFilter = MarketSubscriptionMarketDataFilter(
        fields = List("EX_BEST_OFFERS_DISP","EX_BEST_OFFERS","EX_ALL_OFFERS","EX_TRADED","EX_TRADED_VOL","EX_LTP","EX_MARKET_DEF","SP_TRADED","SP_PROJECTED")
      )
    )
    val encodedMessage = requestToJsonDropNullKeys(marketSubscription)
    val encodedDecoded = decode[RequestMarketSubscription](encodedMessage)
    val jsonDecoded = decode[RequestMarketSubscription](json)
    encodedDecoded.right.value shouldEqual jsonDecoded.right.value
  }

  it should "not fail" in {
    implicit val sys = ActorSystem("test")
    implicit val mat = ActorMaterializer()
    val filename = "bfg/src/test/resources/update_mcm_stream.json"
    val fileContents = scala.io.Source.fromFile(filename).getLines.mkString.replaceAll("[\n\r]","")
    val jsonDecoded = decode[MarketChangeMessage](fileContents)
    val i = 1
    //Source.single(ByteString(fileContents)).via(jsonDecoder).to(Sink.foreach(println)).run()
  }

  it should "decode chunked marketChangeMessage" in {
    implicit val sys = ActorSystem("test")
    implicit val mat = ActorMaterializer()
    case class Ex(a: Int)
    val file = Paths.get("bfg/src/test/resources/update_mcm_stream.json")
    val lines = FileIO.fromPath(file)
      .via(Framing.delimiter(ByteString("\n"), 15000, true))
    val data = lines
    .via(jsonDecoder)
    .to(Sink.foreach(println))
    .run()
    //.runWith(TestSink.probe[Ex])
    //.request(1)
    //.expectComplete()
  }


}
