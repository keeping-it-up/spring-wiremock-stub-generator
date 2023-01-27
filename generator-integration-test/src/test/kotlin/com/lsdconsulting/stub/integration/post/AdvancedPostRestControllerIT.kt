package com.lsdconsulting.stub.integration.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.VerificationException
import com.lsdconsulting.stub.integration.BaseRestControllerIT
import com.lsdconsulting.stub.integration.POST_CONTROLLER_URL
import com.lsdconsulting.stub.integration.controller.post.AdvancedPostRestControllerStub
import com.lsdconsulting.stub.integration.model.GreetingResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity

class AdvancedPostRestControllerIT : BaseRestControllerIT() {
    private val underTest = AdvancedPostRestControllerStub(ObjectMapper())

    @Test
    fun `should handle post mapping with body`() {
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        underTest.postResourceWithBody(greetingResponse)
        val request = HttpEntity(greetingRequest)
        val response =
            restTemplate.postForEntity("$POST_CONTROLLER_URL/resourceWithBody", request, GreetingResponse::class.java)
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBody(1, greetingRequest)
        underTest.verifyPostResourceWithBody(greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        assertThrows<VerificationException> { underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest) }
    }

    @Test
    fun `should handle post mapping with body and path variable`() {
        underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(param, greetingRequest)
        underTest.postResourceWithBodyAndPathVariable(GreetingResponse(name = name), param)
        val request = HttpEntity(greetingRequest)
        val response = restTemplate.postForEntity(
            "$POST_CONTROLLER_URL/resourceWithBody/$param",
            request,
            GreetingResponse::class.java
        )
        assertThat(response.body, notNullValue())
        assertThat(response.body?.name, `is`(name))
        underTest.verifyPostResourceWithBodyAndPathVariable(1, param, greetingRequest)
        underTest.verifyPostResourceWithBodyAndPathVariable(param, greetingRequest)
        underTest.verifyPostResourceWithBodyNoInteraction(greetingRequest)
        assertThrows<VerificationException> {
            underTest.verifyPostResourceWithBodyAndPathVariableNoInteraction(
                param,
                greetingRequest
            )
        }
    }
}