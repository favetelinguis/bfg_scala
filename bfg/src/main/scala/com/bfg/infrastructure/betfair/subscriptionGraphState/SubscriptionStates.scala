package com.bfg.infrastructure.betfair.subscriptionGraphState

/**
  * Created by henke on 2017-05-28.
  */
object SubscriptionStates {
  sealed trait State

  // Entry state
  case object Connecting extends State
  case object Authenticating extends State
  case object Subscribing extends State
  case object Subscribed extends State

}
