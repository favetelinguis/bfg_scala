package com.bfg.domain.model

/**
  * Created by henke on 2017-06-03.
  */
case class MarketRunnerPrices(
                               atl: Option[List[PriceVolume]] = None,
                               atb: Option[List[PriceVolume]] = None,
                               trd: Option[List[PriceVolume]] = None,
                               spb: Option[List[PriceVolume]] = None,
                               spl: Option[List[PriceVolume]] = None,
                               batb: Option[List[LevelPriceVolume]] = None,
                               batl: Option[List[LevelPriceVolume]] = None,
                               bdatb: Option[List[LevelPriceVolume]] = None,
                               bdatl: Option[List[LevelPriceVolume]] = None,
                               spn: Option[Double] = None,
                               spf: Option[Double] = None,
                               ltp: Option[Double] = None,
                               tv: Option[Double] = None
                             )
