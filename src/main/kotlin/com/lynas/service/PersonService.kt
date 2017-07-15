package com.lynas.service

import com.lynas.model.Person
import com.lynas.repo.PersonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
open class PersonService (val personRepository: PersonRepository) {

    @Transactional
    open fun save(person: Person) {
        personRepository.save(person)
    }

    @Transactional
    open fun findPersonById(id: Long): Person? = personRepository.findOne(id)
}