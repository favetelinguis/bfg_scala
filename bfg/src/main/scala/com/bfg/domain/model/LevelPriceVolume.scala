package com.bfg.domain.model

/**
  * Created by henke on 2017-06-03.
  */
case class LevelPriceVolume(
                        level: Int,
                        price: Double,
                        volume: Double
                    ) {
  override def toString: String = s"level $level: $volume @ $price"
}
