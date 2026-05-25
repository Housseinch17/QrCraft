package com.example.qrcraft.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.qrcraft.R
import com.example.qrcraft.core.presentation.designsystem.theme.success

@Composable
fun QrCraftSnackBar(
    modifier: Modifier = Modifier,
    data: SnackbarData
){
    Snackbar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.success,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.check),
                tint = LocalContentColor.current
            )
            Text(
                text = data.visuals.message,
                color = LocalContentColor.current
            )
        }
    }
}