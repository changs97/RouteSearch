package com.changs.routesearch.util

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    object Empty : ApiResult<Nothing>()
    data class Error(
        val exception: Throwable? = null,
        var message: String? = ""
    ) : ApiResult<Nothing>()

    fun handleResponse(
        emptyMsg: String = "There is no result value.",
        errorMsg: String = "Please check your internet status.",
        onError: (String) -> Unit,
        onSuccess: (T) -> Unit,
    ) {
        when (this@ApiResult) {
            is Success -> onSuccess(this@ApiResult.value)
            is Empty -> handleException {
                onError(emptyMsg)
            }
            is Error -> handleException(exception) {
                onError(errorMsg)
            }
        }
    }

    private fun handleException(
        exception: Throwable? = null,
        onError: () -> Unit,
    ) {
        exception?.printStackTrace()
        onError()
    }
}