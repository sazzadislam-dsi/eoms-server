package com.lynas.controller.view

import com.lynas.service.EnrolmentService
import com.lynas.util.getLogger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by muztaba.hasanat on 12/28/2016.
 */
@Controller
@RequestMapping("enrolment")
class EnrolmentController constructor(enrolmentService: EnrolmentService) {

    val logger = getLogger(EnrolmentController::class.java)

    @RequestMapping("/create/{classId}")
    fun create(@PathVariable classId: Long, model: Model): String {
        logger.info("return enrolmentCreate page for classId [{}]", classId)
        model.addAttribute("classId", classId)
        return "enrolmentCreate"
    }
}