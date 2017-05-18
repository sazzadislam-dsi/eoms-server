package com.lynas.controller.rest

import com.lynas.model.FeeInfo
import com.lynas.model.Organization
import com.lynas.service.FeeInfoService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("fees")
class FeeRestController (val feeInfoService: FeeInfoService) {

    @PostMapping
    fun post(@RequestBody feeInfo: FeeInfo, request: HttpServletRequest): FeeInfo {
        return feeInfoService.save(feeInfo)
    }

    @GetMapping
    fun getAll(request: HttpServletRequest): List<FeeInfo>? {
        val organization = request.session.getAttribute("organization") as Organization?
        // TODO need to add org check
        return feeInfoService.findAll()?.filter { it.id != null }
    }


    @DeleteMapping("/{feeId}")
    fun updatePersonContactInfo(@PathVariable feeId: Long, request: HttpServletRequest) : Boolean {
        val organization = request.session.getAttribute("organization") as Organization?
        val feeInfo = feeInfoService.find(feeId)


        return false
    }
}











