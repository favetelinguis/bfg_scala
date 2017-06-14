package com.bfg.infrastructure.gui.view

import com.bfg.infrastructure.gui.model.MainStageModel
import com.softwaremill.macwire.wire

/**
  * Created by henke on 2017-06-11.
  */
trait ViewModule {

  def mainStageModel: MainStageModel

  lazy val mainStageView = wire[MainStageView]

}
