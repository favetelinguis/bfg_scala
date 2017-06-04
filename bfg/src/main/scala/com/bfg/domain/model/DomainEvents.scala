package com.bfg.domain.model

import scala.annotation.meta.field

/**
  * Created by henke on 2017-05-25.
  */
object DomainEvents {

  sealed trait SessionEvent
  object SessionEvent {
    case class SessionCreated(sessionToken: String) extends SessionEvent
  }
}
