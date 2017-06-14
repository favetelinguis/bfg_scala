package com.bfg.infrastructure.gui

import com.bfg.infrastructure.gui.controller.ControllerModule
import com.bfg.infrastructure.gui.model.ModelModule
import com.bfg.infrastructure.gui.view.ViewModule

/**
  * Created by henke on 2017-06-11.
  */
trait GUI extends ControllerModule with ModelModule with ViewModule
