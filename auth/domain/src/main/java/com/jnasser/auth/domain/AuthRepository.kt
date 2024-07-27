package com.jnasser.auth.domain

import com.jnasser.core.domain.util.DataError
import com.jnasser.core.domain.util.EmptyResult
import com.jnasser.core.domain.util.Result


interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
}