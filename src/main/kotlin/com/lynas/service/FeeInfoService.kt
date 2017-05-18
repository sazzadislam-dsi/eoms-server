package com.lynas.service

import com.lynas.model.FeeInfo
import com.lynas.repo.FeeInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by sazzad on 5/18/17.
 */


@Service
class FeeInfoService(val feeInfoRepository: FeeInfoRepository) {

    @Transactional
    open fun save(feeInfo : FeeInfo) : FeeInfo {
        return feeInfoRepository.save(feeInfo)
    }

    @Transactional
    open fun findAll(): MutableIterable<FeeInfo>? {
        return feeInfoRepository.findAll()
    }

    @Transactional
    open fun find(id : Long): FeeInfo? {
        return feeInfoRepository.findOne(id)
    }

    @Transactional
    open fun delete(id:Long) : Boolean {
        try {
            feeInfoRepository.delete(id)
            return true
        }catch (e : Exception) {
            return false
        }
    }

}