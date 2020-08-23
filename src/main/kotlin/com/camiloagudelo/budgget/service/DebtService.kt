package com.camiloagudelo.budgget.service

import com.camiloagudelo.budgget.dao.DebtsDao
import com.camiloagudelo.budgget.domain.Debt
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class DebtService(private val debtsDao: DebtsDao) : BasicCrud<Debt, Int> {

    override fun findAll(): List<Debt> = debtsDao.findAll()

    override fun findById(id: Int): Debt? = debtsDao.findByIdOrNull(id)

    override fun save(body: Debt): Debt =
            if (!debtsDao.existsById(body.id)) debtsDao.save(body)
            else throw DuplicateKeyException("${body.id} already exists")

    override fun update(body: Debt): Debt =
            if (debtsDao.existsById(body.id)) debtsDao.save(body)
            else throw EntityNotFoundException("${body.id} does not exists")

    override fun deleteById(id: Int): Debt = findById(id)?.apply {
        this@DebtService.debtsDao.deleteById(id)
    } ?: throw EntityNotFoundException("$id does not exists")

}