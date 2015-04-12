package kz.pilgrimkst.trader.driver.btce

import org.json4s.native.JsonMethods._
import kz.pilgrimkst.trader.model._

object Mapper {
  implicit val formats = org.json4s.DefaultFormats

  def toTicker(instrument: Instrument, s: String): Option[Ticker] = {
    val tree = parse(s, useBigDecimalForDouble = true) \\ "ticker"
    mapToTicker(tree.extractOpt[TickerDTO], instrument)
  }

  def mapToOrders(instrument: Instrument, s: String): Option[Seq[Order]] = {
    val json = parse(s).extractOpt[Map[String, List[List[BigDecimal]]]].orNull
    if (json != null) {
      val asks = json.getOrElse("asks", List()).map(order => Order(PriceWithAmount(order.head, order.tail.head), ASK))
      val bids = json.getOrElse("bids", List()).map(order => Order(PriceWithAmount(order.head, order.tail.head), BID))

      Option(bids ++ asks)
    }

    None
  }

  def mapToTrades(s: String): Option[Seq[Trade]] = {
    //    {"date":1428746701,"price":230.9,"amount":0.0377,"tid":53861155,"price_currency":"USD","item":"BTC","trade_type":"bid"
    parse(s)
      .extractOpt[List[TradeDTO]]
      .map(trades => trades.map(tradeFromTradeDTO))
  }

  private def tradeFromTradeDTO(t: TradeDTO): Trade = {
    val ccyPair: CcyPair = ccyPairFromTrade(t.priceCurrency, t.item)
    Trade(ccyPair, t.tradeType, PriceWithAmount(t.price, t.amount), t.date)
  }

  private def ccyPairFromTrade(c1: String, c2: String): CcyPair = {
    CcyPair(Currency(c1.toUpperCase), Currency(c2.toUpperCase))
  }

  private def mapToTicker(ticker: Option[TickerDTO], instrument: Instrument): Option[Ticker] = {
    ticker.flatMap[Ticker](t => Option(Ticker(instrument, EXCHANGE, t.buy, t.sell, t.vol, t.updated)))
  }

  val EXCHANGE = Exchange("btc-e.com")

  private case class TickerDTO(high: BigDecimal, low: BigDecimal, vol: BigDecimal, vol_cur: BigDecimal, buy: BigDecimal, sell: BigDecimal, updated: Long)

  private case class TradeDTO(priceCurrency: String, item: String, price: BigDecimal, amount: BigDecimal, tradeType: OrderType, date: Long)

}
