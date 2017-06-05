package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.LevelPriceVolume
import org.scalatest._
/**
  * Created by henke on 2017-06-05.
  */
class LevelPriceVolumeTest
  extends FlatSpecLike with Matchers {
  import LevelPriceVolumeService._

  "updatePrice" should "replace all values in cache for wich there is a delta value" in new LevelPriceVolumeContext {
    val cache = List(cpv1)
    val delta = List(dpv1)

    updateLevelPrice(cache, delta) should contain theSameElementsAs List(dpv1)
  }

  it should "add value to cach if delta not in cach" in new LevelPriceVolumeContext {
    val cache = List(cpv1)
    val delta = List(dpv2)

    updateLevelPrice(cache, delta) should contain theSameElementsAs List(cpv1, dpv2)
  }

  it should "remove from cache all delta where size in delta = 0" in new LevelPriceVolumeContext {
    val cache = List(cpv1, cpv2)
    val delta = List(cpv2Remove)

    updateLevelPrice(cache, delta) should contain theSameElementsAs List(cpv1)

  }

  it should "handle a mix of all the above" in new LevelPriceVolumeContext {
    val cache = List(cpv1, cpv2, cpv3)
    val delta = List(dpv1, cpv2Remove, dpv4)

    updateLevelPrice(cache, delta) should contain theSameElementsAs List(dpv1, cpv3, dpv4)
  }

}

trait LevelPriceVolumeContext {
  val cpv1 = LevelPriceVolume(1, 1.0, 10.0)
  val cpv2 = LevelPriceVolume(2, 2.0, 20.0)
  val cpv3 = LevelPriceVolume(3, 3.0, 30.0)

  val dpv1 = LevelPriceVolume(1, 1.0, 100.0)
  val dpv2 = LevelPriceVolume(2, 2.0, 200.0)
  val dpv4 = LevelPriceVolume(4, 4.0, 400.0)

  val cpv2Remove = LevelPriceVolume(2, 2.0, 0.0)

}