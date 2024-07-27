package com.jnasser.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasNumber = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasMinimumLength = hasMinLength,
            hasUpperCaseCharacter = hasUpperCaseCharacter,
            hasNumber = hasNumber,
            hasLowerCaseCharacter = hasLowerCaseCharacter
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }

}