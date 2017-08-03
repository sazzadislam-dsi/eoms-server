package com.lynas.controller.view

import com.lynas.model.util.SpringSecurityUser
import com.lynas.service.AppUserService
import com.lynas.service.StudentService
import com.lynas.util.AppConstant
import com.lynas.util.SpringUtil
import com.lynas.util.getLogger
import org.slf4j.Logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 7/15/16
 */

@Controller
class HomeController(
        val appUserService: AppUserService,
        val springUtil: SpringUtil,
        val studentService: StudentService) {

    val logger: Logger = getLogger(HomeController::class.java)

    @RequestMapping(value = "/")
    fun home(model: Model, request: HttpServletRequest): String {
        logger.info("return home page {}", springUtil.getAppOrganizationName())
        val user = SecurityContextHolder.getContext().authentication.principal as SpringSecurityUser
        if (request.session.getAttribute(AppConstant.organization) == null) {
            val organization = appUserService.findByUserName(username = user.username)?.organization
            logger.info("Login with Username [{}], Organization name [{}], Organization id [{}]", user.username, organization?.name, organization?.id)
            request.session.setAttribute(AppConstant.organization, organization)
            model.addAttribute("studentCount", studentService.findStudentCountOfOrganization(organization!!.id!!))

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