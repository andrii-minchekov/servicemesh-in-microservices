package com.example.orderservice

import com.example.orderservice.domain.order.Order.Companion.DEFAULT_ORDER_ID
import com.example.orderservice.domain.order.OrderUseCasesApi
import com.example.orderservice.rest.OrderController
import com.example.orderservice.rest.dto.OrderView
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
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

    @MockBean
    private lateinit var useCases: OrderUseCasesApi

    @Test
    fun `should not find order if it wasnt created`() {
        mvc.perform(
                get("/orders/$DEFAULT_ORDER_ID")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Ignore //TODO fix deserialization
    @Test
    fun shouldReturnJustCreatedOrder() {

        val toCreate = OrderView(userId = "1")

        val orderId = mvc.perform(
                post("/orders")
                        .content(toCreate.toJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated).andReturn().response.contentAsString

        given(useCases.findOrderBy(orderId)).willReturn(toCreate.toDomain());
        val actualOrder = mvc.perform(
                get("/orders/$orderId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andReturn()
                .response.contentAsString.deserializeFromJson(OrderView::class.java)

        assertThat(actualOrder).isEqualTo(toCreate.copy(id = orderId))
    }
}
