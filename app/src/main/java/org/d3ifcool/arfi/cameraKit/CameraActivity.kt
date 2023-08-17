package org.d3ifcool.arfi.cameraKit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.arfi.R
import android.app.Activity
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.KeyEvent
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.snap.camerakit.LegalProcessor
import com.snap.camerakit.Session
import com.snap.camerakit.UnauthorizedApplicationException
import com.snap.camerakit.connectOutput
import com.snap.camerakit.lenses.LENS_GROUP_ID_BUNDLED
import com.snap.camerakit.lenses.LensesComponent
import com.snap.camerakit.lenses.apply
import com.snap.camerakit.lenses.configureEachItem
import com.snap.camerakit.lenses.invoke
import com.snap.camerakit.lenses.observe
import com.snap.camerakit.lenses.whenApplied
import com.snap.camerakit.lenses.whenIdle
import com.snap.camerakit.support.widget.CameraLayout
import com.snap.camerakit.support.widget.LensesCarouselView
import com.snap.camerakit.support.widget.arCoreSupportedAndInstalled
import com.snap.camerakit.versionFrom
import org.d3ifcool.arfi.BuildConfig
import java.io.Closeable
import java.util.Date

private const val TAG = "MainActivity"
private const val BUNDLE_ARG_USE_CUSTOM_LENSES_CAROUSEL = "use_custom_lenses_carousel"
private const val BUNDLE_ARG_MUTE_AUDIO = "mute_audio"
private const val BUNDLE_ARG_ENABLE_DIAGNOSTICS = "enable_diagnostics"
private const val CONFIG_KEY_DIAGNOSTICS_ENABLE = "com.snap.camerakit.diagnostics.enable"
private const val CONFIG_VALUE_DIAGNOSTICS_ENABLE =
    "MEUCIQCzXSKUlMwq2l9+wS6L4cnbEXP11jQPlCyXuAFMNsr9SgIgFYO+C44ddwekBcsBY5Ti6C9ZV5OwFdaWDEQ5AlqQx5A="
private const val ACTION_DIAGNOSTICS_DUMP = "com.snap.camerakit.diagnostics.DUMP"
private val LENS_GROUPS_ARCORE_AVAILABLE = arrayOf(
    BuildConfig.LENS_GROUP_ID_AR_CORE // lens group containing lenses using ARCore functionality.
)
private const val PREFS_CAMERA_KIT_SAMPLE = "camera_kit_sample"
private const val KEY_LENS_GROUPS = "lens_groups"
private const val KEY_API_TOKEN = "api_token"

/**
 * A simple activity which demonstrates how to use CameraKit to apply/remove lenses onto a camera preview.
 * Use of camera and management of the [com.snap.camerakit.Session] is done through the [CameraLayout] helper view
 * which is open to extension and customization depending on the app's needs.
 */
class CameraActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var cameraLayout: CameraLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lensGroups: Array<String>

    private val closeOnDestroy = mutableListOf<Closeable>()
    private var apiToken: String? = null
    private var useCustomLensesCarouselView = false
    private var muteAudio = false
    private var enableDiagnostics = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Using the CameraKit version: ${versionFrom(this)}")

        val metadata = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).metaData
        val lockPortraitOrientation = metadata?.getBoolean(getString(R.string.lock_portrait_orientation)) ?: false

        if (lockPortraitOrientation) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        sharedPreferences = getSharedPreferences(PREFS_CAMERA_KIT_SAMPLE, MODE_PRIVATE)
        apiToken = sharedPreferences.getString(KEY_API_TOKEN, null) ?: cameraKitApiToken
        lensGroups = sharedPreferences.getStringSet(KEY_LENS_GROUPS, null)?.toTypedArray()
            ?: if (arCoreSourceAvailable) {
                LENS_GROUPS_ARCORE_AVAILABLE
            } else {
                LENS_GROUPS_ARCORE_AVAILABLE
            }
        useCustomLensesCarouselView = savedInstanceState?.getBoolean(
            BUNDLE_ARG_USE_CUSTOM_LENSES_CAROUSEL
        ) ?: false
        muteAudio = savedInstanceState?.getBoolean(BUNDLE_ARG_MUTE_AUDIO) ?: false
        enableDiagnostics = savedInstanceState?.getBoolean(BUNDLE_ARG_ENABLE_DIAGNOSTICS) ?: false

        // OPTIONAL: For front flash, we change status and navigation bar colors to add extra illumination on subject.
        // In order for system bar colors to change, we need to change window flags to the following.
        window.apply {
            // To allow window to change color later (below).
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // The following only need to be cleared if activity/theme sets these flags.
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        setContentView(R.layout.activity_camera)
        val rootLayout = findViewById<DrawerLayout>(R.id.root_layout)

        // Some content may request additional data such as user name to personalize lenses. Providing this data is
        // optional, the MockUserProcessorSource class demonstrates a basic example to implement a source of the data.
        val mockUserProcessorSource = MockUserProcessorSource(
            userDisplayName = "Jane Doe",
            userBirthDate = Date(136985835000L)
        )
        // This sample uses the CameraLayout helper view that consolidates most common CameraKit use cases
        // into a single class that takes care of runtime permissions and managing CameraKit Session built
        // with default options that can be tweaked using the exposed configure* methods.
        // NOTE: Use of the CameraLayout is encouraged but it is completely optional and depends on the app's
        // requirements and architecture. However, consider working with the CameraKit Session directly to
        // avoid locking your app's design into one that is driven by what's available in the CameraLayout.
        cameraLayout = findViewById<CameraLayout>(R.id.camera_layout).apply {
            if (useCustomLensesCarouselView) {
                // Inflate layout with LensesCarouseView and ViewStub for CameraKit widgets into CameraLayout to use it
                // instead of internal carousel view implementation.
                layoutInflater.inflate(R.layout.lenses_carousel_widget_layout, this, true)
                // CaptureButton should be in front to overlap the lenses carousel.
                captureButton.bringToFront()
            }
            // CameraLayout provides a way to register callbacks for configuring CameraKit Session that
            // is created internally and made available via the onSessionAvailable callback below.
            configureSession {
                apiToken(apiToken)
                userProcessorSource(mockUserProcessorSource)
                // To provide Camera Kit developers with valuable diagnostics information, the Session can be configured
                // with a special signature that can be obtained from the Camera Kit support:
                if (enableDiagnostics) {
                    configureWith(CONFIG_KEY_DIAGNOSTICS_ENABLE, CONFIG_VALUE_DIAGNOSTICS_ENABLE)
                }
            }

            configureLenses {
                // If custom carousel view is inflated then CameraKit will attach lenses widgets to the
                // lenses_widgets_stub ViewStub. Custom ViewStub for widgets is required when custom lenses carousel
                // view is used.
                if (useCustomLensesCarouselView) {
                    attachWidgetsTo(findViewById(R.id.lenses_widgets_stub))
                }
                // Pass a factory which provides a demo service which handles remote API requests from lenses.
                remoteApiServiceFactory(CatFactRemoteApiService.Factory)

            }

            configureLensesCarousel {
                observedGroupIds = linkedSetOf(*lensGroups)
                // If custom carousel view is inflated then LensesCarouselView will be used otherwise CameraKit will
                // use an internal implementation. LensesCarouseView can be configured to customize appearance of the
                // lenses carousel.
                if (useCustomLensesCarouselView) {
                    view = findViewById<LensesCarouselView>(R.id.lenses_carousel)
                }
                // A lambda passed to configureEachItem can be used to customize position or appearance of each
                // item in the lenses carousel.
                configureEachItem {
                    if (lens.groupId == LENS_GROUP_ID_BUNDLED || index == 1) {
                        moveToLeft()
                    } else {
                        moveToRight()
                    }
                }
            }

            // Attach listener for flash state changes. Returned is a closeable to detach the listener on close.
            flashBehavior.attachOnFlashChangedListener(OnFlashChangedListener(window))
                .addTo(closeOnDestroy)
        }

        cameraLayout.onSessionAvailable { session ->
            // Adjust lenses volume considering current muteAudio value.
            session.adjustLensesVolume(muteAudio)

            // An example of how dynamic launch data can be used. Vendor specific metadata is added into
            // LaunchData so it can be used by lens on launch.
            val reApplyLensWithVendorData = { lens: LensesComponent.Lens ->
                if (lens.vendorData.isNotEmpty()) {
                    val launchData = LensesComponent.Lens.LaunchData {
                        for ((key, value) in lens.vendorData) {
                            putString(key, value)
                        }
                    }
                    session.lenses.processor.apply(lens, launchData) { success ->
                        Log.d(TAG, "Apply lens [$lens] with launch data [$launchData] success: $success")
                    }
                }
            }

            var appliedLens: LensesComponent.Lens? = null
            val lensAttribution = findViewById<TextView>(R.id.lens_attribution)
            // This block demonstrates how to receive and react to lens lifecycle events. When Applied event is received
            // we keep the ID of applied lens to persist and restore it via savedInstanceState later on.
            session.lenses.processor.observe { event ->
                Log.d(TAG, "Observed lenses processor event: $event")
                runOnUiThread {
                    event.whenApplied { event ->
                        reApplyLensWithVendorData(event.lens)
                        lensAttribution.text = event.lens.name
                        appliedLens = event.lens
                    }
                    event.whenIdle {
                        lensAttribution.text = null
                        appliedLens = null
                    }
                }
            }.addTo(closeOnDestroy)

            // By default, CameraKit does not reset lens state when app is backgrounded and resumed, however it is
            // possible to do so by simply tracking the last applied lens and applying it with the "reset" flag set
            // to true when app resumes to match the behavior of the Snapchat app.
            val lifecycleObserver = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    appliedLens
                        ?.let { lens ->
                            session.lenses.processor.apply(lens, reset = true)
                        }
                }
            }
            lifecycle.addObserver(lifecycleObserver)
            Closeable {
                lifecycle.removeObserver(lifecycleObserver)
            }.addTo(closeOnDestroy)

            // When CameraKit presents a legal prompt dialog, application may want to know if user has dismissed it
            // in order to de-activate lenses carousel for example.
            // The following block demonstrates how to observe and optionally handle LegalProcessor results:
            session.processor.observe { result ->
                Log.d(TAG, "Observed legal processor result: $result")
                if (result is LegalProcessor.Input.Result.Dismissed) {
                    session.lenses.carousel.deactivate()
                }
            }.addTo(closeOnDestroy)

            // Custom lenses carousel View could be provided only during Session setup process. That is why recreate()
            // method is called to restart activity with updated BUNDLE_ARG_USE_CUSTOM_LENSES_CAROUSEL argument

            // While CameraKit is capable (and does) render camera preview into an internal view, this demonstrates how
            // to connect another TextureView as rendering output.
            val miniPreview = cameraLayout.findViewById<TextureView>(R.id.mini_preview)
            var miniPreviewOutput = Closeable {}
            Closeable { miniPreviewOutput.close() }.addTo(closeOnDestroy)
            val setupMiniPreview = { connectOutput: Boolean ->
                miniPreviewOutput.close()
                if (connectOutput) {
                    miniPreview.visibility = View.VISIBLE
                    miniPreviewOutput = session.processor.connectOutput(miniPreview)
                } else {
                    miniPreview.visibility = View.GONE
                }
            }


        }

        // Register for a callback to present a captured video.
        cameraLayout.onVideoTaken { file ->
            PreviewActivity.startUsing(this@CameraActivity, cameraLayout, file, MIME_TYPE_VIDEO_MP4)
        }

        // Register for a callback to present a captured image.
        cameraLayout.onImageTaken { bitmap ->
            PreviewActivity.startUsing(
                this@CameraActivity, cameraLayout, this@CameraActivity.cacheJpegOf(bitmap), MIME_TYPE_IMAGE_JPEG
            )
        }

        // Register a handler for the specific CameraLayout exceptions as well as all the other possible errors from
        // the managed CameraKit Session.
        cameraLayout.onError { error ->
            val message = when (error) {
                is CameraLayout.Failure.MissingPermissions -> getString(
                    R.string.required_permissions_not_granted, error.permissions.joinToString(", ")
                )
                is CameraLayout.Failure.DeviceNotSupported -> getString(R.string.camera_kit_unsupported)
                else -> {
                    // Try to clear user provided but invalid API token to avoid crash on re-launch.
                    if (error is UnauthorizedApplicationException && error.apiToken == apiToken) {
                        sharedPreferences.edit().putString(KEY_API_TOKEN, null).apply()
                    }
                    if (!BuildConfig.DEBUG) {
                        // This allows app to catch unrecoverable errors and not cause app to crash in production. It is
                        // recommended to propagate this event to your crash reporter of choice for monitoring on the
                        // backend.
                        getString(R.string.camera_kit_error).also {
                            Log.e(TAG, it, error)
                        }
                    } else {
                        throw error
                    }
                }
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            finish()
        }

        // Certain lenses specify a camera facing that they would like to be applied on. CameraLayout provides a way
        // to use a lens specified preference to change the current camera facing by supplying a callback that is
        // invoked whenever a lens is applied:
        cameraLayout.onChooseFacingForLens { lens ->
            lens.facingPreference
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(BUNDLE_ARG_USE_CUSTOM_LENSES_CAROUSEL, useCustomLensesCarouselView)
        outState.putBoolean(BUNDLE_ARG_MUTE_AUDIO, muteAudio)
        outState.putBoolean(BUNDLE_ARG_ENABLE_DIAGNOSTICS, enableDiagnostics)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        closeOnDestroy.forEach { it.close() }
        super.onDestroy()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (cameraLayout.dispatchKeyEvent(event)) {
            true
        } else {
            super.dispatchKeyEvent(event)
        }
    }
}

private val Activity.arCoreSourceAvailable: Boolean get() {
    // Currently, ARCore is supported in portrait orientation only.
    return windowManager.defaultDisplay.rotation == Surface.ROTATION_0 && arCoreSupportedAndInstalled
}

/**
 * Mute lenses audio if [mute] is true. Unmute lenses audio otherwise.
 */
private fun Session.adjustLensesVolume(mute: Boolean) {
    val adjustVolume = if (mute) {
        LensesComponent.Audio.Adjustment.Volume.Mute
    } else {
        LensesComponent.Audio.Adjustment.Volume.UnMute
    }
    lenses.audio.adjust(adjustVolume) { success ->
        Log.d(TAG, "Adjust volume to $adjustVolume success: $success")
    }
}