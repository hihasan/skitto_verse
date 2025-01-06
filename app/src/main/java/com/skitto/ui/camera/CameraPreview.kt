package com.skitto.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CameraPreview(onSurfaceDetected: (Boolean) -> Unit) {
    val context = LocalContext.current
    AndroidView(factory = { context ->
        val textureView = TextureView(context)
        setupCamera(context, textureView, onSurfaceDetected)
        textureView
    })
}

fun setupCamera(context: Context, textureView: TextureView, onSurfaceDetected: (Boolean) -> Unit) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList.firstOrNull() ?: return

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        return
    }

    textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        private var reusableBitmap: Bitmap? = null
        private var frameCounter = 0
        private val scope = CoroutineScope(Dispatchers.Default)

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            openCamera(context, cameraManager, cameraId, surface, onSurfaceDetected)
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = true

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            frameCounter++
            // Process every 3rd frame
            if (frameCounter % 3 == 0) {
                if (reusableBitmap == null) {
                    reusableBitmap = Bitmap.createBitmap(textureView.width, textureView.height, Bitmap.Config.ARGB_8888)
                }
                textureView.getBitmap(reusableBitmap!!)
                val bitmap = reusableBitmap ?: return

                // Use coroutines to process the frame on a background thread
                scope.launch {
                    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
                    val isRedSurface = detectRedSurface(scaledBitmap)
                    withContext(Dispatchers.Main) {
                        onSurfaceDetected(isRedSurface)
                    }
                }
            }
        }
    }
}

fun openCamera(
    context: Context,
    cameraManager: CameraManager,
    cameraId: String,
    surfaceTexture: SurfaceTexture,
    onSurfaceDetected: (Boolean) -> Unit
) {
    val handlerThread = HandlerThread("CameraBackground").apply { start() }
    val backgroundHandler = Handler(handlerThread.looper)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        return
    }

    val characteristics = cameraManager.getCameraCharacteristics(cameraId)
    val streamConfigurationMap = characteristics[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
    val previewSize = streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)?.find { it.width <= 1280 && it.height <= 720 }

    surfaceTexture.setDefaultBufferSize(previewSize?.width ?: 1280, previewSize?.height ?: 720)

    cameraManager.openCamera(
        cameraId,
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                val surface = Surface(surfaceTexture)
                val captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surface)

                // Start the camera preview
                camera.createCaptureSession(
                    listOf(surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            session.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            // Handle failure
                        }
                    },
                    backgroundHandler
                )
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                camera.close()
                // Handle error
            }
        },
        backgroundHandler
    )
}

fun detectRedSurface(bitmap: Bitmap): Boolean {
    val width = bitmap.width
    val height = bitmap.height
    val totalPixelCount = width * height
    val pixels = IntArray(totalPixelCount)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    var redPixelCount = 0
    var totalLuminance = 0

    for (i in pixels.indices step 4) { // Skip every 4th pixel for efficiency
        val pixel = pixels[i]
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)

        // Calculate brightness (luminance)
        val luminance = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
        totalLuminance += luminance

        // Convert to HSV and check if the hue corresponds to red
        val hsv = FloatArray(3)
        Color.RGBToHSV(red, green, blue, hsv)

        val isRedHue = hsv[0] in 0f..30f || hsv[0] in 330f..360f
        val isBrightEnough = hsv[2] > 0.2 // Value (brightness) > 20%
        val isSaturated = hsv[1] > 0.5   // Saturation > 50%

        if (isRedHue && isBrightEnough && isSaturated) {
            redPixelCount++
        }
    }

    // Adjust red threshold based on average luminance
    val averageLuminance = totalLuminance / (totalPixelCount / 4)
    val redThreshold = if (averageLuminance < 50) 0.1 else 0.2 // 10% if low light, else 20%

    return redPixelCount > (totalPixelCount * redThreshold)
}


//fun detectRedSurface(bitmap: Bitmap): Boolean {
//    var redPixelCount = 0
//    val totalPixelCount = bitmap.width * bitmap.height
//
//    for (x in 0 until bitmap.width) {
//        for (y in 0 until bitmap.height) {
//            val pixel = bitmap.getPixel(x, y)
//            val red = Color.red(pixel)
//            val green = Color.green(pixel)
//            val blue = Color.blue(pixel)
//
//            if (red > 200 && green < 100 && blue < 100) {
//                redPixelCount++
//            }
//        }
//    }
//
//    // Consider it a red surface if more than 20% of the pixels are red
//    return redPixelCount > (totalPixelCount * 0.2)
//}


