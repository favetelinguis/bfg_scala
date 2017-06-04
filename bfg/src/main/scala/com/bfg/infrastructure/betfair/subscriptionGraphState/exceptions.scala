package com.bfg.infrastructure.betfair.subscriptionGraphState

/**
  * Created by henke on 2017-05-28.
  */
case class  BetfairConnectionFailedException(msg: String) extends RuntimeException(msg)
case class  BetfairAuthenticationFailedException(msg: String) extends RuntimeException(msg)
case class  BetfairSubscriptionFailedException(msg: String) extends RuntimeException(msg)
