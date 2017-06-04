package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{MarketRunnerPrices, RunnerChange, RunnerId, RunnerSnap}

/**
  * Created by henke on 2017-06-04.
  */
trait RunnerSnapService {

  def convertToRunnerSnap(rc: RunnerChange) = {
    val id = RunnerId(rc.id.get, rc.hc)
    val prices = MarketRunnerPrices(
      atl = rc.atl,
      atb = rc.atb,
      trd = rc.trd,
      spb = rc.spb,
      spl = rc.spl,
      batb = rc.batb,
      batl = rc.batl,
      bdatb = rc.bdatb,
      bdatl = rc.batl,
      spn = rc.spn,
      spf = rc.spf,
      ltp = rc.ltp,
      tv = rc.tv
    )
    RunnerSnap(id, prices)
  }

}
