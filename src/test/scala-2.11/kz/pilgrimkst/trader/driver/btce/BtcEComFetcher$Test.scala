package kz.pilgrimkst.trader.driver.btce

import com.typesafe.scalalogging.Logger
import kz.pilgrimkst.trader.model.{CcyPair, Currency}
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Milliseconds, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.slf4j.LoggerFactory

class BtcEComFetcher$Test extends FeatureSpec with GivenWhenThen with Matchers with ScalaFutures {
  val logger = Logger(LoggerFactory.getLogger(BtcEComFetcher$Test.this.getClass))
  val timeout: Timeout = Timeout(Span(10000, Milliseconds))
  val NON_EXISTENT_PAIR = new CcyPair(Currency.apply("N_A"), Currency.apply("N_A"))
  val BTC_USD = CcyPair(Currency.apply("BTC"), Currency.apply("USD"))
  info(
    """This is driver for getting BTC trades from http://btc-e.com exchange
      it supports following methods""")

  feature("ticker") {
    scenario("for valid ccy pair it should execute ticker handler and pass initialised ticker data into") {
      Given("supported ccy pair" + BTC_USD)
      Then("data should be converted to ticker")

      whenReady(BtcEComFetcher.ticker(BTC_USD), timeout) { ticker =>
        info("Received" + ticker)
        ticker.isDefined shouldBe true
      }

    }

    scenario("for non existent or invalid ccy pair it returns empty option") {
      Given("invalid ccy pair" + NON_EXISTENT_PAIR)
      Then("returned empty ticker option")
      whenReady(BtcEComFetcher.ticker(NON_EXISTENT_PAIR), timeout) { ticker =>
        info("Received" + ticker)
        ticker.isEmpty shouldBe true
      }
    }
  }


  //  "Driver" should "receive non empty order list for bct\\usd" in {
  //    BtcEComFetcher.depth(BTC_USD)(ticker => {
  //      logger.info("retreived {}", ticker)
  //      ticker.isDefined shouldBe true
  //    })
  //  }

}
