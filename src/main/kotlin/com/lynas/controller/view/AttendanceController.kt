package com.lynas.controller.view

import com.lynas.service.ClassService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 1/3/2017.
 */
@Controller()
@RequestMapping("attendance")
class AttendanceController constructor(val classService: ClassService) {

    val logger: Logger = getLogger(this.javaClass)

    @RequestMapping("/book")
    fun attendanceBook(model: Model, request: HttpServletRequest): String {
        model.addAttribute("classList", classService.findClassListByOrganizationId(
                getOrganizationFromSession(request).id!!).sortedBy { it.name })
        logger.info("return attenClassSelect page with classList")
        return "attenClassSelect"
    }

    @RequestMapping("/view/classId/{classId}")
    fun attendanceBookView(@PathVariable classId: Long, model: Model, request: HttpServletRequest): String {
        model.addAttribute("classId", classId)
        logger.info("return attenClassSelect page for classId [{}]", classId)
        return "attendanceView"
    }

    @RequestMapping("/studentList/classId/{classId}/year/{year}")
    fun studentListByClass(@PathVariable classId: Long,
                           @PathVariable year: Int,
                           model: Model,
                           request: HttpServletRequest): String {
        logger.info("hit in studentListByClass with class id {}", classId)
        val studentList = classService.findStudentsByClassId(classId, year, getOrganizationFromSession(request).id!!)
        model.addAttribute("studentList", studentList)
        model.addAttribute("clsId", classId)
        return "attendanceStudent"
    }
}