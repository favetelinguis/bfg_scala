package com.bfg.infrastructure.server.routes

import akka.http.scaladsl.server.Route
import com.bfg.infrastructure.server.handlers.AuthenticationHandler
import io.circe.generic.auto._
import com.bfg.domain.model.DomainEvents.SessionEvent.SessionCreated
import com.bfg.infrastructure.betfair.JsonSupport

/**
  * Created by henke on 2017-05-20.
  */
class AuthenticationRoute(handler: AuthenticationHandler) extends PartialRoute with JsonSupport {

  override val route: Route = authenticationStatus

  def authenticationStatus =
    path("session" / "authStatus") {
      get {
        complete(handler.authenticationStatus.mapTo[SessionCreated])
      }
    }
}
