package com.bfg.domain.model.order.response

import java.time.Instant

import com.bfg.domain.model.order.request.PlaceInstruction

/**
  * Created by henke on 2017-06-06.
  */
case class PlaceInstructionReport(
                                 status: InstructionReportStatus,
                                 errorCode: Option[InstructionReportErrorCode] = None,
                                 orderStatus: Option[OrderStatus] = None,
                                 instruction: PlaceInstruction,
                                 betId: Option[String],
                                 placeDate: Option[Instant] = None,
                                 averagePriceMatched: Option[Double] = None,
                                 sizeMatched: Option[Double] = None
                                 )
