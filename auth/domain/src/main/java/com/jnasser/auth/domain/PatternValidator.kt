package com.jnasser.auth.domain

interface PatternValidator {

    fun matches(value: String): Boolean

}