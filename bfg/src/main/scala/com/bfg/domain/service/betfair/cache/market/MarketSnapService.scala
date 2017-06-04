package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{MarketChange, MarketSnap}
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by henke on 2017-06-04.
  */
trait MarketSnapService extends RunnerSnapService with LazyLogging {

  def newMarketSnap(cache: MarketSnap, delta: MarketChange) = {
    logger.info("NEW MARKET SNAP!!")
    cache
  }

  def convertToMarketSnap(mc: MarketChange): MarketSnap = {
    logger.info("CONVERT TO MARKET SNAP!!")
    MarketSnap(mc.id, mc.marketDefinition, mc.rc.map(rs => rs.map(convertToRunnerSnap)), mc.tv)
  }

}
