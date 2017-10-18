package com.lynas.controller.rest

import com.lynas.model.FeeInfo
import com.lynas.model.StudentFee
import com.lynas.model.util.FeeInfoJson
import com.lynas.model.util.FeeStudentNew
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.service.StudentFeeService
import com.lynas.service.StudentService
import com.lynas.util.convertToDate
import com.lynas.util.getCurrentUserOrganizationId
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by sazzad on 8/15/16
 */

@RestController
@RequestMapping("fees")
class FeeRestController(val feeInfoService: FeeInfoService,
                        val classService: ClassService,
                        val studentService: StudentService,
                        val studentFeeService: StudentFeeService) {

    @PostMapping
    fun post(@RequestBody feeInfoJson: FeeInfoJson, request: HttpServletRequest): FeeInfo {
        val courseById = classService.findById(id = feeInfoJson.classId, orgId = getCurrentUserOrganizationId(request))
        val feeInfo = FeeInfo().apply {
            type = feeInfoJson.type
            amount = feeInfoJson.amount
            year = feeInfoJson.year
            lastDate = if (feeInfoJson.lastDate?.trim() == "") null else feeInfoJson.lastDate?.convertToDate()
            course = courseById
        }
        return feeInfoService.create(feeInfo)
    }

    @PostMapping("/student/new")
    fun studentPayment(request: HttpServletRequest, @RequestBody feeStudentNew: FeeStudentNew): ResponseEntity<*> {
        val fee = feeInfoService.find(feeStudentNew.feeInfoId)
        val studentOf = studentService.findById(feeStudentNew.studentId, getCurrentUserOrganizationId(request))
        val pDate = feeStudentNew.paymentDate.convertToDate()

        val studentFee = StudentFee().apply {
            student = studentOf
            feeInfo = fee
            paymentDate = pDate

        }
        val savedObj = studentFeeService.create(studentFee)
        return responseOK(savedObj)
    }
}











