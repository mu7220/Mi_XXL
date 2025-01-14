package moe.nekoqiqi.mixxl.hooks.modules.miuihome

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClass
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookBeforeMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.replaceMethod
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object SetDeviceLevel : BaseHook() {
    override fun init() {

        if (!getBoolean("miuihome_highend_device", false)) return
        try {
            loadClass("com.miui.home.launcher.common.CpuLevelUtils").methodFinder().filterByName("getQualcommCpuLevel").filterByParamCount(1)
        } catch (_: Exception) {
            loadClass("miuix.animation.utils.DeviceUtils").methodFinder().filterByName("getQualcommCpuLevel").filterByParamCount(1)
        }.first().createHook {
            returnConstant(2)
        }
        try {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("getDeviceLevel") {
                it.result = 2
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isSupportCompleteAnimation") {
                it.result = true
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isLowLevelOrLiteDevice") {
                it.result = false
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isMiuiLiteVersion") {
                it.result = false
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.util.noword.NoWordSettingHelperKt".hookBeforeMethod("isNoWordAvailable") {
                it.result = true
            }
        } catch (_: Exception) {
        }
        try {
            "android.os.SystemProperties".hookBeforeMethod(
                "getBoolean", String::class.java, Boolean::class.java
            ) {
                if (it.args[0] == "ro.config.low_ram.threshold_gb") it.result = false
            }
        } catch (_: Exception) {
        }
        try {
            "android.os.SystemProperties".hookBeforeMethod(
                "getBoolean", String::class.java, Boolean::class.java
            ) {
                if (it.args[0] == "ro.miui.backdrop_sampling_enabled") it.result = true
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.common.Utilities".hookBeforeMethod("canLockTaskView") {
                it.result = true
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod("isMIUIWidgetSupport") {
                it.result = true
            }
        } catch (_: Exception) {
        }
        try {
            "com.miui.home.launcher.MiuiHomeLog".findClass().replaceMethod(
                "log", String::class.java, String::class.java
            ) {
                return@replaceMethod null
            }
        } catch (_: Exception) {
        }
        try {
            "com.xiaomi.onetrack.OneTrack".hookBeforeMethod("isDisable") {
                it.result = true
            }
        } catch (_: Exception) {

        }
        try {
            loadClass("com.miui.home.launcher.common.DeviceLevelUtils").methodFinder().filterByName("needMamlProgressIcon").first().createHook {
                returnConstant(true)
            }
        } catch (_: Exception) {

        }
        try {
            loadClass("com.miui.home.launcher.common.DeviceLevelUtils").methodFinder().filterByName("needRemoveDownloadAnimationDevice").first().createHook {
                returnConstant(false)
            }
        } catch (_: Exception) {

        }

        try {
            loadClass("com.miui.home.launcher.graphics.MonochromeUtils").methodFinder().filterByName("isSupportMonochrome").first().createHook {
                returnConstant(true)
            }
        } catch (_: Exception) {

        }
    }

}
