package com.lynas.controller.view

import com.lynas.model.Course
import com.lynas.model.Organization
import com.lynas.model.query.result.ClassDetailQueryResult
import com.lynas.service.ClassService
import com.lynas.util.AppConstant
import com.lynas.util.getLogger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@Controller
@RequestMapping("class")
class ClassController constructor(val classService: ClassService) {

    private val logger = getLogger(ClassController::class.java)


    @RequestMapping("/home")
    fun classHome(model: Model, request: HttpServletRequest): String {
        val organization = request.session.getAttribute(AppConstant.organization) as Organization
        model.addAttribute("classList", classService.findListByOrganizationName(organization.name).sortedBy { it.name })
        return "classHome"
    }


    @RequestMapping("/createClass")
    fun createClass(): String {
        println("test")
        return "createClass"
    }

    @RequestMapping("/updateClass/{classId}")
    fun updateClass(model: Model, @PathVariable classId: Long): String {
        val course = classService.findById(classId)
        logger.warn("Received Course : " + course.toString())
        model.addAttribute("course", course)
        return "updateClass"
    }


    @RequestMapping("/delete/{classId}")
    fun delete(@PathVariable classId: Long): String {
        classService.deleteById(classId)
        return "redirect:/class/home"
    }

    @RequestMapping("/detail/{classId}")
    fun detail(@PathVariable classId: Long, model: Model): String {
        logger.info("Hit in detail with class id {}", classId)
        val classDetails: Collection<ClassDetailQueryResult> = classService.findStudentsByClassId(classId)
        val cls: Course = classService.findById(classId)
        logger.info("class student number {}", classDetails.size)
        model.addAttribute("classDetails", classDetails)
        model.addAttribute("cls", cls)
        model.addAttribute("clsSize", classDetails.size)
        return "classDetail"
    }

}