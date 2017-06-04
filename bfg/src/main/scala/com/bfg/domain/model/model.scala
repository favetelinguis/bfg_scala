package com.bfg.domain.model

/**
  * Created by henke on 2017-05-18.
  */
case class SessionToken(sessionToken: String, loginStatus: String)
case class KeepAliveResponse(token: String, product: String, status: String, error: String)
