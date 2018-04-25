package demo.msa.sample

import demo.msa.common.LoggerSupport
import org.springframework.boot.SpringApplication

object SampleApplication extends LoggerSupport {

  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[AppConfig])
  }
}
