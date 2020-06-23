package com.example.orderservice.hazelcast

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderRepository
import com.hazelcast.cache.impl.HazelcastServerCachingProvider
import com.hazelcast.config.*
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import info.jerrinot.subzero.SubZero
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import javax.cache.Cache
import javax.cache.configuration.FactoryBuilder
import javax.cache.expiry.AccessedExpiryPolicy
import javax.cache.expiry.Duration
import javax.cache.integration.CacheLoader
import javax.cache.spi.CachingProvider


@Component
@ConditionalOnProperty(name = ["distributed-cache.enabled"], havingValue = "true")
class DistributedOrderRepository(private val listener: ReplicatedMapEntryListener) : OrderRepository {
    companion object {
        val DIST_MAP_NAME = "distributedOrders"
        val INSTANCE_NAME = "order-service-1"
        val JCACHE_NAME = "cachedOrders" + "-" + UUID.randomUUID().toString()
    }

    private lateinit var instance: HazelcastInstance
    private lateinit var storage: Cache<String, Order>

    init {
        val config = Config();
        SubZero.useAsGlobalSerializer(config);
        config.clusterName = "demo"
        config.instanceName = INSTANCE_NAME
        setupRestApiConfig(config)
        setupDistributedMapConfig(config)

        instance = Hazelcast.newHazelcastInstance(config)

        storage = createCache(instance)
    }

    private fun createCache(instance: HazelcastInstance): Cache<String, Order> {
        val provider: CachingProvider = HazelcastServerCachingProvider(instance);
        val config: CacheConfig<String, Order> = CacheConfig(JCACHE_NAME)
        config.setStoreByValue(false)
        config.setReadThrough(true)
        config.setCacheLoaderFactory(FactoryBuilder.SingletonFactory(cacheLoader()));
        config.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration(TimeUnit.MINUTES, 180)))
        config.setInMemoryFormat(InMemoryFormat.OBJECT)
        config.isStatisticsEnabled = true
        return provider.cacheManager.createCache(JCACHE_NAME, config)
    }

    private fun cacheLoader(): CacheLoader<String, Order> {
        return object : CacheLoader<String, Order> {
            override fun loadAll(keys: MutableIterable<String>?): MutableMap<String, Order> {
                TODO("Not yet implemented")
            }

            override fun load(key: String?): Order {
                println("Hardcoded order will be loaded")
                return Order("hardcodedUserId", arrayListOf())
            }

        }
    }

    private fun setupRestApiConfig(config: Config) {
        val restApiConfig = RestApiConfig()
            .setEnabled(true)
            .enableGroups(RestEndpointGroup.DATA)
        config.networkConfig.restApiConfig = restApiConfig
    }

    override fun save(order: Order): Order {
        if (storage.containsKey(order.id)) {
            throw IllegalArgumentException("Order with id ${order.id} already exist in Repository")
        }
        storage.put(order.id, order)
        instance.getMap<String, Order>(DIST_MAP_NAME).put(order.id, order)
        return order
    }

    override fun findOne(orderId: String): Order? {
        return storage.get(orderId)
    }

    override fun findAll(): Collection<Order> {
        return storage.asSequence().map { it.value }.toList()
    }

    private fun setupDistributedMapConfig(config: Config) {
        val mapConfig = config.getMapConfig(DIST_MAP_NAME)
        mapConfig.inMemoryFormat = InMemoryFormat.OBJECT
        mapConfig.asyncBackupCount = 1
        mapConfig.backupCount = 0
        mapConfig.isReadBackupData = true
        mapConfig.isStatisticsEnabled = true
    }

    @PreDestroy
    fun destroy() {
        instance.shutdown()
    }
}