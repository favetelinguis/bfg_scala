package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{MarketRunnerPrices, RunnerChange, RunnerId, RunnerSnap}

/**
  * Created by henke on 2017-06-04.
  */
trait RunnerSnapService extends PriceVolumeService with LevelPriceVolumeService {

  def mergeRunnersChangedToRunnersSnap(cache: Option[List[RunnerSnap]], delta: Option[List[RunnerChange]]): Option[List[RunnerSnap]] = {
    for {
      c <- cache
      d <- delta
    } yield merge(c,d)
  }

  def merge(cache: List[RunnerSnap], delta: List[RunnerChange]): List[RunnerSnap] = {
    pairCacheAndDelta(cache, delta).map(syncPrices)
  }

  def pairCacheAndDelta(cache: List[RunnerSnap], delta: List[RunnerChange]): List[(RunnerSnap, RunnerChange)] = {
    val idsInCache = cache.map(_.runnerId.selectionId).toSet
    val idsInDelta = delta.map(_.id).toSet
    val allIds = idsInCache.union(idsInDelta).toList
    val deltaMap: Map[Long, RunnerChange] =
      delta.map(x => x.id -> x).toMap
    val cacheMap: Map[Long, RunnerSnap] =
      cache.map(x => x.runnerId.selectionId -> x).toMap
    val cacheUnionList = allIds.map(id => cacheMap.getOrElse(id, RunnerSnap.empty(id)))
    val deltaUnionList = allIds.map(id => deltaMap.getOrElse(id, RunnerChange.empty(id)))
    val sortedCache = cacheUnionList.sortWith((r1, r2) => r1.runnerId.selectionId < r2.runnerId.selectionId)
    val sortedDelta = deltaUnionList.sortWith((r1, r2) => r1.id < r2.id)

    sortedCache.zip(sortedDelta)
  }

  def syncPrices(pair: (RunnerSnap, RunnerChange)): RunnerSnap = pair match {
    case (cache: RunnerSnap, delta: RunnerChange) =>
      val newHc = if (delta.hc.isEmpty) cache.runnerId.handicap else delta.hc
      val id = RunnerId(delta.id, newHc)
      val prices = MarketRunnerPrices(
        atl = syncPriceVolume(cache.prices.atl, delta.atl),
        atb = syncPriceVolume(cache.prices.atb, delta.atb),
        trd = syncPriceVolume(cache.prices.trd, delta.trd),
        spb = syncPriceVolume(cache.prices.spb, delta.spb),
        spl = syncPriceVolume(cache.prices.spl, delta.spl),

        batb = syncLevelPriceVolume(cache.prices.batb, delta.batb),
        batl = syncLevelPriceVolume(cache.prices.batl, delta.batl),
        bdatb = syncLevelPriceVolume(cache.prices.bdatb, delta.bdatb),
        bdatl = syncLevelPriceVolume(cache.prices.bdatl, delta.bdatl),

        spn = if (delta.spn.isEmpty) cache.prices.spn else delta.spn,
        spf = if (delta.hc.isEmpty) cache.prices.spf else delta.spf,
        ltp = if (delta.hc.isEmpty) cache.prices.ltp else delta.ltp,
        tv = if (delta.hc.isEmpty) cache.prices.tv else delta.tv
      )
      RunnerSnap(id, prices)
  }

  def convertToRunnerSnap(rc: RunnerChange) = {
    val id = RunnerId(rc.id, rc.hc)
    val prices = MarketRunnerPrices(
      atl = rc.atl,
      atb = rc.atb,
      trd = rc.trd,
      spb = rc.spb,
      spl = rc.spl,
      batb = rc.batb,
      batl = rc.batl,
      bdatb = rc.bdatb,
      bdatl = rc.batl,
      spn = rc.spn,
      spf = rc.spf,
      ltp = rc.ltp,
      tv = rc.tv
    )
    RunnerSnap(id, prices)
  }

}

object RunnerSnapService extends RunnerSnapService
