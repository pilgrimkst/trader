package kz.pilgrimkst.trader.driver.btce

import kz.pilgrimkst.trader.model.{ASK, BID, Currency, CcyPair}


class Mapper$Test {
  val defaultInstrument = CcyPair(Currency("BCT"), Currency("USD"))
  val ordersJson = """{"asks":[[232.9,26.12662],[232.911,0.010978]],"bids":[[233.643,0.010978],[233.673,0.258]]}"""
//  "Mapper.mapToOrders" should "map json string to orders " in {
//    val orders = Mapper.mapToOrders(defaultInstrument, ordersJson)
//    orders.isRight shouldBe true
//    orders match {
//      case Right(items) =>
//        items.length shouldBe 4
//        items.count(o => BID.eq(o.orderType)) shouldBe 2
//        items.count(o => ASK.eq(o.orderType)) shouldBe 2
//    }
//
//  }


}
