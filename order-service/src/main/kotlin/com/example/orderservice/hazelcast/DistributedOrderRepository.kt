package com.example.orderservice.hazelcast

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderRepository
import com.hazelcast.config.Config
import com.hazelcast.config.InMemoryFormat
import com.hazelcast.core.Hazelcast
import com.hazelcast.replicatedmap.ReplicatedMap
import info.jerrinot.subzero.SubZero
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["distributed-cache.enabled"], havingValue = "true")
class DistributedOrderRepository(private val listener: ReplicatedMapEntryListener) : OrderRepository{
    companion object {
        const val MAP_NAME = "orders"
    }

    private lateinit var storage: ReplicatedMap<String, Order>

    init {
        val config = Config();
        SubZero.useAsGlobalSerializer(config);
        val replicatedMapConfig = config.getReplicatedMapConfig(MAP_NAME);
        replicatedMapConfig.inMemoryFormat = InMemoryFormat.OBJECT;
        replicatedMapConfig.isAsyncFillup = false
        replicatedMapConfig.isStatisticsEnabled = true

        val hazelcastInstance = Hazelcast.newHazelcastInstance(config)
        storage = hazelcastInstance.getReplicatedMap(MAP_NAME)
        storage.addEntryListener(listener)
    }

    override fun save(order: Order): Order {
        if(storage.put(order.id, order) != null) {
            throw IllegalArgumentException("Order with id ${order.id} already exist in Repository")
        }
        return order;
    }

    override fun findOne(orderId: String): Order? {
        return storage[orderId]
    }

    override fun findAll(): Collection<Order> {
        return storage.values
    }
}