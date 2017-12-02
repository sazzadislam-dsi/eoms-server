package com.lynas.controller

import com.lynas.model.Course
import com.lynas.model.util.CourseJson
import com.lynas.service.ClassService
import com.lynas.util.AuthUtil
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("classes")
class ClassRestController(val classService: ClassService, val util: AuthUtil) {

    private val logger = getLogger(this.javaClass)

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun post(@RequestBody cls: CourseJson, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Received Class :: " + cls.toString())
        var createdClass = Course(
                name = cls.name,
                shift = cls.shift,
                section = cls.section,
                organization = util.getOrganizationFromToken(request))
        createdClass = classService.create(createdClass)
        logger.info("Saved Class :: " + createdClass.toString())
        return responseOK(createdClass)
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getAll(request: HttpServletRequest): List<Course> {
        val findListByOrganizationId: List<Course> = classService.findListByOrganizationId(util.getOrganizationIdFromToken(request))
        return findListByOrganizationId
    }

}