package com.camiloagudelo.budgget.controller

import com.camiloagudelo.budgget.domain.Debt
import com.camiloagudelo.budgget.service.DebtService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/debts")
class DebtController(debtService: DebtService): BasicController<Debt, Int>(debtService)