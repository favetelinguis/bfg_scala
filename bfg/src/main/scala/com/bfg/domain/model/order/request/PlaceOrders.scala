package com.bfg.domain.model.order.request

import com.bfg.domain.model.Decision

/**
  * Created by henke on 2017-06-06.
  */
case class PlaceOrders(
                      //The market id these orders are to be placed on
                      marketId: String,
                      //The number of place instructions.  The limit of place instructions per request is 200 for the UK/AUS Exchange and 50 for the Italian Exchange.
                      instructions: List[PlaceInstruction],
                      customerRef: Option[String] = None,
                      marketVersion: Option[MarketVersion] = None,
                      customerStrategyRef: Option[String] = None,
                      async: Option[Boolean] = None
                      )

object PlaceOrders {
  def orderFromDecision(decision: Decision): PlaceOrders = {
    ???
  }
}