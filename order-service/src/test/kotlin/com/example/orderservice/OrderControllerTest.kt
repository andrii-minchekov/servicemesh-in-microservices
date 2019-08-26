package com.example.orderservice

import com.example.orderservice.rest.OrderController
import com.example.orderservice.rest.OrderController.Companion.DEFAULT_ORDER_ID
import com.example.orderservice.rest.dto.Order
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(OrderController::class)
class OrderControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun shouldPerformGetOrders() {
        mvc.perform(
            get("/orders/$DEFAULT_ORDER_ID")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    @Ignore
    fun shouldCreateOrder() {
        val toCreate = Order(userId = "1")

        val orderId = mvc.perform(
            post("/orders")
                .content(toCreate.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated).andReturn().response.contentAsString

        val actualOrder = mvc.perform(
            get("/orders/$orderId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn()
            .response.contentAsString.deserializeFromJson(Order::class.java)

        assertThat(actualOrder).isEqualTo(toCreate.copy(id = orderId))
    }
}
