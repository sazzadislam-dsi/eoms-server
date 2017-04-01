package com.lynas.controller.view

import com.lynas.service.StuffService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by sazzad on 8/15/16
 */

@Controller
@RequestMapping("stuff")
class StuffController (val stuffService: StuffService) {

    @RequestMapping("/home")
    fun classHome(): String {

        return "stuffHome"
    }

}