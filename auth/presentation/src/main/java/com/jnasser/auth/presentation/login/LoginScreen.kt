@file:OptIn(ExperimentalFoundationApi::class)

package com.jnasser.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jnasser.auth.presentation.R
import com.jnasser.auth.presentation.register.RegisterAction
import com.jnasser.core.presentation.designsystem.EmailIcon
import com.jnasser.core.presentation.designsystem.Poppins
import com.jnasser.core.presentation.designsystem.RuniqueGray
import com.jnasser.core.presentation.designsystem.RuniqueTheme
import com.jnasser.core.presentation.designsystem.components.GradientBackground
import com.jnasser.core.presentation.designsystem.components.RuniqueActionButton
import com.jnasser.core.presentation.designsystem.components.RuniquePasswordTextField
import com.jnasser.core.presentation.designsystem.components.RuniqueTextField
import com.jnasser.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.your_logged_in,
                    Toast.LENGTH_LONG
                ).show()

                onLoginSuccess()
            }
        }
    }
    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                is LoginAction.OnRegisterClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    GradientBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.hi_there),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(id = R.string.runique_welcome_text),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(48.dp))

                RuniqueTextField(
                    state = state.email,
                    startIcon = EmailIcon,
                    endIcon = null,
                    keyboardType = KeyboardType.Email,
                    hint = stringResource(id = R.string.example_email),
                    title = stringResource(id = R.string.email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                RuniquePasswordTextField(
                    state = state.password,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePasswordVisibility = {
                        onAction(LoginAction.OnTogglePasswordVisibility)
                    },
                    hint = stringResource(id = R.string.password),
                    title = stringResource(id = R.string.password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                RuniqueActionButton(
                    text = stringResource(id = R.string.login),
                    isLoading = state.isLoggingIn,
                    enable = state.canLogin && !state.isLoggingIn,
                    onClick = {
                        onAction(LoginAction.OnLogInClick)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = Poppins,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        append(stringResource(id = R.string.dont_have_an_account) + " ")
                        pushStringAnnotation(
                            tag = "clickable_text",
                            annotation = stringResource(id = R.string.signUp)
                        )
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = Poppins
                            )
                        ) {
                            append(stringResource(id = R.string.signUp))
                        }
                    }
                }
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "clickable_text",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            onAction(LoginAction.OnRegisterClick)
                        }
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun LoginScreenPreview() {
    RuniqueTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}