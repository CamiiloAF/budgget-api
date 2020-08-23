package com.camiloagudelo.budgget.domain

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "debts")
data class Debt(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int,
        @get:Size(min = 1, max = 50)
        val oweTo: String,
        val debtCreatedAt: Long,
        val debtTimeLimit: Long? = null,
        val totalDebt: Double,
        val debtPaid: Double
)