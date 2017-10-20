package com.lynas.controller.rest

import com.lynas.model.FeeInfo
import com.lynas.model.StudentFee
import com.lynas.model.response.ErrorObject
import com.lynas.model.util.FeeInfoJson
import com.lynas.model.util.FeeStudentNew
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.service.StudentFeeService
import com.lynas.service.StudentService
import com.lynas.util.convertToDate
import com.lynas.util.getCurrentUserOrganizationId
import com.lynas.util.responseError
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
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
    fun post(@RequestBody feeInfoJson: FeeInfoJson, request: HttpServletRequest): ResponseEntity<*> {
        val courseById = classService.findById(id = feeInfoJson.classId, orgId = getCurrentUserOrganizationId(request))
                ?: return responseError(ErrorObject(feeInfoJson, "classId", "class Not Found With Given ID", ""))
        val feeInfo = FeeInfo(
                type = feeInfoJson.type,
                amount = feeInfoJson.amount,
                year = feeInfoJson.year,
                lastDate = feeInfoJson.lastDate?.convertToDate()
                        ?: return responseError(ErrorObject(feeInfoJson, "lastDate", "parseError", "dd-mm-yyyy")),
                course = courseById,
                dateCreated = Date(),
                dateModified = Date())
        return responseOK(feeInfoService.create(feeInfo))
    }

    @PostMapping("/student/new")
    fun studentPayment(request: HttpServletRequest, @RequestBody feeStudentNew: FeeStudentNew): ResponseEntity<*> {
        val fee = feeInfoService.find(feeStudentNew.feeInfoId)
                ?: return responseError(ErrorObject(feeStudentNew, "feeInfoId", "not found", ""))
        val studentOf = studentService.findById(feeStudentNew.studentId, getCurrentUserOrganizationId(request))
                ?: return responseError(ErrorObject(feeStudentNew, "studentId", "not found", ""))
        val pDate = feeStudentNew.paymentDate.convertToDate()

        val studentFee = StudentFee(
                student = studentOf,
                feeInfo = fee,
                paymentDate = pDate)
        val savedObj = studentFeeService.create(studentFee)
        return responseOK(savedObj)
    }
}











