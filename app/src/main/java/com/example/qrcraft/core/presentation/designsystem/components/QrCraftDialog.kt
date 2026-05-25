package com.example.qrcraft.core.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun QrCraftDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    dismissText: String,
    confirmText: String,
    dismiss: () -> Unit,
    confirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = {},
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
        },
        confirmButton = {
            QrCraftButton(
                text = confirmText,
                onClick = confirm,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        },
        dismissButton = {
            QrCraftButton(
                text = dismissText,
                onClick = dismiss,
                textColor = MaterialTheme.colorScheme.error
            )
        }
    )
}