package kz.pilgrimkst.trader.driver.btce

import com.typesafe.scalalogging.Logger
import dispatch.{Req, host}
import kz.pilgrimkst.trader.api.{HttpHelper, Driver}
import kz.pilgrimkst.trader.model._
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory

object BtcEComFetcher extends Driver {
  val logger = Logger(LoggerFactory.getLogger(BtcEComFetcher.getClass))
  implicit val formats = org.json4s.DefaultFormats

  override def ticker(instrument: Instrument): Either[Any, Ticker] = {
    logger.debug("Trying to fetch ticker for {}", instrument)
    HttpHelper.query(Requests.tickerForInstrument(instrument)) {
      case Right(content) => {
        Mapper.toTicker(instrument, content)
      }

      case Left(ex) => {
        logger.info("Error processing request: {}", ex.toString)
        Left(ex)
      }
    }
  }

  override def trades(from: Double, to: Double): Seq[Trade] = ???

  override def depth(): Seq[Order] = ???

  private object Requests {
    val HOST = host("btc-e.com").secure

    def tickerForInstrument(instrument: Instrument): Req = {
      HOST / "api" / "2" / instrumentToString(instrument) / "ticker"
    }

    private def instrumentToString(instrument: Instrument) = instrument match {
      case CcyPair(Currency(c1), Currency(c2)) => (c1 + "_" + c2).toLowerCase
    }

  }

  private object Mapper {
    val EXCHANGE = Exchange("btc-e.com")

    def toTicker(instrument: Instrument, s: String): Either[Any, Ticker] = {
      logger.debug("Converting {} to ticker", s)
      val tree = parse(s, useBigDecimalForDouble = true) \\ "ticker"
      mapToTicker(tree.extractOpt[TickerDTO], instrument) match {
        case Some(ticker) => Right(ticker)
        case None => Left("Can't extract ticker from " + s)
      }
    }

    private def mapToTicker(ticker: Option[TickerDTO], instrument: Instrument): Option[Ticker] = {
      ticker.flatMap[Ticker](t => Option(Ticker(instrument, EXCHANGE, t.buy, t.sell, t.vol, t.updated)))
    }

    private case class TickerDTO(high: BigDecimal, low: BigDecimal, vol: BigDecimal, vol_cur: BigDecimal, buy: BigDecimal, sell: BigDecimal, updated: Long)

  }

}
