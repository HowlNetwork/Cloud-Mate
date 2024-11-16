package com.example.cloudmate.network.common

class AppResponse<T, Boolean, Exception> (
    var data: T? = null,
    var success: Boolean? = null,
    var e: Exception? = null
)