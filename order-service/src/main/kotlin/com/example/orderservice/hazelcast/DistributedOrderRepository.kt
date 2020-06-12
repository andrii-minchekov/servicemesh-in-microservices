package com.example.orderservice.hazelcast

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderRepository
import com.hazelcast.config.Config
import com.hazelcast.config.InMemoryFormat
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import com.hazelcast.replicatedmap.ReplicatedMap
import info.jerrinot.subzero.SubZero
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["distributed-cache.enabled"], havingValue = "true")
class DistributedOrderRepository(private val listener: ReplicatedMapEntryListener) : OrderRepository{
    companion object {
        const val REP_MAP_NAME = "replicatedOrders"
        const val DIST_MAP_NAME = "distributedOrders"
    }

    private lateinit var storage: IMap<String, Order>

    init {
        val config = Config();
        SubZero.useAsGlobalSerializer(config);
        config.clusterName = "demo"
        setupDistributedMapConfig(config)
        val instance = Hazelcast.newHazelcastInstance(config)
        storage = instance.getMap(DIST_MAP_NAME)
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

    private fun setupDistributedMapConfig(config: Config) {
        val mapConfig = config.getMapConfig(DIST_MAP_NAME)
        mapConfig.inMemoryFormat = InMemoryFormat.OBJECT
        mapConfig.asyncBackupCount = 1
        mapConfig.backupCount = 0
        mapConfig.isReadBackupData = true
        mapConfig.isStatisticsEnabled = true
    }

    private fun replicatedMap(instance: HazelcastInstance): ReplicatedMap<String, Order> {
        val storage: ReplicatedMap<String, Order>  = instance.getReplicatedMap(REP_MAP_NAME)
        storage.addEntryListener(listener)
        return storage
    }

    private fun setupReplicatedMapConfig(config: Config) {
        val replicatedMapConfig = config.getReplicatedMapConfig(REP_MAP_NAME);
        replicatedMapConfig.inMemoryFormat = InMemoryFormat.OBJECT;
        replicatedMapConfig.isAsyncFillup = false
        replicatedMapConfig.isStatisticsEnabled = true
    }
}