package kz.pilgrimkst.trader.model

case class Trade(instrument: Instrument, orderType: OrderType, priceWithAmount: PriceWithAmount, timestamp: Long)

case class PriceWithAmount(price: BigDecimal, amount: BigDecimal)

case class Order(priceWithAmount: PriceWithAmount, orderType: OrderType)

case class Ticker(s: Instrument, ex: Exchange, bid: BigDecimal, ask: BigDecimal, volume: BigDecimal, timestamp: Long)

case class Exchange(code: String)

trait Instrument

case class CcyPair(c1: Currency, c2: Currency) extends Instrument

case class Currency(code: String)

sealed trait OrderType

case object BID extends OrderType

case object ASK extends OrderType
