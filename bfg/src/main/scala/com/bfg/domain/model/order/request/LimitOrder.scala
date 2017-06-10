package com.bfg.domain.model.order.request

/**
  * Created by henke on 2017-06-06.
  */
case class LimitOrder(
                     size: Double,
                     price: Double,
                     persistenceType: PersistenceType,
                     timeInForce: Option[TimeInForce] = None,
                     minFillSize: Option[Double] = None,
                     betTargetType: Option[BetTargetType] = None,
                     betTargetSize: Option[Double] = None
                     )
