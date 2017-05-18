package com.lynas.controller.view

import com.lynas.model.Organization
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.util.AppConstant
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 5/18/17.
 */


@Controller
@RequestMapping("fee")
class FeeController(val feeInfoService: FeeInfoService, val classService: ClassService) {

    @RequestMapping("/new/class/{classId}")
    fun feeNew(@PathVariable classId:Long, request: HttpServletRequest, model: Model) : String {
        val organization = getOrganizationFromSession(request)
        val course = classService.findById(id = classId, organization = organization.name)
        model.addAttribute("courseId", course.id)
        return "feeNew"
    }


    @RequestMapping("/student/new")
    fun feeStudentNew() : String {

        return "feeStudentNew"
    }

}