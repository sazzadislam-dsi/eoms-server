package com.lynas.controller.view

import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
/**
 * Created by seal on 2/6/2017.
 */
@Controller
@RequestMapping("/subject")
class SubjectController constructor(val subjectService: SubjectService)  {

    val logger = getLogger(SubjectController::class.java)

    @RequestMapping("/class/{classId}/list")
    fun subjectListOfClass(@PathVariable classId: Long, model: Model): String {
        logger.info("Return subject list for class id {}", classId)
        model.addAttribute("list", subjectService.findAllByClassId(classId))
        model.addAttribute("classId", classId)
        return "subjectOfClass"
    }

    @RequestMapping("/student/{stdId}/list")
    fun subjectListOfStudent(@PathVariable stdId: Long, model: Model): String {
        logger.info("Return subject list for student id {}", stdId)
        model.addAttribute("list", subjectService.findAllByStudentId(stdId))
        return "subjectOfClass"
    }


    @RequestMapping("/class/{classId}/create")
    fun create(@PathVariable classId: Long, model: Model): String {
        logger.info("Return subject create page for class Id {}", classId)
        model.addAttribute("classId", classId)
        return "subjectCreate"
    }
}