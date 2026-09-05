package com.nova.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nova.app.ui.theme.NovaTheme
import com.nova.app.ui.theme.NovaThemeStyle
import com.nova.app.ui.theme.ThemeCenter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NovaApp()
        }
    }
}

@Composable
fun NovaApp() {

    var selectedTheme by remember {
        mutableStateOf(NovaThemeStyle.OCEAN)
    }

    var darkMode by remember {
        mutableStateOf(false)
    }

    var showThemeCenter by remember {
        mutableStateOf(false)
    }

    NovaTheme(
        themeStyle = selectedTheme,
        darkMode = darkMode
    ) {

        if (showThemeCenter) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("NOVA Themes")
                        },
                        navigationIcon = {
                            TextButton(
                                onClick = {
                                    showThemeCenter = false
                                }
                            ) {
                                Text("Back")
                            }
                        }
                    )
                }
            ) { padding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    ThemeCenter(
                        selectedTheme = selectedTheme,
                        darkMode = darkMode,
                        onThemeSelected = {
                            selectedTheme = it
                        },
                        onDarkModeChanged = {
                            darkMode = it
                        }
                    )
                }
            }

        } else {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("NOVA")
                        },
                        actions = {
                            TextButton(
                                onClick = {
                                    showThemeCenter = true
                                }
                            ) {
                                Text("Themes")
                            }
                        }
                    )
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Welcome to NOVA",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Your intelligent assistant",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(
                        modifier = Modifier.height(28.dp)
                    )

                    Button(
                        onClick = {
                            showThemeCenter = true
                        }
                    ) {
                        Text("🎨  Choose Theme")
                    }
                }
            }
        }
    }
}
