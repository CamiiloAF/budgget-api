package com.camiloagudelo.budgget.dto

data class ApiResponse<T>(val status: Int, val payload: T?)