package com.camiloagudelo.budgget.dao

import com.camiloagudelo.budgget.domain.Debt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DebtsDao: JpaRepository<Debt, Int>