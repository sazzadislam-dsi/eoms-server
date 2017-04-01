package com.lynas.controller.view

import com.lynas.service.OrganizationService
import com.lynas.util.AppConstant
import com.lynas.util.SpringUtil
import com.lynas.util.getLogger
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 7/15/16
 */

@Controller
class HomeController constructor(val orgService: OrganizationService, val springUtil: SpringUtil) {

    val logger: Logger = getLogger(HomeController::class.java)

    @RequestMapping(value = "/")
    fun home(request: HttpServletRequest): String {
        logger.info("return home page {}", springUtil.getAppOrganizationName())
        if (null == request.session.getAttribute(AppConstant.organization)){
            request.session.setAttribute(AppConstant.organization, orgService.findByName(springUtil.getAppOrganizationName()))
        }
        return "home"
    }

    @RequestMapping(value = "/login")
    fun login():String{
        return "login"
    }

    @RequestMapping(value = "/newHome")
    fun newHome(): String {
        return "newHome"
    }
}