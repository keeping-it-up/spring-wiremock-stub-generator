package com.lsdconsulting.stub.integration.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.GET_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.get.GetRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.format.datetime.standard.Jsr310DateTimeFormatAnnotationFormatterFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import java.net.URLEncoder.encode
import java.nio.charset.StandardCharsets.UTF_8
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.format.DateTimeFormatter.ofPattern


class GetRestControllerAnnotationFormatterFactoryIT : BaseRestControllerIT() {
    private val underTest = GetRestControllerStub(ObjectMapper(), Jsr310DateTimeFormatAnnotationFormatterFactory())

    @Test
    fun `should handle get mapping with ZonedDateTime request param`() {
        val param = ZonedDateTime.now()
        underTest.verifyGetResourceWithZonedDatetimeNoInteraction(param)
        underTest.verifyGetResourceWithZonedDatetimeNoInteractionWithUrl()
        underTest.getResourceWithZonedDatetime(greetingResponse, param)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithZonedDatetime?param=${encode(param.format(ISO_DATE_TIME), UTF_8)}",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithZonedDatetime(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithZonedDatetimeNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithZonedDatetimeNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with ZonedDateTime request param and all DateTimeFormat arguments`() {
        val param = ZonedDateTime.now()
        underTest.verifyGetResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteraction(param)
        underTest.verifyGetResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteractionWithUrl()
        underTest.getResourceWithZonedDatetimeAndAllDateTimeFormatArguments(greetingResponse, param)

        val response = restTemplate.exchange(
            "$GET_CONTROLLER_URL/resourceWithZonedDatetimeAndAllDateTimeFormatArguments?param=${encode(param.format(
                ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")), UTF_8)}",
            GET, HttpEntity(mapOf<String, String>()), GreetingResponse::class.java
        )

        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyGetResourceWithZonedDatetimeAndAllDateTimeFormatArguments(param)
        assertThrows<VerificationException> { underTest.verifyGetResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteraction(param) }
        assertThrows<VerificationException> { underTest.verifyGetResourceWithZonedDatetimeAndAllDateTimeFormatArgumentsNoInteractionWithUrl() }
    }

    @Test
    fun `should handle get mapping with ZonedDateTime and multi-value request parameters`() {
        val param = ZonedDateTime.now()
        underTest.verifyGetResourceWithZonedDatetimeAndMultiValueNoInteraction(param, setOf(33, 44))
        underTest.getResourceWithZonedDatetimeAndMultiValue(greetingResponse, param, setOf(33, 44))

        val request = HttpGet(
            "$GET_CONTROLLER_URL/resourceWithZonedDatetimeAndMultiValue?param=${
                encode(param.format(ISO_DATE_TIME), UTF_8)}&multiValue=33&multiValue=44"
        )
        HttpClientBuilder.create().build().use { client -> client.execute(request) as CloseableHttpResponse }

        underTest.verifyGetResourceWithZonedDatetimeAndMultiValue(param, setOf(33, 44))
        assertThrows<VerificationException> { underTest.verifyGetResourceWithZonedDatetimeAndMultiValueNoInteraction(param, setOf(33, 44)) }
    }
}
