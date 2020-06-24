package com.example.orderservice.hazelcast

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderRepository
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.configuration.CacheConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.cache.Cache
import javax.cache.CacheManager
import javax.cache.expiry.AccessedExpiryPolicy
import javax.cache.expiry.Duration
import javax.cache.integration.CacheLoader


@Component
@ConditionalOnProperty(name = ["distributed-cache.enabled"], havingValue = "true")
class DistributedOrderRepository(private val cacheManager: CacheManager) : OrderRepository {
    companion object {
        val JCACHE_NAME = "cachedOrders" + "-" + UUID.randomUUID().toString()
    }

    private lateinit var storage: Cache<String, Order>

    init {
        val config: CacheConfiguration<String, Order> = CacheConfiguration()
        config.cacheMode = CacheMode.REPLICATED
        config.isStoreByValue = false
        //config.setReadThrough(true)
        //config.setCacheLoaderFactory(FactoryBuilder.SingletonFactory(cacheLoader()));
        config.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration(TimeUnit.MINUTES, 180)))
        config.isStatisticsEnabled = true
        storage = cacheManager.createCache(JCACHE_NAME, config)
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

    override fun save(order: Order): Order {
        if (storage.containsKey(order.id)) {
            throw IllegalArgumentException("Order with id ${order.id} already exist in Repository")
        }
        storage.put(order.id, order)
        return order
    }

    override fun findOne(orderId: String): Order? {
        return storage.get(orderId)
    }

    override fun findAll(): Collection<Order> {
        return storage.asSequence().map { it.value }.toList()
    }
}