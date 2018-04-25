package demo.msa.framework.registry

import demo.msa.common.LoggerSupport
import javax.servlet.{ServletContextEvent, ServletContextListener}
import org.springframework.beans.factory.annotation.{Autowired, Qualifier, Value}
import org.springframework.stereotype.Component
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import scala.collection.JavaConverters._

@Component
class WebListener extends ServletContextListener with LoggerSupport {
  @Value("${server.address}")
  private var serverAddress: String = _

  @Value("${server.port}")
  private var serverPort: String = _

  @Autowired
  @Qualifier("serviceRegistry")
  private var serviceRegistry: ServiceRegistry = _

  override def contextInitialized(servletContextEvent: ServletContextEvent): Unit = {
    log.debug(s"contextInitialized")
    val servletContext = servletContextEvent.getServletContext
    val applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
    val mapping = applicationContext.getBean(classOf[RequestMappingHandlerMapping])
    val infoMap = mapping.getHandlerMethods.asScala
    log.debug(s"contextInitialized: infoMap(${infoMap.keys})")
    for (info <- infoMap.keySet) {
      val serviceName = info.getName
      if (serviceName != null) {
        serviceRegistry.register(serviceName, s"$serverAddress:$serverPort")
      }
    }

  }

  override def contextDestroyed(servletContextEvent: ServletContextEvent): Unit = {

  }
}
