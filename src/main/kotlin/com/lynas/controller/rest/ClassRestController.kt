package com.lynas.controller.rest

import com.lynas.model.Course
import com.lynas.model.Organization
import com.lynas.service.ClassService
import com.lynas.util.AppConstant
import com.lynas.util.responseConflict
import com.lynas.util.responseOK
import com.lynas.util.verifyClassOrganization
import org.slf4j.LoggerFactory
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
open class ClassRestController(val classService: ClassService) {

    private val logger = LoggerFactory.getLogger(ClassRestController::class.java)

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    open fun post(@RequestBody cls: Course, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Received Class :: " + cls.toString())
        var createdClass = cls
        createdClass.organization = request.session.getAttribute(AppConstant.organization) as Organization?

        try {
            createdClass = classService.save(createdClass)
        } catch (ex: DuplicateKeyException) {
            return responseConflict(cls)
        }
        logger.info("Saved Class :: " + createdClass.toString())
        return responseOK(createdClass)
    }


    @PatchMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    open fun patch(@RequestBody cls: Course, request: HttpServletRequest): Course {
        val previousClass = classService.findById(cls.id!!)

        println(cls.toString())
        println(verifyClassOrganization(previousClass, request))

        /*logger.info("Received Class :: " + cls.toString())
        cls.organization = request.session.getAttribute(AppConstant.organization) as Organization?
        classService.save(cls)
        logger.info("Saved Class :: " + cls.toString())*/
        return cls
    }


    @GetMapping("/getCollection/orgName/{name}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getCollection(@PathVariable name: String): List<Course> {
        return classService.findListByOrganizationName(name)
    }

}