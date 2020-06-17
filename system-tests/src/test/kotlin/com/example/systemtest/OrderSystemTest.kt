package com.example.systemtest

import TimingExtension
import com.example.systemtest.dto.OrderDto
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_OK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TimingExtension::class)
class OrderSystemTest {

    private val isServerSideBalanced = false
    private val servers = arrayOf("http://192.168.1.118:8072", "http://192.168.1.235:8072")
    private val roundRobinUri = RoundRobin(servers)
    private val staticAliasUri = { "http://order-service-8072" }
    private lateinit var baseUri: () -> String

    companion object {
        const val BASE_PATH = "orders"
        val USER_ID = UUID.randomUUID().toString()
    }

    @BeforeAll
    fun setUp() {
        RestAssured.basePath = BASE_PATH
        baseUri = if (isServerSideBalanced) {
            RestAssured.proxy("localhost", 4141)
            staticAliasUri
        } else {
            roundRobinUri
        }
    }

    @RepeatedTest(2)
    fun shouldReturnNewlyCreatedOrder() {
        val orderDto = OrderDto(USER_ID)
        val createResponse = given()
            .baseUri(baseUri())
            .body(orderDto)
            .contentType(ContentType.JSON)
            .post();
        assertThat(createResponse.statusCode).isEqualTo(SC_CREATED)

        val getResponse = given()
            .baseUri(baseUri())
            .get("/{orderId}", createResponse.body.asString());
        assertThat(getResponse.statusCode).isEqualTo(SC_OK)
    }
}