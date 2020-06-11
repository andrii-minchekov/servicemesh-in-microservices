package com.example.systemtest

import com.example.systemtest.dto.LineItemDto
import com.example.systemtest.dto.OrderDto
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_OK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import java.lang.System.lineSeparator
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderUseCasesTest {

    companion object {
        const val PRODUCT_ID = "1"
        const val QUANTITY = 1
        const val BASE_PATH = "orders"
        val USER_ID = UUID.randomUUID().toString()
    }

    @BeforeAll
    fun setUp() {
        RestAssured.baseURI = "http://order-service"
        RestAssured.basePath = BASE_PATH
        RestAssured.proxy("localhost", 4141)
    }

    @RepeatedTest(1)
    fun shouldReturnNewlyCreatedOrder() {
        val createResponse = given().log().all()
            .basePath(BASE_PATH)
            .body(OrderDto(USER_ID, arrayOf(LineItemDto(PRODUCT_ID, QUANTITY))))
            .contentType(ContentType.JSON)
            .post();
        logResponse(createResponse)
        assertThat(createResponse.statusCode).isEqualTo(SC_CREATED)

        val getResponse = given().log().all()
            .get("/{orderId}", createResponse.body.asString());
        logResponse(getResponse)
        assertThat(getResponse.statusCode).isEqualTo(SC_OK)
    }

    private fun logResponse(response: Response) {
        println()
        val builder = StringBuilder("RESPONSE:${lineSeparator()}")
        builder.append("BODY:", lineSeparator(), response.body.asString(), lineSeparator())
        builder.append("STATUS:", java.lang.String.valueOf(response.statusCode), lineSeparator())
        builder.append("HEADERS:", lineSeparator(), response.headers.toString(), lineSeparator())
        println(builder.toString())
    }
}