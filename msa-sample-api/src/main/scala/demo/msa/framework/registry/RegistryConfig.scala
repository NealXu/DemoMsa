package demo.msa.framework.registry

import demo.msa.common.LoggerSupport
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
@ConfigurationProperties(prefix = "registry")
class RegistryConfig extends LoggerSupport {
  private var servers: String = _

  @Bean
  def serviceRegistry(): ServiceRegistry = {
    new ServiceRegistryImpl(servers)
  }

  def setServers(servers: String): Unit = {
    log.debug(s"set servers($servers)")
    this.servers = servers
  }
}
