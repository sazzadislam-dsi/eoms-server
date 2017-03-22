package com.lynas.controller.rest

import com.lynas.model.Organization
import com.lynas.model.Stuff
import com.lynas.service.StuffService
import com.lynas.util.AppConstant
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("stuffs")
class StuffRestController(val stuffService: StuffService) {

    @PostMapping
    fun post(@RequestBody stuff: Stuff, request: HttpServletRequest): Stuff {
        stuff.person?.organization = request.session.getAttribute(AppConstant.organization) as Organization
        stuffService.save(stuff)
        return stuff
    }
}