package com.nova.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ThemeCenter(
    selectedTheme: NovaThemeStyle,
    darkMode: Boolean,
    onThemeSelected: (NovaThemeStyle) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Theme Center",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Personalize the NOVA experience",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Dark Mode",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Use a darker interface",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = darkMode,
                    onCheckedChange = onDarkModeChanged
                )
            }
        }

        Text(
            "Color Themes",
            style = MaterialTheme.typography.titleLarge
        )

        NovaThemeStyle.entries.forEach { theme ->
            ThemeCard(
                theme = theme,
                selected = theme == selectedTheme,
                onClick = { onThemeSelected(theme) }
            )
        }
    }
}

@Composable
private fun ThemeCard(
    theme: NovaThemeStyle,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        border = if (selected)
            androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            )
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                theme.primary,
                                theme.secondary,
                                theme.tertiary
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    )
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    theme.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "NOVA ${if (selected) "• Active" else "Theme"}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (selected) {
                RadioButton(
                    selected = true,
                    onClick = onClick
                )
            }
        }
    }
}
