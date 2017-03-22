package com.lynas.controller.view

import com.lynas.service.AppUserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by sazzad on 7/15/16
 */

@Controller
class HomeController(val appUserService: AppUserService) {

    @RequestMapping(value = "/")
    fun home(): String {
        //println(appUserService.loadUserByUsername("admin"))
        return "home"
    }


    @RequestMapping(value = "/user/{userName}")
    fun home(@PathVariable userName: String): String {
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