package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{MarketRunnerPrices, RunnerChange, RunnerId, RunnerSnap}
import org.scalatest._
/**
  * Created by henke on 2017-06-05.
  */
class RunnerSnapServiceTest
  extends FlatSpecLike with Matchers {
  import RunnerSnapService._

  "pairCacheAndDelta" should "handle more runners in cache" in new RunnerSnapContext {
    val cache = List(cacheA, cacheB)
    val delta = List(deltaA)

    pairCacheAndDelta(cache, delta) should contain theSameElementsAs List((cacheA, deltaA), (cacheB, RunnerChange.empty(cacheB.runnerId.selectionId)))

  }

  it should "handle more runners in delta" in new RunnerSnapContext {
    val cache = List(cacheA)
    val delta = List(deltaA, deltaB)

    pairCacheAndDelta(cache, delta) should contain theSameElementsAs List((cacheA, deltaA), (RunnerSnap.empty(cacheB.runnerId.selectionId), deltaB))

  }

}

trait RunnerSnapContext {
  val deltaA = RunnerChange(id = 1)
  val deltaB = RunnerChange(id = 2)
  val cacheA = RunnerSnap(
    runnerId = RunnerId(selectionId = 1),
    prices = MarketRunnerPrices())
  val cacheB = RunnerSnap(
    runnerId = RunnerId(selectionId = 2),
    prices = MarketRunnerPrices())
}