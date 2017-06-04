package com.bfg.domain.model

/**
  * Created by henke on 2017-06-03.
  */
case class PriceVolume(
                   price: Double,
                   volume: Double
               ) {

  override def toString = s"$volume @ $price"
}
