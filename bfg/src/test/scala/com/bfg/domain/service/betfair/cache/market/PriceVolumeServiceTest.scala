package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.PriceVolume
import org.scalatest._
/**
  * Created by henke on 2017-06-05.
  */
class PriceVolumeServiceTest
  extends FlatSpecLike with Matchers {
  import PriceVolumeService._

  "updatePrice" should "replace all values in cache for wich there is a delta value" in new PriceVolumeContext {
    val cache = List(cpv1)
    val delta = List(dpv1)

    updatePrice(cache, delta) should contain theSameElementsAs List(dpv1)
  }

  it should "add value to cach if delta not in cach" in new PriceVolumeContext {
    val cache = List(cpv1)
    val delta = List(dpv2)

    updatePrice(cache, delta) should contain theSameElementsAs List(cpv1, dpv2)
  }

  it should "remove from cache all delta where size in delta = 0" in new PriceVolumeContext {
    val cache = List(cpv1, cpv2)
    val delta = List(cpv2Remove)

    updatePrice(cache, delta) should contain theSameElementsAs List(cpv1)

  }

  it should "handle a mix of all the above" in new PriceVolumeContext {
    val cache = List(cpv1, cpv2, cpv3)
    val delta = List(dpv1, cpv2Remove, dpv4)

    updatePrice(cache, delta) should contain theSameElementsAs List(dpv1, cpv3, dpv4)
  }

}

trait PriceVolumeContext {

  val cpv1 = PriceVolume(1.0, 10.0)
  val cpv2 = PriceVolume(2.0, 20.0)
  val cpv3 = PriceVolume(3.0, 30.0)

  val dpv1 = PriceVolume(1.0, 100.0)
  val dpv2 = PriceVolume(2.0, 200.0)
  val dpv4 = PriceVolume(4.0, 400.0)

  val cpv2Remove = PriceVolume(2.0, 0.0)

}