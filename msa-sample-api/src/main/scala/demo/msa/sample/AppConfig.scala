package demo.msa.sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
@SpringBootApplication(scanBasePackages = Array("demo.msa"))
class AppConfig {
  @RequestMapping(name = "HelloService", method = Array(RequestMethod.GET), path = Array("/hello"))
  def hello(): String = "Hello, My Baby Girl!"
}
