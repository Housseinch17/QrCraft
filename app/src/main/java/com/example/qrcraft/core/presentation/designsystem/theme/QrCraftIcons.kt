package com.example.qrcraft.core.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.qrcraft.R

object QrCraftIcons {
    val alert: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.alert)

    val arrowLeft: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.arrow_left)

    val clockRefresh: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.clock_refresh)

    val copy: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.copy)

    val heart: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.heart)

    val image: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.image)

    val link: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.link)

    val marker: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.marker)

    val phone: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.phone)

    val plusCircle: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.plus_circle)

    val scan: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.scan)

    val share: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.share)

    val type: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.type)

    val user: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.user)

    val wifi: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.wifi)

    val zap: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.zap)

    val zapOff: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.zap_off)
}