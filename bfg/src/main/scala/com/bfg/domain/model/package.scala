package com.bfg.domain

/**
  * Created by henke on 2017-05-12.
  */
package object model {

  type RequestType = String
  val Authentication: RequestType = "authentication"
  val MarketSubscription: RequestType = "marketSubscription"

  type ResponseType = String
  val Connection = "connection"
  val Status = "status"
  val MCM = "mcm"
  val OrderChangeMessage = "ocm"

  // Streaming types
  type Session = String
  type AppKey = String
}
