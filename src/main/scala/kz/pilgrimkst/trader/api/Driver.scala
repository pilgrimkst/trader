package kz.pilgrimkst.trader.api

import kz.pilgrimkst.trader.model.{Instrument, Order, Ticker, Trade}

trait Driver {

  def ticker(instrument: Instrument): Either[Any, Ticker]
  def trades(from: Double, to: Double): Seq[Trade]

  def depth(): Seq[Order]

}
