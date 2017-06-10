package com.bfg.infrastructure

/**
  * Created by henke on 2017-05-20.
  */
package object config {

  // HTTP server
  trait ServerHostConfig
  trait ServerPortConfig

  // Betfair
  trait StreamEndpoint
  trait StreamPort
  trait KeepAliveEndpoint
  trait SessionEndpoint
  trait AppKey
  trait BetfairUser
  trait BetfairPwd
  trait P12Pwd
  trait BetingRestEndpoint

  // Trader
  trait WindowSize
}
