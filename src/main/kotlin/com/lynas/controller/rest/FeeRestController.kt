package com.lynas.controller.rest

import com.lynas.model.FeeInfo
import com.lynas.model.Organization
import com.lynas.model.request.FeeInfoJson
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.util.convertToDate
import com.lynas.util.getOrganizationFromSession
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("fees")
class FeeRestController(val feeInfoService: FeeInfoService, val classService: ClassService) {

    @PostMapping
    fun post(@RequestBody feeInfoJson: FeeInfoJson, request: HttpServletRequest): FeeInfo {
        val organization = getOrganizationFromSession(request)
        val courseById = classService.findById(id = feeInfoJson.classId, organization = organization.name)
        val feeInfo = FeeInfo().apply {
            type = feeInfoJson.type
            amount = feeInfoJson.amount
            year = feeInfoJson.year
            lastDate = feeInfoJson.lastDate?.convertToDate()
            course = courseById
        }
        return feeInfoService.save(feeInfo)
    }

    @GetMapping
    fun getAll(request: HttpServletRequest): List<FeeInfo>? {
        val organization = getOrganizationFromSession(request)
        // TODO need to add org check
        return feeInfoService.findAll()?.filter { it.id != null }
    }


    @DeleteMapping("/{feeId}")
    fun updatePersonContactInfo(@PathVariable feeId: Long, request: HttpServletRequest) : Boolean {
        val organization = getOrganizationFromSession(request)
        val feeInfo = feeInfoService.find(feeId)


        return false
    }
}











