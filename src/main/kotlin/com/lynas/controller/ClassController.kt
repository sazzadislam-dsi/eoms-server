package com.lynas.controller

import com.lynas.dto.CourseDTO
import com.lynas.model.Course
import com.lynas.service.ClassService
import com.lynas.util.AuthUtil
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("classes")
class ClassController(val classService: ClassService, val util: AuthUtil) {

    private val log = getLogger(this.javaClass)

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun post(@RequestBody cls: CourseDTO, request: HttpServletRequest): ResponseEntity<*> {
        log.info("Received Class :: " + cls.toString())
        var createdClass = Course(name = cls.name, shift = cls.shift, section = cls.section,
                organization = util.getOrganizationFromToken(request))
        createdClass = classService.create(createdClass)
        log.info("Saved Class :: " + createdClass.toString())
        return responseOK(createdClass)
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getAll(request: HttpServletRequest)
            = classService.findListByOrganizationId(util.getOrganizationIdFromToken(request))


    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun totalNumberOfClasses(request: HttpServletRequest)
            = classService.findListCountByOrganizationName(util.getOrganizationIdFromToken(request))

    @GetMapping("/class/{classId}/year/{year}/students")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getAllStudent(@PathVariable classId: Long, @PathVariable year: Int, request: HttpServletRequest) = classService.findStudentsByClassId(classId, util.getOrganizationIdFromToken(request), year)

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getCourseById(@PathVariable classId: Long, request: HttpServletRequest) = classService.findById(classId, util.getOrganizationIdFromToken(request))
}