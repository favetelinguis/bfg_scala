package com.bfg.infrastructure.config

import com.softwaremill.tagging._
import com.typesafe.config.ConfigFactory

/**
  * Created by henke on 2017-05-20.
  */
trait ConfigModule {

  private val config = ConfigFactory.load()

  lazy val serverHostConfig: String @@ ServerHostConfig = {
    config.getString("server.host").taggedWith[ServerHostConfig]
  }

  lazy val serverPortConfig: Int @@ ServerPortConfig = {
    config.getInt("server.port").taggedWith[ServerPortConfig]
  }

  lazy val appKeyConfig: String @@ AppKey = {
    config.getString("server.testappkey").taggedWith[AppKey]
  }

  lazy val betfairUserConfig: String @@ BetfairUser = {
    config.getString("server.bfusr").taggedWith[BetfairUser]
  }

  lazy val betfairPwdConfig: String @@ BetfairPwd = {
    config.getString("server.bfpwd").taggedWith[BetfairPwd]
  }

  lazy val p12PwdConfig: String @@ P12Pwd = {
    config.getString("server.p12pwd").taggedWith[P12Pwd]
  }

  lazy val sessionEndpointConfig: String @@ SessionEndpoint = {
    config.getString("server.endpoints.session").taggedWith[SessionEndpoint]
  }

  lazy val keepAliveEndpointConfig: String @@ KeepAliveEndpoint = {
    config.getString("server.endpoints.keepalive").taggedWith[KeepAliveEndpoint]
  }

  lazy val streamEndpointConfig: String @@ StreamEndpoint = {
    config.getString("server.endpoints.streamurl").taggedWith[StreamEndpoint]
  }

  lazy val streamPort: Int @@ StreamPort = {
    config.getInt("server.endpoints.streamport").taggedWith[StreamPort]
  }
}
