package com.lynas.controller

import com.lynas.dto.FeeInfoDTO
import com.lynas.dto.PayFeeDTO
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.FeeInfo
import com.lynas.model.StudentFee
import com.lynas.service.ClassService
import com.lynas.service.FeeInfoService
import com.lynas.service.StudentFeeService
import com.lynas.service.StudentService
import com.lynas.util.AuthUtil
import com.lynas.util.convertToDate
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
class FeeController(val feeInfoService: FeeInfoService,
                    val classService: ClassService,
                    val studentService: StudentService,
                    val studentFeeService: StudentFeeService,
                    val authUtil: AuthUtil) {

    @PostMapping
    fun post(@RequestBody feeInfoDTO: FeeInfoDTO, request: HttpServletRequest): ResponseEntity<*> {
        val courseById = classService.findById(id = feeInfoDTO.classId,
                orgId = authUtil.getOrganizationIdFromToken(request))
                ?: throw EntityNotFoundException("class Not Found With Given ID [${feeInfoDTO.classId}]")

        val feeInfo = FeeInfo(
                type = feeInfoDTO.type,
                amount = feeInfoDTO.amount,
                year = feeInfoDTO.year,
                lastDate = feeInfoDTO.lastDate?.convertToDate()!!,
                course = courseById,
                dateCreated = Date(),
                dateModified = Date())
        return responseOK(feeInfoService.create(feeInfo))
    }

    @PostMapping("/student/new")
    fun studentPayment(request: HttpServletRequest, @RequestBody payFeeDTO: PayFeeDTO): ResponseEntity<*> {
        val fee = feeInfoService.find(payFeeDTO.feeInfoId)
                ?: throw EntityNotFoundException("fee info not found for id [${payFeeDTO.feeInfoId}]")
        val studentOf = studentService.findById(payFeeDTO.studentId, authUtil.getOrganizationIdFromToken(request))
                ?: throw EntityNotFoundException("student not found for id [${payFeeDTO.studentId}]")
        val pDate = payFeeDTO.paymentDate.convertToDate()

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











