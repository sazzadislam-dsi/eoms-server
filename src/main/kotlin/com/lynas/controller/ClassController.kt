package com.lynas.controller

import com.lynas.dto.CourseDTO
import com.lynas.service.ClassService
import com.lynas.util.AuthUtil
import com.lynas.util.responseCreated
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("classes")
class ClassController(val classService: ClassService, val util: AuthUtil) {

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getClassById(@PathVariable classId: Long, request: HttpServletRequest)
            = classService.findById(classId, util.getOrganizationIdFromToken(request))

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun createNewClass(@RequestBody courseDTO: CourseDTO, request: HttpServletRequest)
        = responseCreated(classService.create(courseDTO.getCourse(util.getOrganizationFromToken(request))))

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getAllClasses(request: HttpServletRequest)
            = classService.findListByOrganizationId(util.getOrganizationIdFromToken(request))

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getTotalNumberClasses(request: HttpServletRequest)
            = classService.findListCountByOrganizationName(util.getOrganizationIdFromToken(request))

    @GetMapping("/class/{classId}/year/{year}/students")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    fun getAllStudentByClassAndYear(@PathVariable classId: Long, @PathVariable year: Int, request: HttpServletRequest)
            = classService.findStudentsByClassId(classId, util.getOrganizationIdFromToken(request), year)
}