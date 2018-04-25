package demo.msa.framework.registry

import demo.msa.common.LoggerSupport
import java.util.concurrent.CountDownLatch
import org.apache.zookeeper.Watcher.Event
import org.apache.zookeeper._
import org.springframework.stereotype.Component

@Component
class ServiceRegistryImpl extends ServiceRegistry with LoggerSupport {

  private var zk: Option[ZooKeeper] = None

  def this(zkServers: String) = {
    this()
    zk = try {
      Some(
        new ZooKeeper(zkServers, ServiceRegistryImpl.SESSION_TIMEOUT, new Watcher {
          override def process(watchedEvent: WatchedEvent): Unit = {
            if (watchedEvent.getState == Event.KeeperState.SyncConnected) {
              ServiceRegistryImpl.latch.countDown()
            }
          }
        })
      )
    } catch {
      case e: Exception =>
        log.error(s"create zookeeper client failure, $e")
        None
    }
  }

  def register(serviceName: String, serviceAddress: String): Unit = {
    ServiceRegistryImpl.latch.await()
    try {
      zk match {
        case Some(zookeeper) =>
          log.debug(s"serviceName($serviceName) serviceAddress($serviceAddress)")
          val registryPath = ServiceRegistryImpl.REGISTRY_PATH
          if (zookeeper.exists(registryPath, false) == null) {
            zookeeper.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
            log.info(s"create registry node: $registryPath")
          }

          val servicePath = s"$registryPath/$serviceName"
          if (zookeeper.exists(servicePath, false) == null) {
            zookeeper.create(servicePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
            log.info(s"create service node: $servicePath")
          }

          val addressPath = s"$servicePath/address-"
          val addressNode = zookeeper.create(
            addressPath, serviceAddress.getBytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL)
          log.info(s"create address node: $addressNode => $serviceAddress")
        case None =>
          log.info(s"no zookeeper client")
      }
    } catch {
      case e: Exception => log.error(s"$e")
    }
  }

}

object ServiceRegistryImpl {
  private val latch = new CountDownLatch(1)
  private final val REGISTRY_PATH = "/registry"
  private final val SESSION_TIMEOUT = 5000
}
