package com.lynas.controller.view

import com.lynas.model.query.result.ClassDetailQueryResult
import com.lynas.service.ClassService
import com.lynas.service.EnrolmentService
import com.lynas.util.getCurrentYear
import com.lynas.util.getLogger
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate
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
        val organization = getOrganizationFromSession(request)
        val course = classService.findById(classId, organization.id!!)
        val enrolStudentList: Collection<ClassDetailQueryResult> = classService.findStudentsByClassId(classId, organization.id!!, getCurrentYear())
        val courseName = course?.name + " " + course?.section + " " + course?.shift
        model.addAttribute("classId", classId)
        model.addAttribute("className", courseName)
        model.addAttribute("enrolStudentList", enrolStudentList)
        logger.info("return enrolmentCreate page for classId [{}]", classId)
        return "enrolmentCreate"
    }

    @RequestMapping("/delete/{enrolmentId}/class/{classId}/student/{studentId}")
    fun delete(@PathVariable enrolmentId: Long, @PathVariable classId: Long, @PathVariable studentId: Long,
               request: HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        return if(enrolmentService.delete(enrolmentId,studentId,LocalDate.now().year,organization.id!!)){
            "redirect:/enrolment/create/" + classId
        } else {
            "error"
        }
    }
}