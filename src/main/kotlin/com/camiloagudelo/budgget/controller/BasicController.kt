package com.camiloagudelo.budgget.controller

import com.camiloagudelo.budgget.dto.ApiResponse
import com.camiloagudelo.budgget.service.BasicCrud
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

abstract class BasicController<T, ID>(private val basicCrud: BasicCrud<T, ID>) {
    @GetMapping
    fun findAll(): ApiResponse<List<T>> {
        val entity =basicCrud.findAll()
        val status = if (!entity.isNullOrEmpty()) HttpStatus.OK else HttpStatus.NOT_FOUND
        return ApiResponse(status = status.value(), payload = entity)

    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: ID): ResponseEntity<ApiResponse<T>> {
        val entity = basicCrud.findById(id)
        val status = if (entity != null) HttpStatus.OK else HttpStatus.NOT_FOUND
        return ResponseEntity.status(status).body(ApiResponse(payload = entity, status = status.value()))
    }

    @PostMapping
    fun save(@Valid @RequestBody body: T): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse(status = HttpStatus.CREATED.value(), payload = basicCrud.save(body))
            )

    @PutMapping
    fun update(@Valid @RequestBody body: T): ApiResponse<T> {
        val entity = basicCrud.update(body)
        val status = if (entity != null) HttpStatus.NO_CONTENT else HttpStatus.CONFLICT
        return ApiResponse(status = status.value(), payload = entity)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: ID): ApiResponse<T> {
        val entity = basicCrud.deleteById(id)
        val status = if (entity != null) HttpStatus.OK else HttpStatus.CONFLICT
        return ApiResponse(status = status.value(), payload = entity)
    }
}