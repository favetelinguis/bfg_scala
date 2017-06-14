package com.bfg.infrastructure.betfair

import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext}

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.stream.ActorMaterializer
import com.bfg.domain.model.SessionToken
import com.bfg.infrastructure.config._
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henke on 2017-05-13.
  */
class SessionService(
                      betfairPwd: String @@ BetfairPwd,
                      betfairUser: String @@ BetfairUser,
                      sessionEndpoint: String @@ SessionEndpoint,
                      appKey: String @@ AppKey,
                      p12Pwd: String @@ P12Pwd,
                      actorSystem: ActorSystem,
                     actorMaterializer: ActorMaterializer,
                     dispatcher: ExecutionContext) extends LazyLogging with JsonSupport {
  private implicit val mat = actorMaterializer
  private implicit val sys = actorSystem
  private implicit val dis = dispatcher

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

  private val httpsContext = ConnectionContext.https {
    val password = p12Pwd.toCharArray
    val context = SSLContext.getInstance("TLS")
    val ks = KeyStore.getInstance("PKCS12")
    ks.load(getClass.getClassLoader.getResourceAsStream("keys/client-2048.p12"), password)
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, password)
    context.init(keyManagerFactory.getKeyManagers, null, new SecureRandom)
    context
  }

  def getSession(): Future[SessionToken] =
    Http()
      .singleRequest(request = loginRequest, connectionContext = httpsContext)
      .map{r=>
        logger.info(s"Response from Session login: ${r.entity}")
        r
      }
      .flatMap{ case HttpResponse(StatusCodes.OK,_,entity,_) => Unmarshal(entity.withContentType(ContentTypes.`application/json`)).to[SessionToken] }
}
