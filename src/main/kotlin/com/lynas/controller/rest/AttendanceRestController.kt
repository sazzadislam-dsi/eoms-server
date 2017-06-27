package com.lynas.controller.rest

import com.lynas.exception.SameDateAttendanceException
import com.lynas.model.AttendanceBook
import com.lynas.model.Organization
import com.lynas.model.request.AttendanceJsonWrapper
import com.lynas.model.response.ErrorObject
import com.lynas.service.AttendanceService
import com.lynas.util.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.text.ParseException
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 1/13/2017
 */

@RestController
@RequestMapping("attendances")
class AttendanceRestController constructor(val attendanceService: AttendanceService) {

    val logger = getLogger(AttendanceRestController::class.java)

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ROLE_USER','ROLE_ADMIN','ADMIN')")
    fun postAttendance(@RequestBody attendanceJson: AttendanceJsonWrapper,
                       request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Post of student attendance list {} for class id {}", attendanceJson, attendanceJson.classId)
        val organization = getOrganizationFromSession(request)
        try {
            val attendanceBook: AttendanceBook = attendanceService.post(attendanceJson, organization.id!!)
            logger.info("Post successfully attendance book")
        } catch (ex: SameDateAttendanceException) {
            logger.error("Duplicate attendance entry found on date [{}], class ID [{}]", attendanceJson.date, attendanceJson.classId)
            return responseConflict(attendanceJson)
        } catch (ex: Exception) {
            val cause = ex.cause
            if (cause is ParseException) {
                logger.error(cause.message)
            }
            return responseError(ErrorObject(
                    attendanceJson,
                    "date",
                    Constants.INVALID_DATE_FORMAT,
                    Constants.EXPECTED_DATE_FORMAT))
        }
        return responseOK(attendanceJson)
    }

    @GetMapping("/ofClass/{classId}/onDay/{day}")
    @PreAuthorize("hasAnyRole('USER','ROLE_USER','ROLE_ADMIN','ADMIN')")
    fun getAttendanceOfAClassOnDate(
            request: HttpServletRequest,
            @PathVariable classId: Long,
            @PathVariable day: String): ResponseEntity<*> {
        val dateOf: Date
        try {
            dateOf = day.convertToDate()
        } catch (ex: ParseException) {
            ex.printStackTrace()
            return responseError(ErrorObject(
                    day,
                    "day",
                    Constants.INVALID_DATE_FORMAT,
                    Constants.EXPECTED_DATE_FORMAT))
        }
        val organization = getOrganizationFromSession(request)
        val result = attendanceService.getAttendanceOfAClassOnDate(
                dateOf.time,
                classId,
                organization.id!!)

        return responseOK(result)
    }

}











