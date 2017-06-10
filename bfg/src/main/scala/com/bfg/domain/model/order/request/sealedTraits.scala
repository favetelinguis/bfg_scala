package com.bfg.domain.model.order.request

/**
  * Created by henke on 2017-06-06.
  */

sealed trait Side
object SideChoices {
  case object BACK extends Side
  case object LAY extends Side
}

sealed trait OrderType
object OrderTypeChoices {
  case object LIMIT extends OrderType
  case object LIMIT_ON_CLOSE extends OrderType
  case object MARKET_ON_CLOSE extends OrderType
}

sealed trait PersistenceType
object PersistenceTypeChoices {
  case object LAPSE extends PersistenceType
  case object PERSIST extends PersistenceType
  case object MARKET_ON_CLOSE extends PersistenceType
}

sealed trait TimeInForce
object TimeInForceChoices {
  case object FILL_OR_KILL extends TimeInForce
}

sealed trait BetTargetType
object BetTargetTypeChoices {
  case object BACKERS_PROFIT extends BetTargetType
  case object PAYOUT extends BetTargetType
}
