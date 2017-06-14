package com.bfg.infrastructure.gui.model

import com.bfg.domain.model.MarketSnap
import scalafx.Includes._

import scalafx.collections.{ObservableBuffer, ObservableMap}

/**
  * Created by henke on 2017-06-11.
  */
class MainStageModel {

  var marketSnaps = new ObservableBuffer[String]()
}
