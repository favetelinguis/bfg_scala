package com.bfg.infrastructure.betfair

import java.time.Instant

import com.bfg.UnitSpec
import com.bfg.domain.model._
import com.bfg.domain.model.order.request.Side
import com.bfg.domain.model.order.request.SideChoices.BACK
import io.circe.parser.decode
import io.circe.syntax._
import org.scalatest.EitherValues

/**
  * Created by henke on 2017-05-18.
  */
class JsonSupportTest extends UnitSpec with EitherValues with JsonSupport {

  "A BetfairEvent" should "know ConnectionMessage" in {
    val connectionEvent = """{"op":"connection","connectionId":"050-250517141605-1391626"}"""
    val either = decode[BetfairEvent](connectionEvent)
    either.right.value shouldBe a [ConnectionMessage]
  }
  it should "know StatusMessage" in {
    val authEvent = """{"op":"status","id":1,"statusCode":"SUCCESS","connectionClosed":false}"""
    val either = decode[BetfairEvent](authEvent)
    either.right.value shouldBe a [StatusMessage]
  }

  it should "know failed StatusMessage" in {
    val failMsg = """{"op":"status","statusCode":"FAILURE","errorCode":"TIMEOUT","errorMessage":"Connection is not subscribed and is idle: 15000 ms","connectionClosed":true,"connectionId":"050-250517141605-1391626"}"""
    val either = decode[BetfairEvent](failMsg)
    either.right.value shouldBe a [StatusMessage]
  }

  it should "know Heatbeatmessage" in {
    val hrt = """{"op":"mcm","id":123,"clk":"AAAAAAAA","pt":1495773941868,"ct":"HEARTBEAT"}"""
    val either = decode[BetfairEvent](hrt)
    either.right.value shouldBe a [MarketChangeMessage]
  }

  "Sumtypes" should "be decode to strings" in {
    (BACK: Side).asJson.as[String].right.get shouldBe "BACK"
  }

  "A RequestMessage" should "not include the class name in the json message" in {
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

  "Unix timestamps in milliseconds" should "be converted to ZonedDateTime" in {
    val time = """1495774056868"""
    val decodedTime = decode[Instant](time)
    val i = 1
  }

  "Chunked JSON" should "be supported" in {

  }

  "Null parameters" should "be supported" in {

  }
}
