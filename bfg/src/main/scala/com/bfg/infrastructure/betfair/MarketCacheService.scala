package com.bfg.infrastructure.betfair

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.bfg.domain.model.{MarketChange, MarketChangeMessage, MarketSnap}
import com.bfg.domain.service.betfair.cache.market.MarketChangeService
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by henke on 2017-06-03.
  */
class MarketCacheService() extends MarketChangeService with LazyLogging {

  val onlyChangeMessage: Flow[MarketChangeMessage, List[MarketChange], NotUsed] = Flow[MarketChangeMessage]
    .collect {
      case mcm if ! mcm.mc.isEmpty => mcm.mc.get
    }

  val splitMarketChanges: Flow[List[MarketChange], MarketChange, NotUsed] = Flow[List[MarketChange]]
    .mapConcat(identity)

  val holdCache = Flow[MarketChange]
    .scan(Option.empty[MarketSnap])((cache: Option[MarketSnap], newMarketChange: MarketChange) => updateCache(cache, newMarketChange))
    .collect {
      // Drops the first empty cache message
      case Some(cache) => cache
    }
    //.drop(1) // Drops the first empty value from scan


    val splitByMarketId = Flow[MarketChange]
    .groupBy(Int.MaxValue, _.id)
    .via(holdCache).async
    .mergeSubstreams

  val marketCache = onlyChangeMessage.via(splitMarketChanges).via(splitByMarketId)
}
