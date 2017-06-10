package com.bfg.domain.model.order.response

/**
  * Created by henke on 2017-06-06.
  */
case class PlaceExecutionReport(
                               customerRef: Option[String] = None,
                               status: ExecutionReportStatus,
                               errorCode: Option[ExecutionReportErrorCode],
                               marketId: Option[String],
                               instructionReports: Option[List[PlaceInstructionReport]]
                               )
