package com.lynas.test.controller

import com.lynas.test.authToken
import com.lynas.util.getLogger
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClassControllerTest {

    private val log = getLogger(ClassControllerTest::class.java)
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Before
    fun init() {
        testRestTemplate.restTemplate.interceptors = listOf(HttpHeaderInterceptor("Authorization", authToken))
    }


    @Test
    fun testGetCourseList() {
        val result = testRestTemplate.getForEntity("/classes", List::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.statusCodeValue)
        result.body.forEach {
            if (it is LinkedHashMap<*, *>) {
                log.info("$$$ ${it["id"]}")
            }
        }
    }

}