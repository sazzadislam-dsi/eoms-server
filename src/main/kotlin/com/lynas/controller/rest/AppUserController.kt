package com.lynas.controller.rest

import com.lynas.model.AppUser
import com.lynas.service.AppUserService
import com.lynas.util.encodePassword
import com.lynas.util.getOrganizationFromSession
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/23/16
 */

@RestController
@RequestMapping("app_user")
class AppUserController(val appUserService: AppUserService) {


    @PostMapping
    fun post(@RequestBody appUser: AppUser, request: HttpServletRequest): AppUser? {

        return appUserService.create(appUser.apply {
            password = encodePassword(appUser.password)
            organization = getOrganizationFromSession(request)
        })
    }

    @GetMapping
    fun post(@PathVariable username: String): AppUser?
        = appUserService.findByUserName(username)

}

