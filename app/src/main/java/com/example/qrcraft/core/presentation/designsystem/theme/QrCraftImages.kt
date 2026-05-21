package com.example.qrcraft.core.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.qrcraft.R

object QrCraftImages {
    val barcode: Painter
    @Composable
    get() = painterResource(R.drawable.barcode)

    val frame: Painter
        @Composable
        get() = painterResource(R.drawable.frame)
}