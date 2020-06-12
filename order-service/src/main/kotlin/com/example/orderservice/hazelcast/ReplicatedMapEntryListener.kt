package com.example.orderservice.hazelcast

import com.example.orderservice.domain.order.Order
import com.hazelcast.core.EntryEvent
import com.hazelcast.core.EntryListener
import com.hazelcast.map.MapEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ReplicatedMapEntryListener : EntryListener<String, Order> {
    private val log = LoggerFactory.getLogger(ReplicatedMapEntryListener::class.java)

    override fun entryExpired(event: EntryEvent<String, Order>) {
        log.info("Order ${event.value.id} was Expired by member ${event.member.address} at ${time()}")
    }

    override fun entryEvicted(event: EntryEvent<String, Order>) {
        log.info("Order ${event.value.id} was Evicted by member ${event.member.address} at ${time()}")
    }

    override fun entryUpdated(event: EntryEvent<String, Order>) {
        log.info("Order ${event.value.id} was Updated by member ${event.member.address} at ${time()}")
    }

    override fun mapCleared(event: MapEvent) {
        log.info("Map was Cleared by member ${event.member.address} at ${time()}")
    }

    override fun mapEvicted(event: MapEvent) {
        log.info("Map was Evicted by member ${event.member.address} at ${time()}")
    }

    override fun entryAdded(event: EntryEvent<String, Order>) {
        log.info("Order ${event.value.id} with size {${event.value.data.size}} was Added by member ${event.member.address} at ${time()}")
    }

    override fun entryRemoved(event: EntryEvent<String, Order>) {
        log.info("Order ${event.value.id} was Removed by member ${event.member.address} at ${time()}")
    }

    private fun time() = LocalDateTime.now()
}