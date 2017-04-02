package com.lynas.controller.rest

import com.lynas.model.AttendanceBook
import com.lynas.model.Organization
import com.lynas.model.request.AttendanceJsonWrapper
import com.lynas.model.response.ErrorObject
import com.lynas.service.AttendanceService
import com.lynas.util.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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
    fun postAttendance(@RequestBody attendanceJson: AttendanceJsonWrapper): ResponseEntity<*> {
        logger.info("Post of student attendance list {} for class id {}", attendanceJson, attendanceJson.classId)
        try {
            attendanceJson.date.convertToDate()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return responseError(ErrorObject(
                    attendanceJson,
                    "date",
                    Constants.INVALID_DATE_FORMAT,
                    Constants.EXPECTED_DATE_FORMAT))
        }
        val attendanceBook: AttendanceBook = attendanceService.post(attendanceJson)
        logger.info("Post successfully attendance book {}", attendanceBook)
        return responseOK(attendanceJson)
    }

    @GetMapping("/ofClass/{className}/onDay/{day}")
    @PreAuthorize("hasAnyRole('USER','ROLE_USER','ROLE_ADMIN','ADMIN')")
    fun getAttendanceOfAClassOnDate(
            request: HttpServletRequest,
            @PathVariable className: String,
            @PathVariable day: String): ResponseEntity<*> {
        val dateOf: Date
        try {
            dateOf = day.convertToDate()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return responseError(ErrorObject(
                    day,
                    "day",
                    Constants.INVALID_DATE_FORMAT,
                    Constants.EXPECTED_DATE_FORMAT))
        }
        val organization = request.session.getAttribute(AppConstant.organization) as Organization?
        val result = attendanceService.getAttendanceOfAClassOnDate(
                dateOf.time,
                className,
                organization?.name)
        print("")
        return responseOK(result)
    }

}











