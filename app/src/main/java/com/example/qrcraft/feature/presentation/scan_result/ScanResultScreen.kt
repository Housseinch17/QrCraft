package com.example.qrcraft.feature.presentation.scan_result

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qrcraft.R
import com.example.qrcraft.core.data.model.BarModel
import com.example.qrcraft.core.data.model.ScannedContent
import com.example.qrcraft.core.presentation.designsystem.components.QrCraftIOutlinedButtonWithIcon
import com.example.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.example.qrcraft.core.presentation.designsystem.theme.QrCraftIcons
import com.example.qrcraft.core.presentation.designsystem.theme.QrCraftImages
import com.example.qrcraft.core.presentation.designsystem.theme.link
import com.example.qrcraft.core.presentation.designsystem.theme.linkBG
import com.example.qrcraft.core.presentation.designsystem.theme.onOverlay
import com.example.qrcraft.core.presentation.designsystem.theme.onSurfaceAlt
import com.example.qrcraft.core.presentation.designsystem.theme.onSurfaceDisabled
import com.example.qrcraft.core.presentation.ui.ObserveAsEvents
import com.example.qrcraft.feature.mapper.displayType
import com.example.qrcraft.feature.util.shareText

@Composable
fun ScanResultRoot(
    modifier: Modifier = Modifier,
    scanResultViewModel: ScanResultViewModel,
    navigateBack: () -> Unit,
) {
    val state by scanResultViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    BackHandler {
        navigateBack()
    }

    ObserveAsEvents(scanResultViewModel.events) { events ->
        when (events) {
            is ScanResultEvents.Copy -> {
                val clip = ClipData.newPlainText("Copied Text", events.rawValue)
                clipboardManager.setPrimaryClip(clip)
                Toast.makeText(context, "Copied Text!", Toast.LENGTH_LONG).show()
            }

            is ScanResultEvents.Share -> {
                context.shareText(text = events.rawValue)
            }
        }
    }

    ScanResultScreen(
        modifier = modifier,
        navigateBack = navigateBack,
        state = state,
        onActions = scanResultViewModel::onActions
    )
}

@Composable
fun ScanResultScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    state: ScanResultUiState,
    onActions: (ScanResultActions) -> Unit,
) {
    QRCraftTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.onSurface,
            topBar = {
                ScanResultTopBar(
                    navigateBack = navigateBack
                )
            }
        ) { innerPadding ->
            ScanResultContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                barcodeModel = state.barModel,
                onShareClick = {
                    onActions(ScanResultActions.Share)
                },
                onCopyClick = {
                    onActions(ScanResultActions.Copy)
                },
            )
        }
    }
}

@Composable
fun ScanResultTopBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.scan_result),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onOverlay
            )
        )

        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = navigateBack),
            imageVector = QrCraftIcons.arrowLeft,
            contentDescription = stringResource(R.string.navigate_back),
            tint = MaterialTheme.colorScheme.onOverlay
        )
    }
}

@Composable
fun ScanResultContent(
    modifier: Modifier = Modifier,
    barcodeModel: BarModel,
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        val imageSize = maxWidth * 0.5f
        val barcodeContentBlockPadding = (imageSize / 2)

        BarcodeContentBlock(
            modifier = Modifier
                .padding(top = barcodeContentBlockPadding),
            topPadding = imageSize / 2,
            barcodeModel = barcodeModel,
            onShareClick = onShareClick,
            onCopyClick = onCopyClick
        )

        BarcodeImage(
            modifier = Modifier
                .size(imageSize)
        )
    }
}

@Composable
fun BarcodeImage(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.Transparent),
        painter = QrCraftImages.barcode,
        contentDescription = stringResource(R.string.barcode),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun BarcodeContentBlock(
    modifier: Modifier = Modifier,
    topPadding: Dp,
    barcodeModel: BarModel,
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(start = 16.dp, end = 16.dp, top = topPadding, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = barcodeModel.content.displayType(),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        BarcodeContent(
            modifier = Modifier.weight(1f, fill = false),
            scanningContent = barcodeModel.content
        )
        Spacer(modifier = Modifier.height(24.dp))
        ScanResultButtons(
            onShareClick = onShareClick,
            onCopyClick = onCopyClick
        )
    }
}

@Composable
fun BarcodeContent(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent,
) {
    when (scanningContent) {
        is ScannedContent.ContactContent -> ContactContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )

        is ScannedContent.EmailContent -> EmailContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )

        is ScannedContent.GeoContent -> GeolocationContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )

        is ScannedContent.LinkContent -> LinkContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )

        is ScannedContent.PhoneContent -> PhoneContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )

        is ScannedContent.TextContent -> TextContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )

        is ScannedContent.WifiContent -> WifiContentView(
            modifier = modifier,
            scanningContent = scanningContent
        )
    }
}

@Composable
fun TextContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.TextContent,
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var shouldShowButton by rememberSaveable {
        mutableStateOf(false)
    }

    val verticalScroll = rememberScrollState()

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .weight(1f, false)
                .verticalScroll(verticalScroll),
            text = scanningContent.value,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = if (isExpanded) Int.MAX_VALUE else 6,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                //hasVisualOverflow is true when text is clipped/ellipsized
                if (!shouldShowButton && (result.hasVisualOverflow || result.lineCount >= 6)) {
                    shouldShowButton = true
                }
            }
        )

        AnimatedVisibility(visible = shouldShowButton) {
            Text(
                modifier = Modifier
                    .clickable(onClick = {
                        isExpanded = !isExpanded
                    })
                    .padding(top = 8.dp),
                text = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if (isExpanded) MaterialTheme.colorScheme.onSurfaceDisabled else MaterialTheme.colorScheme.onSurfaceAlt
                )
            )
        }
    }
}

@Composable
fun EmailContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.EmailContent
) {
    Text(
        modifier = modifier,
        text = scanningContent.address,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun LinkContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.LinkContent
) {
    val uriHandler = LocalUriHandler.current
    Text(
        modifier = modifier
            .background(MaterialTheme.colorScheme.linkBG)
            .clickable(onClick = {
                uriHandler.openUri(uri = scanningContent.url)
            }),
        text = scanningContent.url,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.link
        )
    )
}

@Composable
fun ContactContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.ContactContent
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            text = scanningContent.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = Modifier,
            text = scanningContent.email ?: stringResource(R.string.unknown),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = Modifier,
            text = scanningContent.phone ?: stringResource(R.string.unknown),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun PhoneContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.PhoneContent
) {
    Text(
        modifier = modifier,
        text = scanningContent.number,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun GeolocationContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.GeoContent
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier,
            text = scanningContent.latitude + ",",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = Modifier,
            text = scanningContent.longitude,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun WifiContentView(
    modifier: Modifier = Modifier,
    scanningContent: ScannedContent.WifiContent
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier,
            text = stringResource(R.string.ssid) + ": ${scanningContent.ssid}",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = modifier,
            text = stringResource(R.string.password) + ": ${
                scanningContent.password ?: stringResource(
                    R.string.unknown
                )
            }",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = modifier,
            text = stringResource(R.string.encryption_type) + ": ${scanningContent.securityType}",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun ScanResultButtons(
    modifier: Modifier = Modifier,
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        QrCraftIOutlinedButtonWithIcon(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.share),
            icon = QrCraftIcons.share,
            onClick = onShareClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        QrCraftIOutlinedButtonWithIcon(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.copy),
            icon = QrCraftIcons.share,
            onClick = onCopyClick
        )
    }
}


