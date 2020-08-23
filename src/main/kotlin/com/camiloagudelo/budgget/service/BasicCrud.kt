package com.camiloagudelo.budgget.service

interface BasicCrud<T, ID> {
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun save(body: T): T
    fun update(body: T): T
    fun deleteById(id: ID): T
}