package kz.pilgrimkst.trader.driver.btce

import com.typesafe.scalalogging.Logger
import kz.pilgrimkst.trader.model.{Ticker, Currency, CcyPair}
import org.scalatest.{FlatSpec, Matchers, FunSuite}
import org.slf4j.LoggerFactory

class BtcEComFetcher$Test extends FlatSpec with Matchers {
  val logger = Logger(LoggerFactory.getLogger(BtcEComFetcher$Test.this.getClass))
  val NON_EXISTENT_PAIR = new CcyPair(Currency.apply("N_A"), Currency.apply("N_A"))
  val BTC_USD = CcyPair(Currency.apply("BTC"), Currency.apply("USD"))

  "Driver" should "receive non empty ticker for bct\\usd" in {
    val result = BtcEComFetcher.ticker(BTC_USD)
    logger.info("retreived {}", result)
    result.isRight shouldBe true
  }

  "Driver" should "return empty ticker for non supported instrument" in {
    val result = BtcEComFetcher.ticker(NON_EXISTENT_PAIR)
    logger.info("retreived {}", result)
    result.isLeft shouldBe true
  }

}
