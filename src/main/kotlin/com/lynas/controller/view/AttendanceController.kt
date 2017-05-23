package com.lynas.controller.view

import com.lynas.model.Organization
import com.lynas.service.ClassService
import com.lynas.util.AppConstant
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 1/3/2017.
 */
@Controller()
@RequestMapping("attendance")
class AttendanceController constructor(val classService: ClassService) {

    val logger: Logger = getLogger(AttendanceController::class.java)

    @RequestMapping("/book")
    fun attendanceBook(model: Model, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findClassListByOrganizationName(organization.name).sortedBy { it.name })
        logger.info("return attenClassSelect page with classList")
        return "attenClassSelect"
    }

    @RequestMapping("/view")
    fun attendanceBookView(model: Model, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findClassListByOrganizationName(organization.name).sortedBy { it.name })
        logger.info("return attenClassSelect page with classList")
        return "attendanceView"
    }

    @RequestMapping("/studentList")
    fun studentListByClass(@RequestParam classId: Long,
                           @RequestParam year: Int,
                           model: Model,
                           request: HttpServletRequest): String {
        logger.info("hit in studentListByClass with class id {}", classId)
        val organization = getOrganizationFromSession(request)
        val studentList = classService.findStudentsByClassId(classId, year, organization.name)
        model.addAttribute("studentList", studentList)
        model.addAttribute("clsId", classId)
        return "attendanceStudent"
    }
}