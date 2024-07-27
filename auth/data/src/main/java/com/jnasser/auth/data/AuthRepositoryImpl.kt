package com.jnasser.auth.data

import com.jnasser.auth.domain.AuthRepository
import com.jnasser.core.data.networking.post
import com.jnasser.core.domain.util.AuthInfo
import com.jnasser.core.domain.util.DataError
import com.jnasser.core.domain.util.EmptyResult
import com.jnasser.core.domain.util.Result
import com.jnasser.core.domain.util.SessionStorage
import com.jnasser.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository {

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(email, password)
        )
        if(result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    result.data.accessToken,
                    result.data.refreshToken,
                    result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(email, password)
        )
    }
}