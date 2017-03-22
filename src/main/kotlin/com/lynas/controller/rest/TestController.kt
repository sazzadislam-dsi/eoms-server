package com.lynas.controller.rest

import com.lynas.service.OrganizationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/23/16
 */

@RestController
@RequestMapping("test")
class TestController(val organizationService: OrganizationService) {

    @GetMapping("/{id}")
    fun get(request: HttpServletRequest, @PathVariable id: Long): Test {
        request.session.setAttribute("organization", organizationService.readByID(id))

        return Test().apply { name = "get" }
    }

    class Test {
        var name = ""
    }

}

