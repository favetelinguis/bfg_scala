package com.bfg.domain.model

/**
  * Created by henke on 2017-06-03.
  */
case class RunnerSnap(
                           runnerId: RunnerId,
                           prices: MarketRunnerPrices
                           )

object RunnerSnap {
  def empty(selectionId: Long) = RunnerSnap(RunnerId(selectionId = selectionId), MarketRunnerPrices())
}
