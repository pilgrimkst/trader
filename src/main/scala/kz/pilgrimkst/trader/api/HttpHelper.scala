package kz.pilgrimkst.trader.api

import com.typesafe.scalalogging.Logger
import dispatch.{as, Req}
import org.slf4j.LoggerFactory


import dispatch.Defaults._
import dispatch.{Http, Req, as, _}

object HttpHelper {
  val logger = Logger(LoggerFactory.getLogger(HttpHelper.getClass))

  def query[T](request: Req)(f: Either[Any, String] => Either[Any, T]): Either[Any, T] = {
    logger.debug("Querying url {}", request.url)
    Http(request OK as.String).either()
    match {
      case Right(content) => {
        logger.debug("OK, with responded with {}", content)
        f(Right(content))
      }
      case Left(ex) => {
        logger.info("error querying url {}: {}", request.url, ex.toString)
        f(Left(ex))
      }
    }
  }

}
