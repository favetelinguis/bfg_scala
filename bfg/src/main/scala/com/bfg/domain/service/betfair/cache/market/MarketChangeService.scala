package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{MarketChange, MarketSnap}

/**
  * Created by henke on 2017-06-03.
  */
trait MarketChangeService extends MarketSnapService {

  def updateCache(cache: Option[MarketSnap], delta: MarketChange): Option[MarketSnap] = {
    // MarketSnap is None at first message then just return MarketChange
    val newCache = cache.map(c => newMarketSnap(c, delta)).getOrElse(convertToMarketSnap(delta))
    Some(newCache)
  }
}

object MarketChangeService extends MarketChangeService
