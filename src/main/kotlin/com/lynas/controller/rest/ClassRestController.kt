package com.lynas.controller.rest

import com.lynas.model.Course
import com.lynas.model.util.CourseJson
import com.lynas.service.ClassService
import com.lynas.util.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("classes")
class ClassRestController(val classService: ClassService) {

    private val logger = getLogger(this.javaClass)

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun post(@RequestBody cls: CourseJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Received Class :: " + cls.toString())
        var createdClass = Course(
                name = cls.name,
                shift = cls.shift,
                section = cls.section,
                organization = getOrganizationFromSession(request))
        createdClass = classService.create(createdClass)
        logger.info("Saved Class :: " + createdClass.toString())
        return responseOK(createdClass)
    }

    @GetMapping("/getCollection/orgName/{name}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getCollection(@PathVariable name: String, request: HttpServletRequest): List<Course> {
        val organization = getOrganizationFromSession(request)
        if (organization.name != name) {
            logger.warn("Request orgName [{}] and session orgName [{}] does not match", name, organization.name)
            return ArrayList()
        }
        // TODO should not throw NLP for generic purpose
        // TODO this check should be in validation work
        return classService.findListByOrganizationId(organization.id ?: throw NullPointerException(Constants.ORG_ID_NULL))
    }

}