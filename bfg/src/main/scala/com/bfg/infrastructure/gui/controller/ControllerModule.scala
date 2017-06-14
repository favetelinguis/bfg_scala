package com.bfg.infrastructure.gui.controller

import com.bfg.infrastructure.gui.model.MainStageModel
import com.bfg.infrastructure.gui.view.MainStageView
import com.softwaremill.macwire.wire
/**
  * Created by henke on 2017-06-11.
  */
trait ControllerModule {

  def mainStageView: MainStageView
  def mainStageModel: MainStageModel

  lazy val mainStageController = wire[MainStageController]

}
