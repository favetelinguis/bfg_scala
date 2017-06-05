package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.LevelPriceVolume

/**
  * Created by henke on 2017-06-05.
  */
trait LevelPriceVolumeService {

  //todo not sure if this is needed
  //used if runners can be added or removed after the intial message
  def syncLevelPriceVolume(cache: Option[List[LevelPriceVolume]], delta: Option[List[LevelPriceVolume]]): Option[List[LevelPriceVolume]] = {
    val data = (cache, delta)
    data match {
      case (None, ds@Some(_)) => ds
      case (cs@Some(_), None) => cs
      case (Some(cs), Some(ds)) => Some(updateLevelPrice(cs, ds))
      case _ => None
    }
  }

  def updateLevelPrice(cache: List[LevelPriceVolume], delta: List[LevelPriceVolume]): List[LevelPriceVolume] = {
    val idsInCache = cache.map(_.level).toSet
    val idsInDelta = delta.map(_.level).toSet
    val allIds = idsInCache.union(idsInDelta).toList
    val deltaMap: Map[Int, LevelPriceVolume] =
      delta.map(x => x.level -> x).toMap
    val cacheMap: Map[Int, LevelPriceVolume] =
      cache.map(x => x.level -> x).toMap

    allIds.map {price =>
      // Ids in both cache and delta use delta
      // Ids in only cache keep keep cache
      // Remove all values with zero volume
      if (deltaMap.isDefinedAt(price)) deltaMap.get(price).get else cacheMap.get(price).get
    }.filter(d => d.volume.toInt != 0)
  }

}

object LevelPriceVolumeService extends LevelPriceVolumeService
