package com.lynas.controller

import com.lynas.dto.AttendanceDTOofClass
import com.lynas.dto.ErrorObject
import com.lynas.service.AttendanceService
import com.lynas.util.*
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.text.ParseException
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 1/13/2017
 */

@Api("attendance")
@RestController
@RequestMapping("attendances")
class AttendanceController constructor(val attendanceService: AttendanceService, val util: AuthUtil) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ROLE_USER','ROLE_ADMIN','ADMIN')")
    fun createNewAttendance(@RequestBody attendanceJson: AttendanceDTOofClass, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Post of student attendance list {} for class id {}", attendanceJson, attendanceJson.classId)
        attendanceService.create(attendanceDto = attendanceJson,
                orgId = util.getOrganizationIdFromToken(request))
        logger.info("Post successfully attendance book")
        return responseOK(attendanceJson)
    }

    @GetMapping("/ofClass/{classId}/onDay/{day}")
    @PreAuthorize("hasAnyRole('USER','ROLE_USER','ROLE_ADMIN','ADMIN')")
    fun getAttendanceOfClassOnDate(request: HttpServletRequest, @PathVariable classId: Long, @PathVariable day: String)
            : ResponseEntity<*> {
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
        val result = attendanceService.getAttendanceOfAClassOnDate(date = dateOf.time, classId = classId,
                orgId = util.getOrganizationIdFromToken(request))
        return responseOK(result)
    }

}










