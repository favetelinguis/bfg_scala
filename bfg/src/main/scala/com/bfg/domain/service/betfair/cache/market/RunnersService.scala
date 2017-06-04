package com.bfg.domain.service.betfair.cache.market

import com.bfg.domain.model.{RunnerChange, RunnerDefinition}

/**
  * Created by henke on 2017-06-03.
  */
trait RunnersService {

  // Pair all cache values with delta values
  def runnersChange(cache: List[RunnerChange], delta: List[RunnerChange]): Seq[(RunnerChange, RunnerChange)] = {
    val deltaMap: Map[Long, RunnerChange] =
      delta.flatMap(x => x.id.map(id => id -> x)).toMap

    val cacheDeltaPairs: List[(RunnerChange, RunnerChange)] = cache.map { c =>
      c -> c.id.flatMap(deltaMap.get).getOrElse(RunnerChange.empty)
    }

    cacheDeltaPairs
  }
}

object RunnersService extends RunnersService
