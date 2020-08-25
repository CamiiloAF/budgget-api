package com.camiloagudelo.budgget.controller

import com.camiloagudelo.budgget.dto.ApiErrorResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class RestResponseEntityErrorHandler : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val result: Map<String, List<String?>> = ex.bindingResult.fieldErrors.groupBy({ it.field }, { it.defaultMessage })
        return ResponseEntity.status(status).headers(headers).body(result)
    }

    @ExceptionHandler(DuplicateKeyException::class, EntityNotFoundException::class)
    fun handleJpa(exception: Exception): ResponseEntity<ApiErrorResponse> {
        val status = HttpStatus.CONFLICT
        return ResponseEntity.status(status).body(
                ApiErrorResponse(
                        status = status.value(),
                        title = exception::class.simpleName.toString(),
                        message = exception.localizedMessage
                )
        )
    }
}