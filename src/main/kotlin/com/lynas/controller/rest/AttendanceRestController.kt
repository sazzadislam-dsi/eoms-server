package com.lynas.controller.rest

import com.lynas.exception.SameDateAttendanceException
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

    val logger = getLogger(this.javaClass)

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ROLE_USER','ROLE_ADMIN','ADMIN')")
    fun post(@RequestBody attendanceJson: AttendanceJsonWrapper,
                       request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Post of student attendance list {} for class id {}", attendanceJson, attendanceJson.classId)
        try {
            attendanceService.create(
                    attendanceJsonWrapper = attendanceJson,
                    orgId = getOrganizationFromSession(request).id!!)
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
                    receivedObject = attendanceJson,
                    errorField = "date",
                    errorMessage = Constants.INVALID_DATE_FORMAT,
                    errorSuggestion = Constants.EXPECTED_DATE_FORMAT))
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
                    receivedObject = day,
                    errorField = "day",
                    errorMessage = Constants.INVALID_DATE_FORMAT,
                    errorSuggestion = Constants.EXPECTED_DATE_FORMAT))
        }
        val result = attendanceService.getAttendanceOfAClassOnDate(
                date = dateOf.time,
                classId = classId,
                orgId = getOrganizationFromSession(request).id!!)

        return responseOK(result)
    }

}











