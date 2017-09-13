package com.lynas.controller.view

import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 2/6/2017.
 */
@Controller
@RequestMapping("/subject")
class SubjectController constructor(val subjectService: SubjectService)  {

    val logger = getLogger(this.javaClass)

    @RequestMapping("/class/{classId}/list")
    fun subjectListOfClass(@PathVariable classId: Long, model: Model, request: HttpServletRequest): String {
        logger.info("Return subject list for class id {}", classId)
        model.addAttribute("subjectList", subjectService.findAllByClassId(
                classId,
                getOrganizationFromSession(request).id!!))
        model.addAttribute("classId", classId)
        return "subjectOfClass"
    }

    @RequestMapping("/student/{stdId}/list")
    fun subjectListOfStudent(@PathVariable stdId: Long, model: Model, request: HttpServletRequest): String {
        logger.info("Return subject list for student id {}", stdId)
        model.addAttribute("subjectList", subjectService.findAllByStudentId(
                stdId,
                getOrganizationFromSession(request).id!!))
        return "subjectOfClass"
    }


    @RequestMapping("/class/{classId}/create")
    fun create(@PathVariable classId: Long, model: Model, request: HttpServletRequest): String {
        logger.info("Return subject create page for class Id {}", classId)
        model.addAttribute("classId", classId)
        model.addAttribute("subjectList",
                subjectService.findAllByClassId(classId, getOrganizationFromSession(request).id!!))
        return "subjectCreate"
    }
}