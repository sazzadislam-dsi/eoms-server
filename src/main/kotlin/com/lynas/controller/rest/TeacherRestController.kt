package com.lynas.controller.rest

import com.lynas.model.Organization
import com.lynas.model.Teacher
import com.lynas.service.TeacherService
import com.lynas.util.AppConstant
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("teachers")
class TeacherRestController(val teacherService: TeacherService) {

    @PostMapping
    fun post(@RequestBody teacher: Teacher, request: HttpServletRequest): Teacher {
        if (request.session.getAttribute(AppConstant.organization) == null) {

        }
        teacher.person?.organization = request.session.getAttribute(AppConstant.organization) as Organization
        teacherService.save(teacher)
        return teacher
    }
}