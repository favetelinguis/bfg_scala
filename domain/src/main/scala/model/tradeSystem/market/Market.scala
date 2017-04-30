package model.tradeSystem.market

import model.tradeSystem.{Order, Orders}

/**
  * Created by henke on 2017-04-29.
  */
trait Market {
  """
    | Basic contract of a Market aggregate
  """.stripMargin
  def id: String
  def orders: Orders
}

case class OrderMarket(
                   id: String,
                   orders: Orders
                 ) extends Market
