package com.lynas.service

import com.lynas.dto.AttendanceDTOofClass
import com.lynas.dto.AttendanceViewQueryResult
import com.lynas.exception.DuplicateEntryException
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.AttendanceBook
import com.lynas.model.StudentAttendance
import com.lynas.repo.AttendanceRepository
import com.lynas.util.convertToDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by seal on 2/8/2017
 */
@Service
class AttendanceService constructor(val studentService: StudentService,
                                    val classService: ClassService,
                                    val attendanceRepository: AttendanceRepository) {

    @Transactional
    fun create(attendanceDto: AttendanceDTOofClass, orgId: Long): AttendanceBook {
        val attendanceDate = attendanceDto.date.convertToDate()
        val course = classService.findById(attendanceDto.classId, orgId)
                ?: throw EntityNotFoundException(
                        "Course or class not found with given classID : ${attendanceDto.classId}")
        val foundDuplicate = checkExistingAttendanceOnGivenDate(attendanceDate, attendanceDto.classId, orgId)
        if (foundDuplicate) {
            throw DuplicateEntryException("Attendance duplicate entry found at date $attendanceDate")
        }

        //TODO catch this exception properly
        val set = attendanceDto.attendanceDTO.map {
            StudentAttendance(
                    student = studentService.findById(it.t, orgId)
                            ?: throw EntityNotFoundException("student Not found with given studentId: ${it.t}"),
                    attendanceStatus = it.i)
        }.toMutableSet()

        val attendanceBook = AttendanceBook(studentAttendances = set, attendanceDate = attendanceDate, course = course)
        return attendanceRepository.save(attendanceBook)
    }

    private fun checkExistingAttendanceOnGivenDate(_attendanceDate: Date, classId: Long, orgId: Long): Boolean {
        return attendanceRepository.findAttendanceBookOfClass(_attendanceDate.time, classId, orgId)
                .isEmpty()
                .not()
    }


    @Transactional
    fun getAttendanceOfAClassOnDate(date: Long, classId: Long, orgId: Long): List<AttendanceViewQueryResult> {
        return attendanceRepository.findAttendanceBookOfClass(date, classId, orgId)
    }


}