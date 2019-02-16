import android.app.Activity
import android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
import android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
import androidx.annotation.FloatRange


fun Activity.requestFullBrightness() = requestBrightness(BRIGHTNESS_OVERRIDE_FULL)
fun Activity.requestNoneBrightness() = requestBrightness(BRIGHTNESS_OVERRIDE_NONE)
fun Activity.requestBrightness(@FloatRange(from = -1.0, to = 1.0) brightness: Float) = window.run {
    val attrs = attributes.apply {
        screenBrightness = brightness
    }
    attributes = attrs
}