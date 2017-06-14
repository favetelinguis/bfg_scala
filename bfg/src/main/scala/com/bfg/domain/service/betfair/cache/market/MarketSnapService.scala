package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{MarketChange, MarketSnap}
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by henke on 2017-06-04.
  */
trait MarketSnapService extends RunnerSnapService with LazyLogging {

  def newMarketSnap(cache: MarketSnap, delta: MarketChange) = {
    val newMarketDefinition = if (delta.marketDefinition.isEmpty) cache.marketDefinition else delta.marketDefinition
    val newTv = if (delta.tv.isEmpty) cache.tradedVolume else delta.tv
    MarketSnap(cache.marketId, newMarketDefinition, mergeRunnersChangedToRunnersSnap(cache.marketRunners, delta.rc), newTv)
  }

  def convertToMarketSnap(mc: MarketChange): MarketSnap = {
    MarketSnap(mc.id, mc.marketDefinition, mc.rc.map(rs => rs.map(convertToRunnerSnap)), mc.tv)
  }

}
