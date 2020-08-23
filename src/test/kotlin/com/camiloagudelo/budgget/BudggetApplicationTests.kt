package com.camiloagudelo.budgget

import com.camiloagudelo.budgget.domain.Debt
import com.camiloagudelo.budgget.service.DebtService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@SpringBootTest
class BudggetApplicationTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
    }

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var debtService: DebtService

    private val debtsEndPoint = "/api/v1/debts/"

    @Test
    fun findAll() {
        val debtsFromService = debtService.findAll()

        val debts: List<Debt> = mockMvc.perform(MockMvcRequestBuilders.get(debtsEndPoint))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assertThat(debtsFromService, Matchers.`is`(Matchers.equalTo(debts)))
    }

    @Test
    fun findById() {
        val debt = getOneDebtFromService()

        mockMvc.perform(MockMvcRequestBuilders.get("$debtsEndPoint${debt.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.oweTo", Matchers.`is`(debt.oweTo)))
    }

    @Test
    fun findByIdEmpty() {
        mockMvc.perform(MockMvcRequestBuilders.get("$debtsEndPoint/-1"))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun saveSuccessfully() {
        val debt = Debt(
                id = -90000,
                oweTo = "Owe to test",
                debtCreatedAt = 100000,
                totalDebt = 88888.8,
                debtPaid = 99999.9
        )

        val debtFromApi: Debt = mockMvc.perform(MockMvcRequestBuilders.post(debtsEndPoint)
                .body(data = debt, mapper = mapper))
                .andExpect(status().isCreated)
                .bodyTo(mapper)

        assert(debtService.findAll().contains(debtFromApi))
    }

    @Test
    fun saveFail() {
        val debt = getOneDebtFromService()

        mockMvc.perform(MockMvcRequestBuilders.post(debtsEndPoint)
                .body(data = debt, mapper = mapper))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))
    }

    @Test
    fun updateSuccessFully() {
        val debt = getOneDebtFromService().copy(oweTo = "Other oweTo name")

        val debtFromApi: Debt = mockMvc.perform(MockMvcRequestBuilders.put(debtsEndPoint)
                .body(data = debt, mapper = mapper))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assertThat(debtService.findById(debt.id), Matchers.`is`(debtFromApi))
    }


    @Test
    fun updateFailed() {
        val debt = Debt(
                id = -80000,
                oweTo = "Owe to test updateFailed",
                debtCreatedAt = 100000,
                totalDebt = 88888.8,
                debtPaid = 99999.9
        )

        mockMvc.perform(MockMvcRequestBuilders.put(debtsEndPoint)
                .body(data = debt, mapper = mapper))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))

    }

    @Test
    fun deleteByIdSuccessFully() {
        val debt = getOneDebtFromService(takeFirst = false)

        val debtFromApi: Debt = mockMvc.perform(MockMvcRequestBuilders.delete("$debtsEndPoint/${debt.id}"))
                .andExpect(status().isOk)
                .bodyTo(mapper)

        assert(!debtService.findAll().contains(debtFromApi))
    }

    @Test
    fun deleteByIdFail() {
        mockMvc.perform(MockMvcRequestBuilders.delete("$debtsEndPoint/-1"))
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
    }

    @Test
    fun saveCheckRules() {
        val debt = Debt(
                id = -99999,
                oweTo = "",
                debtCreatedAt = 100000,
                totalDebt = 88888.8,
                debtPaid = 99999.9
        )

        mockMvc.perform(MockMvcRequestBuilders.post(debtsEndPoint)
                .body(data = debt, mapper = mapper))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.oweTo").exists())

    }

    @Test
    fun updateCheckRules() {
        val debt = getOneDebtFromService().copy(oweTo = "")
        
        mockMvc.perform(MockMvcRequestBuilders.put(debtsEndPoint)
                .body(data = debt, mapper = mapper))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.oweTo").exists())
    }

    private fun getOneDebtFromService(takeFirst: Boolean = true): Debt {
        val debtsFromService = debtService.findAll()
        assert(debtsFromService.isNotEmpty()) { "Should not be empty" }

        return if (takeFirst) debtsFromService.first() else debtsFromService.last()
    }
}
