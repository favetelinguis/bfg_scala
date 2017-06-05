package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.PriceVolume

/**
  * Created by henke on 2017-06-05.
  */
trait PriceVolumeService {

  //todo not sure if this is needed
  //used if runners can be added or removed after the intial message
  def syncPriceVolume(cache: Option[List[PriceVolume]], delta: Option[List[PriceVolume]]): Option[List[PriceVolume]] = {
    val data = (cache, delta)
    data match {
      case (None, ds @ Some(_)) => ds
      case (cs @ Some(_), None) => cs
      case (Some(cs), Some(ds)) => Some(updatePrice(cs,ds))
      case _ => None
    }
  }

  def updatePrice(cache: List[PriceVolume], delta: List[PriceVolume]): List[PriceVolume] = {
    val idsInCache = cache.map(_.price).toSet
    val idsInDelta = delta.map(_.price).toSet
    val allIds = idsInCache.union(idsInDelta).toList
    val deltaMap: Map[Double, PriceVolume] =
      delta.map(x => x.price -> x).toMap
    val cacheMap: Map[Double, PriceVolume] =
      cache.map(x => x.price -> x).toMap

    allIds.map {price =>
      // Ids in both cache and delta use delta
      // Ids in only cache keep keep cache
      // Remove all values with zero volume
      if (deltaMap.isDefinedAt(price)) deltaMap.get(price).get else cacheMap.get(price).get
    }.filter(d => d.volume.toInt != 0)
  }

}

object PriceVolumeService extends PriceVolumeService
