package com.lynas.controller.view

import com.lynas.service.ClassService
import com.lynas.service.EnrolmentService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by muztaba.hasanat on 12/28/2016.
 */
@Controller
@RequestMapping("enrolment")
class EnrolmentController constructor(val enrolmentService: EnrolmentService,
                                      val classService: ClassService) {

    val logger = getLogger(EnrolmentController::class.java)

    @RequestMapping("/create/{classId}")
    fun create(@PathVariable classId: Long, model: Model, request: HttpServletRequest): String {
        logger.info("return enrolmentCreate page for classId [{}]", classId)
        model.addAttribute("classId", classId)
        val course = classService.findById(classId, getOrganizationFromSession(request).id!!)
        val courseName = course?.name + " " + course?.section + " " + course?.shift
        model.addAttribute("className", courseName)
        return "enrolmentCreate"
    }
}