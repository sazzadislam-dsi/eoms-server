package com.lynas.controller.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by sazzad on 5/18/17.
 */


@Controller
@RequestMapping("fee")
class FeeController {

    @RequestMapping("/new")
    fun feeNew() : String {

        return "feeNew"
    }


    @RequestMapping("/student/new")
    fun feeStudentNew() : String {

        return "feeStudentNew"
    }

}