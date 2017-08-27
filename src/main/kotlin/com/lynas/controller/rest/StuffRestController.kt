package com.lynas.controller.rest

import com.lynas.model.Stuff
import com.lynas.service.StuffService
import com.lynas.util.getOrganizationFromSession
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
class StuffRestController (val stuffService: StuffService) {

    @PostMapping
    fun post(@RequestBody stuff: Stuff,request : HttpServletRequest): Stuff {
        stuff.person?.organization = getOrganizationFromSession(request)
        stuffService.save(stuff)
        return stuff
    }
}