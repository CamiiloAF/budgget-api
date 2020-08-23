package com.camiloagudelo.budgget

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class BudggetApplication

fun main(args: Array<String>) {
    runApplication<BudggetApplication>(*args)
}
