package com.bfg.infrastructure.gui.model

import com.softwaremill.macwire.wire
/**
  * Created by henke on 2017-06-11.
  */
trait ModelModule {
  lazy val mainStageModel = wire[MainStageModel]

}
