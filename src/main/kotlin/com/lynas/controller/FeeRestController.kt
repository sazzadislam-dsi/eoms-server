package com.lynas.controller

import com.lynas.exception.EntityNotFoundException
import com.lynas.model.FeeInfo
import com.lynas.model.StudentFee
import com.lynas.model.response.ErrorObject
import com.lynas.model.util.FeeInfoJson
import com.lynas.model.util.FeeStudentNew
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.service.StudentFeeService
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
import com.lynas.util.convertToDate
import com.lynas.util.responseError
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
                        val studentFeeService: StudentFeeService,
                        val authUtil: AuthUtil) {

    @PostMapping
    fun post(@RequestBody feeInfoJson: FeeInfoJson, request: HttpServletRequest): ResponseEntity<*> {
        val courseById = classService.findById(id = feeInfoJson.classId, orgId = authUtil.getOrganizationIdFromToken(request))
                ?: throw EntityNotFoundException("class Not Found With Given ID [${feeInfoJson.classId}]")

        // TODO feeInfoJson.lastDate?.convertToDate() error handling should be placed in controller advice
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
                ?: throw EntityNotFoundException("fee info not found for id [${feeStudentNew.feeInfoId}]")
        val studentOf = studentService.findById(feeStudentNew.studentId, authUtil.getOrganizationIdFromToken(request))
                ?: throw EntityNotFoundException("student not found for id [${feeStudentNew.studentId}]")
        val pDate = feeStudentNew.paymentDate.convertToDate()

        val studentFee = StudentFee(
                student = studentOf,
                feeInfo = fee,
                paymentDate = pDate)
        val savedObj = studentFeeService.create(studentFee)
        return responseOK(savedObj)
    }

    @GetMapping("/class/{classId}")
    fun getFeeInfoByClassId(@PathVariable classId: Long, request: HttpServletRequest) = feeInfoService.findFeeInfoByClass(classId)
            .filter { it.course.organization.id == authUtil.getOrganizationIdFromToken(request) }

    @GetMapping("/students/{id}")
    fun getFeesOfStudent(@PathVariable id: Long, request: HttpServletRequest) =
            feeInfoService.findStudentFeeInfoByStudent(id, authUtil.getOrganizationIdFromToken(request))

}











