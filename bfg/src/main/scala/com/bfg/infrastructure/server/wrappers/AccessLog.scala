package com.bfg.infrastructure.server.wrappers
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by henke on 2017-05-20.
  */
class AccessLog extends Wrapper with LazyLogging {
  override def wrap(route: Route): Route = {
    logger.info("LOGGING FROM AccessLog")
    route
  }
}
