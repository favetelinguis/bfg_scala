package com.bfg.domain.model

/**
  * Created by henke on 2017-06-03.
  */
case class MarketRunnerPrices(
                               atl: Option[List[PriceVolume]],
                               atb: Option[List[PriceVolume]],
                               trd: Option[List[PriceVolume]],
                               spb: Option[List[PriceVolume]],
                               spl: Option[List[PriceVolume]],
                               batb: Option[List[LevelPriceVolume]],
                               batl: Option[List[LevelPriceVolume]],
                               bdatb: Option[List[LevelPriceVolume]],
                               bdatl: Option[List[LevelPriceVolume]],
                               spn: Option[Double],
                               spf: Option[Double],
                               ltp: Option[Double],
                               tv: Option[Double]
                             )
