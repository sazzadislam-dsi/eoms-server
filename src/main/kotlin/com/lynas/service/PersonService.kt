package com.lynas.service

import com.lynas.model.Person
import com.lynas.repo.PersonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 7/19/16
 */

@Service
class PersonService(val personRepository: PersonRepository) {

    @Transactional
    fun create(person: Person) {
        personRepository.save(person)
    }

    @Transactional
    fun findPersonById(id: Long): Person? = personRepository.findOne(id)
}