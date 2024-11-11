package com.jnasser.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserDataValidatorTest {

    @Test
    fun testAddingTwoNumber() {
        assertThat(2 + 2).isEqualTo(4)
    }
}