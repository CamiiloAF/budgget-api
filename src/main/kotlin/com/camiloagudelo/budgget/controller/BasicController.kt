package com.camiloagudelo.budgget.controller

import com.camiloagudelo.budgget.service.BasicCrud
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

abstract class BasicController<T, ID>(private val basicCrud: BasicCrud<T, ID>) {
    @GetMapping
    fun findAll() = basicCrud.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: ID): ResponseEntity<T> {
        val entity = basicCrud.findById(id)
        return ResponseEntity.status(if (entity != null) HttpStatus.OK else HttpStatus.NOT_FOUND).body(entity)
    }

    @PostMapping
    fun save(@Valid @RequestBody body: T) =
            ResponseEntity.status(HttpStatus.CREATED).body(basicCrud.save(body))

    @PutMapping
    fun update(@Valid @RequestBody body: T) = basicCrud.update(body)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: ID) = basicCrud.deleteById(id)
}