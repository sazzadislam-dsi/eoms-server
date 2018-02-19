package com.lynas.test.controller

import com.lynas.dto.OrganizationDTO
import com.lynas.dto.OrganizationInfoDTO
import com.lynas.util.getLogger
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:sample.properties")
class OrganizationControllerTest {

    private val log = getLogger(OrganizationControllerTest::class.java)
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate
    @Value("\${authToken}")
    private val authToken: String? = null

    @Before
    fun init() {
        testRestTemplate.restTemplate.interceptors = listOf(HttpHeaderInterceptor("Authorization", authToken))
    }

    @Test
    fun testCreateCourse() {
        val result = testRestTemplate.postForEntity("/organizations",
                OrganizationDTO(
                        name = "random org ${Math.random()}",
                        establishmentYear = 2001,
                        organizationInfo = OrganizationInfoDTO(
                                founderDescription = "random desc",
                                founderName = "random name"
                        )
                ),
                HashMap::class.java)
        assertEquals(result.statusCode, HttpStatus.CREATED)
        assertNotNull(result.statusCodeValue)
        assertNotNull(result.body["id"])
        log.info("$$$ ${result.body["id"]}")
    }

}