package com.changs.routesearch.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

fun <T> safeFlow(apiFunc: suspend () -> Response<T>): Flow<ApiResult<T>> = flow {
    try {
        val res = apiFunc.invoke()
        if (res.isSuccessful) {
            val body = res.body() ?: throw java.lang.NullPointerException()
            emit(ApiResult.Success(body))
        }
    } catch (e: NullPointerException) {
        emit(ApiResult.Empty)
    } catch (e: HttpException) {
        emit(ApiResult.Error(exception = e))
    } catch (e: Exception) {
        emit(ApiResult.Error(exception = e))
    }
}