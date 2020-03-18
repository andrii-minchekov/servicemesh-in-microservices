package com.example.orderservice

import com.example.orderservice.domain.order.Order
import com.example.orderservice.domain.order.OrderUseCasesApi
import com.example.orderservice.rest.OrderController
import com.example.orderservice.rest.dto.OrderDto
import com.example.orderservice.rest.dto.toDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@RunWith(SpringRunner::class)
@WebMvcTest(OrderController::class)
class OrderControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc
    @MockBean
    private lateinit var orderUseCases: OrderUseCasesApi
    private val api = Api()

    @Test
    fun shouldFindOneOrderSuccess() {
        val order = Order("1", listOf())
        val orderId = order.id
        `when`(orderUseCases.findOne(orderId)).thenReturn(order)

        val response = api.findOne(orderId)
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        assertThat(response.contentAsString.deserialize(OrderDto::class.java)).isEqualTo(order.toDto())
    }

    @Test
    fun shouldCreateOrderSuccess() {
        val userId = "1"
        val toCreate = OrderDto(userId = userId)
        val order = Order(userId, listOf())
        `when`(orderUseCases.create(any())).thenReturn(order)
        `when`(orderUseCases.findOne(orderId = order.id)).thenReturn(order)

        val createResponse = api.create(toCreate)

        assertThat(createResponse.status).isEqualTo(HttpStatus.CREATED.value())

        val orderId = createResponse.contentAsString
        assertThat(orderId).isNotBlank()

        val actualOrder = api.findOne(orderId).contentAsString.deserialize(OrderDto::class.java)

        assertThat(actualOrder).isEqualTo(order.toDto())
    }

    @Test
    fun shouldReturnApiErrorIfCreateOrderWithNoLineItems() {

    }

    private inner class Api {
        fun create(dto: OrderDto): MockHttpServletResponse = mvc.perform(
            post("/orders")
                .content(dto.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        fun findOne(orderId: String): MockHttpServletResponse = mvc.perform(
            get("/orders/$orderId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response
    }
}
