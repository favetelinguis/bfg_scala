package com.bfg.domain.service

import cats.data.Kleisli
import cats.implicits._

/**
  * Created by henke on 2017-05-06.
  * Name the module after the behavior it models
  */
trait Trading[Account, Market, Order, ClientOrder, Execution, Trade] {
  def clientOrders: Kleisli[List, ClientOrder, Order]
  def execute(m: Market,  a: Account): Kleisli[List, Order, Execution]
  def allocate(as: List[Account]): Kleisli[List, Execution, Trade]

  def tradeGeneration(
                     market: Market,
                     broker: Account,
                     clientAccounts: List[Account]
                     ): Kleisli[List, ClientOrder, Trade] = {
    clientOrders andThen
      execute(market, broker) andThen
        allocate(clientAccounts)
  }
}

object Trading {

  // Phantom types are for when the algebra has the same return types
  sealed trait State
  object State {
    sealed trait Executed extends State
    sealed trait Allocated extends State
  }
}
