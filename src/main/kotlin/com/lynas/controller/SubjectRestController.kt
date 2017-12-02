package com.lynas.controller

import com.lynas.exception.EntityNotFoundException
import com.lynas.model.Subject
import com.lynas.model.util.SubjectPostJson
import com.lynas.service.ClassService
import com.lynas.service.SubjectService
import com.lynas.util.AuthUtil
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * Created by seal on 2/7/2017.
 */

@RestController
@RequestMapping("/subjects")
class SubjectRestController constructor(val subjectService: SubjectService,
                                        val classService: ClassService,
                                        val authUtil: AuthUtil) {

    val logger = getLogger(this.javaClass)

    @PostMapping
    fun post(@RequestBody subjectJson: SubjectPostJson, request: HttpServletRequest): ResponseEntity<*> {
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

}