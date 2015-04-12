package kz.pilgrimkst.trader.driver.btce

import com.typesafe.scalalogging.Logger
import dispatch.Defaults._
import dispatch._
import kz.pilgrimkst.trader.driver.btce.Mapper.{mapToOrders, mapToTrades, toTicker}
import kz.pilgrimkst.trader.driver.btce.Requests.{depthFor, tickerFor}
import kz.pilgrimkst.trader.model._
import org.slf4j.LoggerFactory


object BtcEComFetcher {
  type Consumer[T] = Option[T] => Unit

  val logger = Logger(LoggerFactory.getLogger(BtcEComFetcher.getClass))

  def ticker(instrument: Instrument) = {
    logger.debug("Trying to fetch ticker for {}", instrument)
    fetch(tickerFor(instrument))(toTicker(instrument, _))
  }

  def trades(from: Long, to: Long) = {
    logger.debug("Trying to fetch trades from {} to {}", from.toString, to.toString)
    fetch(Requests.tradesForInstrument(from, to))(mapToTrades)
  }

  def depth(instrument: Instrument) = {
    logger.debug("Trying to fetch orders for {}", instrument)
    fetch(depthFor(instrument))(mapToOrders(instrument, _))
  }

  def query[T](request: Req)(onComplete: String => T) = {
    for {
      response <- Http(request OK as.String)
    } yield {
      onComplete(response)
    }
  }

  def fetch[T](r: Req)(extractor: String => Option[T]) = {
    query(r)(extractor)
  }

}


private object Requests {
  val HOST = host("btc-e.com").secure

  def tickerFor(implicit instrument: Instrument): Req = {
    hostWithInstrument(instrument) / "ticker"
  }

  def tradesForInstrument(from: Long, to: Long): Req = {
    HOST / "trades"
  }

  def depthFor(implicit instrument: Instrument): Req = {
    hostWithInstrument(instrument) / "depth"
  }

  private def instrumentToString(instrument: Instrument) = instrument match {
    case CcyPair(Currency(c1), Currency(c2)) => (c1 + "_" + c2).toLowerCase
  }

  private def hostWithInstrument(instrument: Instrument): Req = {
    HOST / "api" / "2" / instrumentToString(instrument)
  }
}



