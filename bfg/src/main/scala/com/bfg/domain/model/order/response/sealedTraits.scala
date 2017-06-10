package com.bfg.domain.model.order.response

/**
  * Created by henke on 2017-06-06.
  */
sealed trait ExecutionReportStatus
object ExecutionReportStatusChoices {
  case object SUCCESS extends ExecutionReportStatus
  case object FAILURE extends ExecutionReportStatus
  case object PROCESSED_WITH_ERRORS extends ExecutionReportStatus
  case object TIMEOUT extends ExecutionReportStatus
}

sealed trait ExecutionReportErrorCode
object ExecutionReportErrorCodeChoices {
  case object ERROR_IN_MATCHER extends ExecutionReportErrorCode
  case object PROCESSED_WITH_ERRORS extends ExecutionReportErrorCode
  case object BET_ACTION_ERROR extends ExecutionReportErrorCode
  case object INVALID_ACCOUNT_STATE extends ExecutionReportErrorCode
  case object INVALID_WALLET_STATUS extends ExecutionReportErrorCode
  case object INSUFFICIENT_FUNDS extends ExecutionReportErrorCode
  case object LOSS_LIMIT_EXCEEDED extends ExecutionReportErrorCode
  case object MARKET_SUSPENDED extends ExecutionReportErrorCode
  case object MARKET_NOT_OPEN_FOR_BETTING extends ExecutionReportErrorCode
  case object DUPLICATE_TRANSACTION extends ExecutionReportErrorCode
  case object INVALID_ORDER extends ExecutionReportErrorCode
  case object INVALID_MARKET_ID extends ExecutionReportErrorCode
  case object PERMISSION_DENIED extends ExecutionReportErrorCode
  case object DUPLICATE_BETIDS extends ExecutionReportErrorCode
  case object NO_ACTION_REQUIRED extends ExecutionReportErrorCode
  case object SERVICE_UNAVAILABLE extends ExecutionReportErrorCode
  case object REJECTED_BY_REGULATOR extends ExecutionReportErrorCode
  case object NO_CHASING extends ExecutionReportErrorCode
  case object REGULATOR_IS_NOT_AVAILABLE extends ExecutionReportErrorCode
  case object TOO_MANY_INSTRUCTIONS extends ExecutionReportErrorCode
  case object INVALID_MARKET_VERSION extends ExecutionReportErrorCode
}

sealed trait InstructionReportStatus
object InstructionReportStatusChoices {
  case object SUCCESS extends InstructionReportStatus
  case object FAILURE extends InstructionReportStatus
  case object TIMEOUT extends InstructionReportStatus
}

sealed trait InstructionReportErrorCode
object InstructionReportErrorCodeChoices {
  case object INVALID_BET_SIZE extends InstructionReportErrorCode
  case object INVALID_RUNNER extends InstructionReportErrorCode
  case object BET_TAKEN_OR_LAPSED extends InstructionReportErrorCode
  case object BET_IN_PROGRESS extends InstructionReportErrorCode
  case object RUNNER_REMOVED extends InstructionReportErrorCode
  case object MARKET_NOT_OPEN_FOR_BETTING extends InstructionReportErrorCode
  case object LOSS_LIMIT_EXCEEDED extends InstructionReportErrorCode
  case object MARKET_NOT_OPEN_FOR_BSP_BETTING extends InstructionReportErrorCode
  case object INVALID_PRICE_EDIT extends InstructionReportErrorCode
  case object INVALID_ODDS extends InstructionReportErrorCode
  case object INSUFFICIENT_FUNDS extends InstructionReportErrorCode
  case object INVALID_PERSISTENCE_TYPE extends InstructionReportErrorCode
  case object ERROR_IN_MATCHER extends InstructionReportErrorCode
  case object INVALID_BACK_LAY_COMBINATION extends InstructionReportErrorCode
  case object ERROR_IN_ORDER extends InstructionReportErrorCode
  case object INVALID_BID_TYPE extends InstructionReportErrorCode
  case object INVALID_BET_ID extends InstructionReportErrorCode
  case object CANCELLED_NOT_PLACED extends InstructionReportErrorCode
  case object RELATED_ACTION_FAILED extends InstructionReportErrorCode
  case object NO_ACTION_REQUIRED extends InstructionReportErrorCode
  case object TIME_IN_FORCE_CONFLICT extends InstructionReportErrorCode
  case object UNEXPECTED_PERSISTENCE_TYPE extends InstructionReportErrorCode
  case object INVALID_ORDER_TYPE extends InstructionReportErrorCode
  case object UNEXPECTED_MIN_FILL_SIZE extends InstructionReportErrorCode
  case object INVALID_CUSTOMER_ORDER_REF extends InstructionReportErrorCode
  case object INVALID_MIN_FILL_SIZE extends InstructionReportErrorCode
}

sealed trait OrderStatus
object OrderStatusChoices {
  case object PENDING extends OrderStatus
  case object EXECUTION_COMPLETE extends OrderStatus
  case object EXECUTABLE extends OrderStatus
  case object EXPIRED extends OrderStatus
}
