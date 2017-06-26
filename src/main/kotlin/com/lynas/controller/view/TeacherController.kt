package com.lynas.controller.view

import com.lynas.service.ClassService
import com.lynas.util.getOrganizationFromSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@Controller
@RequestMapping("teacher")
class TeacherController (val classService: ClassService) {

    @RequestMapping("/home")
    fun classHome(model: Model,request : HttpServletRequest): String {
        val organization = getOrganizationFromSession(request)
        model.addAttribute("classList", classService.findListByOrganizationId(organization.id!!))
        return "classHome"
    }

}