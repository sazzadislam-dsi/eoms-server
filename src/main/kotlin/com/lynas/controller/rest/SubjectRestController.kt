package com.lynas.controller.rest

import com.lynas.model.Course
import com.lynas.model.Subject
import com.lynas.model.request.SubjectPostJson
import com.lynas.service.ClassService
import com.lynas.service.SubjectService
import com.lynas.util.getLogger
import com.lynas.util.responseOK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by seal on 2/7/2017.
 */

@RestController
@RequestMapping("/subjects")
class SubjectRestController constructor(val subjectService: SubjectService,
                                        val classService: ClassService) {

    val logger = getLogger(SubjectRestController::class.java)

    @PostMapping
    fun post(@RequestBody subjectJson: SubjectPostJson): ResponseEntity<*> {
        logger.info("Hit post method with {}", subjectJson)
        val subject: Subject = Subject().apply {
            subjectName = subjectJson.subjectName
            subjectDescription = subjectJson.subjectDescription
            subjectBookAuthor = subjectJson.subjectBookAuthor

        }

        // Problem in save object
        subjectService.post(subject)
        val cls: Course = classService.findById(subjectJson.classId as Long)
        cls.subjects.add(subject)
        classService.save(cls)
        logger.info("Post successful")
        return responseOK(subjectJson)
    }

}