package com.lynas.controller

import com.lynas.dto.AttendanceOfClassDTO
import com.lynas.service.AttendanceService
import com.lynas.util.AuthUtil
import com.lynas.util.convertToDate
import com.lynas.util.responseCreated
import com.lynas.util.responseOK
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 1/13/2017
 */

@Api("attendance")
@RestController
@RequestMapping("attendances")
class AttendanceController constructor(val attendanceService: AttendanceService, val util: AuthUtil) {

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    fun createNewAttendance(@RequestBody attendanceOfClassDTO: AttendanceOfClassDTO, request: HttpServletRequest)
            : ResponseEntity<*> {
        attendanceService.create(attendanceOfClassDTO = attendanceOfClassDTO,
                orgId = util.getOrganizationIdFromToken(request))
        return responseCreated(attendanceOfClassDTO)
    }

    @GetMapping("/ofClass/{classId}/onDay/{day}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    fun getAttendanceOfClassOnDate(request: HttpServletRequest, @PathVariable classId: Long, @PathVariable day: String)
            : ResponseEntity<*> {
        val result = attendanceService.getAttendanceOfAClassOnDate(
                date = day.convertToDate().time,
                classId = classId,
                orgId = util.getOrganizationIdFromToken(request))
        return responseOK(result)
    }

}











