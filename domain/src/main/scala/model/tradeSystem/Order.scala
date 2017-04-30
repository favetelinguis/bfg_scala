package model.tradeSystem

/**
  * Created by henke on 2017-04-29.
  */
trait Order {

}


object Order {
  """
    | Factory method, call like Order()
  """.stripMargin

  // Not in the book to place them in Object or make private
  private case class BackOrder() extends Order
  private case class LayOrder() extends Order

  def apply(s: String): Order = new BackOrder()
}
