package com.lynas.controller.rest

import com.lynas.exception.DuplicateCourseException
import com.lynas.model.Course
import com.lynas.service.ClassService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import com.lynas.util.responseConflict
import com.lynas.util.responseOK
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("classes")
class ClassRestController (val classService: ClassService) {

    private val logger = getLogger(this.javaClass)

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun post(@RequestBody cls: Course, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Received Class :: " + cls.toString())
        var createdClass = cls
        createdClass.organization = getOrganizationFromSession(request)

        try {
            createdClass = classService.create(createdClass)
        }
        catch (ex: DuplicateCourseException) {
            logger.warn("Duplicate class info found, class name [{}], shift [{}], section [{}]", cls.name, cls.shift, cls.section)
            return responseConflict(cls)
        }catch (ex: DuplicateKeyException){
            return responseConflict(cls)
        }
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
        return classService.findListByOrganizationId(organization.id!!)
    }

}