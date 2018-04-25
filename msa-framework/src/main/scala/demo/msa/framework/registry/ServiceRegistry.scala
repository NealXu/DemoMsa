package demo.msa.framework.registry

/**
 * 服务注册表
 */
abstract class ServiceRegistry {
  def register(serviceName: String, serviceAddress: String): Unit
}
