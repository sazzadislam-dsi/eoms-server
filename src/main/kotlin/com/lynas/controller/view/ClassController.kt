package com.lynas.controller.view

import com.lynas.model.Course
import com.lynas.model.query.result.ClassDetailQueryResult
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
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
class ClassController constructor(val classService: ClassService,
                                  val feeInfoService: FeeInfoService,
                                  val subjectService: SubjectService) {

    private val logger = getLogger(ClassController::class.java)


    @RequestMapping("/home")
    fun classHome(model: Model, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findListByOrganizationId(organization.id!!).sortedBy { it.name })
        return "classHome"
    }


    @RequestMapping("/createClass")
    fun createClass(): String {
        return "createClass"
    }

    @RequestMapping("/updateClass/{classId}")
    fun updateClass(model: Model, @PathVariable classId: Long, request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        val course = classService.findById(classId, organization.id!!)
        logger.warn("Received Course : " + course.toString())
        model.addAttribute("course", course)
        return "updateClass"
    }


    @RequestMapping("/delete/{classId}")
    fun delete(@PathVariable classId: Long): String {
        classService.deleteById(classId)
        return "redirect:/class/home"
    }

    @RequestMapping("/detail/{classId}/year/{year}")
    fun detail(@PathVariable classId: Long, @PathVariable year: Int, model: Model, request: HttpServletRequest): String {
        logger.info("Hit in detail with class id {}", classId)
        val organization = getOrganizationFromSession(request)
        val classDetails: Collection<ClassDetailQueryResult> = classService.findStudentsByClassId(classId, organization.id!!, year)
        val cls: Course = classService.findById(classId, organization.id!!)
        logger.info("class student number {}", classDetails.size)
        model.addAttribute("classDetails", classDetails)
        model.addAttribute("cls", cls)
        model.addAttribute("clsSize", classDetails.size)
        val feeInfoList = feeInfoService.findFeeInfoByClass(classId)
                ?.filter { it.course?.organization?.id == getOrganizationFromSession(request).id }
                ?.sortedBy { it.type }
        model.addAttribute("feeList", feeInfoList)
        model.addAttribute("list", subjectService.findAllByClassId(classId, organization.id!!))
        return "classDetail"
    }

}