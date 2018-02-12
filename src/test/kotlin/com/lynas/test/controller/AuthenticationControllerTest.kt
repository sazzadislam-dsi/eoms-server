package com.lynas.test.controller

import com.lynas.dto.AuthenticationRequestDTO
import com.lynas.dto.AuthenticationResponseDTO
import com.lynas.util.getLogger
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    private val log = getLogger(AuthenticationControllerTest::class.java)
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate


    @Test
    fun testHelloController() {
        val authDto = AuthenticationRequestDTO("admin", "123456")
        val result = testRestTemplate.postForEntity("/auth/login", authDto, AuthenticationResponseDTO::class.java)
        log.info(result.toString())
        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.statusCodeValue)
    }

}