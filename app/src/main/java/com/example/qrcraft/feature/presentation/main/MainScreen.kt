package com.example.qrcraft.feature.presentation.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.viewfinder.core.ImplementationMode
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qrcraft.R
import com.example.qrcraft.core.data.model.BarModel
import com.example.qrcraft.core.presentation.designsystem.components.QrCraftDialog
import com.example.qrcraft.core.presentation.designsystem.components.QrCraftSnackBar
import com.example.qrcraft.core.presentation.designsystem.theme.QrCraftIcons
import com.example.qrcraft.core.presentation.designsystem.theme.onOverlay
import com.example.qrcraft.core.presentation.ui.ObserveAsEvents
import com.example.qrcraft.core.presentation.ui.UiText
import com.example.qrcraft.feature.data.mapper.toBarModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MainRoot(
    modifier: Modifier = Modifier,
    mainScreenViewModel: MainScreenViewModel = koinViewModel(),
    navigateToScanResult: (BarModel) -> Unit = {},
) {
    val state by mainScreenViewModel.state.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    val isCameraPermissionGranted = remember {
        context.checkSelfPermission(
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            mainScreenViewModel.onActions(
                MainScreenActions.UpdateCameraPermission(
                    hasGrantCameraPermission = isGranted
                )
            )
        }

    LaunchedEffect(Unit) {
        mainScreenViewModel.onActions(
            MainScreenActions.UpdateCameraPermission(
                hasGrantCameraPermission = isCameraPermissionGranted
            )
        )
    }

    LaunchedEffect(state.showSnackBar) {
        if (state.showSnackBar) {
            snackBarHostState.showSnackbar(
                message = UiText.StringResource(R.string.camera_permission_granted)
                    .asString(context = context), withDismissAction = false
            )
        }
    }

    ObserveAsEvents(mainScreenViewModel.events) { events ->
        when (events) {
            MainScreenEvents.RequestCameraPermission -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            MainScreenEvents.CloseApp -> {
                (context as? Activity)?.finishAndRemoveTask()

            }

            is MainScreenEvents.NavigateToScanResult -> {
                navigateToScanResult(events.barModel)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    QrCraftSnackBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        data = data
                    )
                })
        }
    ) { innerPadding ->
        MainScreen(
            modifier = modifier.padding(innerPadding),
            showCameraPermissionDialog = !(state.hasGrantCameraPermission),
            isScanningBarcode = state.isScanningBarcode,
            dismiss = {
                mainScreenViewModel.onActions(MainScreenActions.CloseApp)
            },
            confirm = {
                mainScreenViewModel.onActions(MainScreenActions.GrantAccess)
            },
            updateIsScanningBarcode = { isScanningBarcode ->
                mainScreenViewModel.onActions(
                    MainScreenActions.UpdateIsScanningBarcode(
                        isScanningBarcode = isScanningBarcode
                    )
                )
            },
            onBarcodeScanning = { barModel ->
                Timber.tag("MyTag").d("barModel: $barModel")
                mainScreenViewModel.onActions(
                    MainScreenActions.OnBarcodeScanning(
                        barModel = barModel
                    )
                )
            },
        )
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    showCameraPermissionDialog: Boolean,
    isScanningBarcode: Boolean,
    dismiss: () -> Unit,
    confirm: () -> Unit,
    updateIsScanningBarcode: (Boolean) -> Unit,
    onBarcodeScanning: (BarModel) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MainScreenCameraPermissionDialog(
            modifier = Modifier.fillMaxWidth(),
            showCameraPermissionDialog = showCameraPermissionDialog,
            dismiss = dismiss,
            confirm = confirm
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CameraPreviewScreen(
                onBarcodeScanned = onBarcodeScanning,
                isScanningBarcode = isScanningBarcode,
                updateIsScanningBarcode = updateIsScanningBarcode
            )
        }
    }
}

@Composable
fun CameraPreviewScreen(
    isScanningBarcode: Boolean,
    onBarcodeScanned: (BarModel) -> Unit,
    updateIsScanningBarcode: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ✅ New API — surfaceRequest comes from ViewModel via Preview use case
    val previewUseCase = remember { Preview.Builder().build() }
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    val barcodeScanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            barcodeScanner.close()
        }
    }

    val imageAnalyzer = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context)
                ) { imageProxy ->
                    processImageProxy(
                        barcodeScanner = barcodeScanner,
                        imageProxy = imageProxy,
                        updateIsScanningBarcode = updateIsScanningBarcode,
                        onBarcodeScanned = onBarcodeScanned,
                        isScanning = isScanningBarcode
                    )
                }
            }
    }

    LaunchedEffect(lifecycleOwner) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)

        // ✅ Collect surfaceRequests from Preview
        previewUseCase.setSurfaceProvider { request ->
            surfaceRequest = request
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                imageAnalyzer
            )
        } catch (e: Exception) {
            Timber.tag("MyTag").e(e, "Camera binding failed")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        surfaceRequest?.let { request ->
            CameraXViewfinder(
                modifier = Modifier.fillMaxSize(),
                surfaceRequest = request,
                implementationMode = ImplementationMode.EMBEDDED,
            )
        }

        ScanningFrameOverlay()

        AnimatedVisibility(
            visible = isScanningBarcode,
            modifier = Modifier.align(Alignment.Center),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = MaterialTheme.colorScheme.onOverlay
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.loading) + "...",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onOverlay
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    isScanning: Boolean,
    updateIsScanningBarcode: (Boolean) -> Unit,
    onBarcodeScanned: (BarModel) -> Unit,
) {
    if (isScanning) {
        imageProxy.close()
        return
    }

    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }

    val image = InputImage.fromMediaImage(
        mediaImage,
        imageProxy.imageInfo.rotationDegrees
    )

    barcodeScanner.process(image)
        .addOnSuccessListener { barcodes ->
            if (barcodes.isEmpty()) {
                return@addOnSuccessListener
            }
            barcodes.firstOrNull()?.let { barcode ->
                val barModel = barcode.toBarModel()
                updateIsScanningBarcode(true)
                onBarcodeScanned(barModel)
            }
        }
        .addOnFailureListener {
            updateIsScanningBarcode(false)
        }
        .addOnCompleteListener { task->
            if (!task.isSuccessful || task.result.isNullOrEmpty()) {
                updateIsScanningBarcode(false)
            }
            imageProxy.close()
        }
}

@Composable
fun ScanningFrameOverlay() {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.qr_scan_instruction),
            style = MaterialTheme.typography.titleSmall.copy(
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            modifier = Modifier.fillMaxWidth(),
            imageVector = QrCraftIcons.frame,
            contentDescription = stringResource(R.string.frame),
        )
    }
}

@Composable
fun MainScreenCameraPermissionDialog(
    modifier: Modifier = Modifier,
    showCameraPermissionDialog: Boolean,
    dismiss: () -> Unit,
    confirm: () -> Unit
) {
    AnimatedVisibility(visible = showCameraPermissionDialog) {
        QrCraftDialog(
            modifier = modifier,
            title = stringResource(R.string.camera_permission_title),
            description = stringResource(R.string.camera_permission_description),
            dismissText = stringResource(R.string.close_app),
            confirmText = stringResource(R.string.grant_access),
            dismiss = dismiss,
            confirm = confirm
        )
    }
}