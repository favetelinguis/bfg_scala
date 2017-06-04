package com.bfg.infrastructure.server.wrappers

import akka.http.scaladsl.server.Route
import com.softwaremill.macwire.wire

/**
  * Created by henke on 2017-05-20.
  */
trait Wrapper {
  def wrap(route: Route): Route
}

trait WrapperModule {

  lazy val wrappers: Seq[Wrapper] = Seq(
    wire[AccessLog]
  )

}
