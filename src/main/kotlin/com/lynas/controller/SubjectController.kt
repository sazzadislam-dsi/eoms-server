package com.lynas.controller

import com.lynas.dto.SubjectDTO
import com.lynas.dto.SubjectResponseDTO
import com.lynas.exception.EntityNotFoundException
import com.lynas.model.Subject
import com.lynas.service.ClassService
import com.lynas.service.SubjectService
import com.lynas.util.AuthUtil
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 2/7/2017.
 */

@RestController
@RequestMapping("/subjects")
class SubjectController constructor(val subjectService: SubjectService,
                                    val classService: ClassService,
                                    val authUtil: AuthUtil) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody subjectJson: SubjectDTO, request: HttpServletRequest): ResponseEntity<*> {
        logger.info("Hit create method with {}", subjectJson)
        val course = classService.findById(subjectJson.classId, authUtil.getOrganizationIdFromToken(request))
                ?: throw EntityNotFoundException("Class can't find for id [${subjectJson.classId}")
        val subject = Subject(
                subjectName = subjectJson.subjectName,
                subjectDescription = subjectJson.subjectDescription,
                subjectBookAuthor = subjectJson.subjectBookAuthor,
                cls = course)
        subjectService.create(subject)
        return responseOK(subjectJson)
    }

    @GetMapping("/class/{classId}")
    fun getAllSubjectsOfClass(@PathVariable classId: Long, request: HttpServletRequest)
            : List<SubjectResponseDTO> {
        return subjectService.findAllByClassId(classId, authUtil.getOrganizationIdFromToken(request))
                .map {
                    SubjectResponseDTO(id = it.id, subjectName = it.subjectName,
                            subjectDescription = it.subjectDescription, subjectBookAuthor = it.subjectBookAuthor)
                }
                .toList()
    }

    @GetMapping("/student/{id}")
    fun getAllByStudentId(@PathVariable id: Long, request: HttpServletRequest) = subjectService.findAllByStudentId(id, authUtil.getOrganizationIdFromToken(request))

}