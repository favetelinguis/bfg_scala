package com.bfg.domain.model.order.request


/**
  * Created by henke on 2017-06-06.
  */
case class PlaceInstruction(
                           orderType: OrderType,
                           selectionId: Long,
                           handicap: Option[Double] = None,
                           side: Side,
                           limitOrder: Option[LimitOrder] = None,
                           limitOnCloseOrder: Option[LimitOnCloseOrder] = None,
                           marketOnCloseOrder: Option[MarketOnCloseOrder] = None,
                           customerOrderRef: Option[String] = None
                           )
