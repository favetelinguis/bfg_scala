package model.tradeSystem.decision

import model.tradeSystem.Orders

/**
  * Is private and final needed?
  */
private final case class Decision(
  id: String,
  market: String,
  amount: Long,
  direction: String,
  risk: Double,
  openOrders: Orders
)

object Decision {
  /**
    * Only have the factory in the companion object??
    */
  def apply(a: Int): Decision = Decision("","",3l,"",5,new Orders {})
}
