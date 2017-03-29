package com.lynas.controller.view

import com.lynas.service.ClassService
import com.lynas.service.OrganizationService
import com.lynas.service.StudentService
import com.lynas.util.AppConstant
import com.lynas.util.SpringUtil
import org.apache.log4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 7/15/16
 */

@Controller
class HomeController constructor(
        val orgService: OrganizationService,
        val classService: ClassService,
        val springUtil: SpringUtil,
        val studentService: StudentService,
        val log: Logger) {

    @RequestMapping(value = "/")
    fun home(request: HttpServletRequest, model: Model): String {
        log.warn("AT HOME")
        println("aaa" + springUtil.getAppOrganizationName())
        if (null == request.session.getAttribute(AppConstant.organization)) {
            val orgName = springUtil.getAppOrganizationName()
            request.session.setAttribute(AppConstant.organization, orgService.findByName(orgName))
            model.addAttribute("totalNumberOfClasses", classService.findListCountByOrganizationName(orgName))
            model.addAttribute("totalNumberOfStudents", studentService.getTotalNumberOfStudents())
        }
        return "home"
    }

    @RequestMapping(value = "/login")
    fun login(): String {
        return "login"
    }

    @RequestMapping(value = "/newHome")
    fun newHome(): String {
        return "newHome"
    }
}