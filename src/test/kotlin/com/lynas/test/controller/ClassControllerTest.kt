package com.lynas.test.controller

import com.lynas.dto.CourseDTO
import com.lynas.model.util.Section
import com.lynas.model.util.Shift
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
class ClassControllerTest {

    private val log = getLogger(ClassControllerTest::class.java)
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate
    @Value("\${authToken}")
    private val authToken: String? = null

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


    @Test
    fun testGetCourse() {
        val result = testRestTemplate.getForEntity("/classes/class/42", HashMap::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.statusCodeValue)
        assertEquals(result.body["id"], 42)

    }


    @Test
    fun testCreateCourse() {
        val result = testRestTemplate.postForEntity("/classes",
                CourseDTO(name = "random class ${Math.random()}",shift = Shift.MORNING,section = Section.SECTION_1),
                HashMap::class.java)
        assertEquals(result.statusCode, HttpStatus.CREATED)
        assertNotNull(result.statusCodeValue)
        assertNotNull(result.body["id"])
        log.info("$$$ ${result.body["id"]}")
    }


    @Test
    fun testGetCourseCount() {
        val result = testRestTemplate.getForEntity("/classes/count", Int::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals((result.body>0), true)
    }

}