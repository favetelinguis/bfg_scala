package com.bfg.infrastructure.gui.controller

import com.bfg.domain.model.MarketSnap
import com.bfg.infrastructure.gui.model.MainStageModel
import com.bfg.infrastructure.gui.view.MainStageView

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.collections.ObservableMap.{Add, Remove, Replace}
import scalafx.collections.{ObservableBuffer, ObservableMap}

/**
  * Created by henke on 2017-06-11.
  */
class MainStageController(view: MainStageView, model: MainStageModel) {

  val marketSnaps = ObservableMap.empty[String, MarketSnap]

  def handleMarketSnapMonitor(snap: MarketSnap) = {
    // todo do i really need runLater on this?
    Platform.runLater {
      marketSnaps.update(snap.marketId, snap)
    }
  }

  marketSnaps.onChange((map, change) => {
    change match {
      case Add(key, added) =>
        model.marketSnaps += key
      case Remove(key, removed) =>
        model.marketSnaps -= key
      case Replace(key, added, removed) =>
        model.marketSnaps -= key
        model.marketSnaps += key
    }
  })

}
