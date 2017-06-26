package com.lynas.controller.view

import com.lynas.service.ClassService
import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

/**
 * Created by muztaba.hasanat on 2/15/2017.
 */

@Controller
@RequestMapping("exam")
class ExamController constructor(val classService: ClassService,
                                 val subjectService: SubjectService) {

    val logger = getLogger(ExamController::class.java)

    @RequestMapping("/class/select")
    fun attendanceBook(model: Model, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findClassListByOrganizationId(organization.id!!).sortedBy { it.name })
        logger.info("return examClassSelect page")
        return "exmClassSelect"
    }

    @RequestMapping("/studentList/year/{year}")
    fun studentListByClass(@PathVariable year: Int, @RequestParam classId: Long, model: Model, request: HttpServletRequest): String {
        logger.info("hit in studentListByClass with class id {}", classId)
        val organization = getOrganizationFromSession(request)
        val studentList = classService.findStudentsByClassId(classId, organization.id!!, year)
        val subjectList = subjectService.findAllByClassId(classId, organization.id!!)
        model.addAttribute("studentList", studentList)
        model.addAttribute("subjectList", subjectList)
        model.addAttribute("clsId", classId)
        return "exmStudentList"
    }

    @RequestMapping("/subject/result")
    fun resultOfSubject(request: HttpServletRequest, model: Model): String {
        logger.info("return subjectResult")
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findClassListByOrganizationId(organization.id!!).sortedBy { it.name })

        return "subjectResult"
    }

    @RequestMapping("/student/result")
    fun resultOfStudent(request: HttpServletRequest, model: Model): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findClassListByOrganizationId(organization.id!!).sortedBy { it.name })

        return "studentResult"
    }

    @RequestMapping("/class/result")
    fun resultOfClass(request: HttpServletRequest, model: Model): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findClassListByOrganizationId(organization.id!!).sortedBy { it.name })

        return "classResult"
    }
}