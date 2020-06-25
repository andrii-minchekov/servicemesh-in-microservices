package com.example.orderservice

import com.example.orderservice.rest.filter.CustomRequestLoggingFilter
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer
import org.hobsoft.spring.resttemplatelogger.LoggingCustomizer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}

@SpringBootApplication
class OrderServiceApplication {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .customizers(LoggingCustomizer())
            .build()
    }

    @Bean
    fun logFilter(): CustomRequestLoggingFilter {
        return CustomRequestLoggingFilter()
    }

    @Bean
    fun nodeConfigurer(): IgniteConfigurer {
        return IgniteConfigurer { cfg: IgniteConfiguration ->
            //Setting some property.
            //Other will come from `application.yml`
            cfg.igniteInstanceName = "my-ignite"
            val spi = TcpDiscoverySpi()
            val ipFinder = TcpDiscoveryMulticastIpFinder()
            spi.ipFinder = ipFinder
            cfg.discoverySpi = spi
            //-DIGNITE_JETTY_PORT=21666
        }
    }

//    @Bean
//    fun cacheManager(ignite:Ignite):CacheManager {
//        return Caching.getCachingProvider().cacheManager
//    }
}
