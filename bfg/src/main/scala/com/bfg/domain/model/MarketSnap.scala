package com.bfg.domain.model

/**
  * Created by henke on 2017-06-03.
  */
case class MarketSnap(
                       marketId: String,
                       marketDefinition: Option[MarketDefinition],
                       marketRunners: Option[List[RunnerSnap]],
                       tradedVolume: Option[Double])
