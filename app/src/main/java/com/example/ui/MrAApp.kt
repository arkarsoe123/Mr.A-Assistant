package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MrAApp(
    viewModel: MrAViewModel,
    modifier: Modifier = Modifier
) {
    // Shows the Welcome loading screen once per session on app launch
    var hasFinishedWelcome by rememberSaveable { mutableStateOf(false) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = when {
                !hasFinishedWelcome -> AppAuthState.WELCOME
                !isLoggedIn -> AppAuthState.AUTH
                else -> AppAuthState.HOME
            },
            animationSpec = tween(durationMillis = 400),
            label = "app_auth_flow"
        ) { state ->
            when (state) {
                AppAuthState.WELCOME -> {
                    WelcomeLoadingScreen(
                        onContinue = {
                            hasFinishedWelcome = true
                        }
                    )
                }

                AppAuthState.AUTH -> {
                    AuthScreen(
                        viewModel = viewModel
                    )
                }

                AppAuthState.HOME -> {
                    MrAHomeScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

private enum class AppAuthState {
    WELCOME,
    AUTH,
    HOME
}
