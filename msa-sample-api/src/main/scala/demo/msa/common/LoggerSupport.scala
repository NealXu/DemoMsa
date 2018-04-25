package demo.msa.common

import org.slf4j.{Logger, LoggerFactory}

trait LoggerSupport {
  protected val log: Logger = LoggerFactory.getLogger(this.getClass)
}
