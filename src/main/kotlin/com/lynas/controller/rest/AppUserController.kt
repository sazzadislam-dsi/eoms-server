package com.lynas.controller.rest

import com.lynas.model.AppUser
import com.lynas.model.Organization
import com.lynas.service.AppUserService
import com.lynas.util.AppConstant
import com.lynas.util.encodePassword
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

        return appUserService.save(appUser.apply {
            password = encodePassword(appUser.password)
            organization = request.session.getAttribute(AppConstant.organization) as Organization
        })
    }

    @GetMapping
    fun post(@PathVariable username: String): AppUser?
            = appUserService.findByUserName(username)

}
