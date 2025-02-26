package moe.nekoqiqi.mixxl.hooks.modules.miuihome

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callStaticMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClass
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookAfterAllMethods
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookAfterMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookBeforeAllMethods
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookBeforeMethod
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object BlurWhenOpenFolder : BaseHook() {
    override fun init() {

        if (!getBoolean("miuihome_blur_when_open_folder", false)) return
        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val blurUtilsClass = "com.miui.home.launcher.common.BlurUtils".findClass()
        val navStubViewClass = "com.miui.home.recents.NavStubView".findClass()
        val applicationClass = "com.miui.home.launcher.Application".findClass()

        try {
            // Replace it if official MiuiHome supports it
            launcherClass.hookBeforeMethod("isShouldBlur") {
                it.result = false
            }
            blurUtilsClass.hookBeforeMethod("fastBlurWhenOpenOrCloseFolder", launcherClass, Boolean::class.java) {
                it.result = null
            }
        } catch (_: Exception) {
            // Official MiuiHome doesn't support this feature
        }

        var isShouldBlur = false

        launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            val isInNormalEditing = mLauncher.callMethod("isInNormalEditing") as Boolean
            if (!isInNormalEditing) blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true)
        }

        launcherClass.hookAfterMethod("isFolderShowing") {
            isShouldBlur = it.result as Boolean
        }

        launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
            isShouldBlur = false
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            val isInNormalEditing = mLauncher.callMethod("isInNormalEditing") as Boolean
            if (isInNormalEditing) blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
            else blurUtilsClass.callStaticMethod("fastBlur", 0.0f, mLauncher.window, true)
        }

        launcherClass.hookBeforeMethod("onGesturePerformAppToHome") {
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            if (isShouldBlur) blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
        }

        blurUtilsClass.hookBeforeAllMethods("fastBlurWhenStartOpenOrCloseApp") {
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            val isInEditing = mLauncher.callMethod("isInEditing") as Boolean
            if (isShouldBlur) it.result = blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
            else if (isInEditing) it.result = blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
        }

        blurUtilsClass.hookBeforeAllMethods("fastBlurWhenFinishOpenOrCloseApp") {
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            val isInEditing = mLauncher.callMethod("isInEditing") as Boolean
            if (isShouldBlur) it.result = blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
            else if (isInEditing) it.result = blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
        }

        blurUtilsClass.hookAfterAllMethods("fastBlurWhenEnterRecents") {
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            val isInShortcutMenuState = mLauncher.callMethod("isInShortcutMenuState") as Boolean
            if (isInShortcutMenuState) mLauncher.callMethod("hideShortcutMenuWithoutAnim")
        }

        blurUtilsClass.hookAfterAllMethods("fastBlurWhenExitRecents") {
            val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
            val isInEditing = mLauncher.callMethod("isInEditing") as Boolean
            if (isShouldBlur) it.result = blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
            else if (isInEditing) it.result = blurUtilsClass.callStaticMethod("fastBlur", 1.0f, mLauncher.window, true, 0L)
        }

        blurUtilsClass.hookBeforeAllMethods("fastBlurDirectly") {
            val blurRatio = it.args[0] as Float
            if (isShouldBlur && blurRatio == 0.0f) it.result = null
        }

        if ((getBoolean("miuihome_use_complete_blur", false) && !getBoolean("miuihome_complete_blur_fix", false)) || !(getBoolean("miuihome_use_complete_blur", false))) {
            navStubViewClass.hookBeforeMethod("onPointerEvent", MotionEvent::class.java) {
                val mLauncher = applicationClass.callStaticMethod("getLauncher") as Activity
                val motionEvent = it.args[0] as MotionEvent
                val action = motionEvent.action
                if (action == 2 && isShouldBlur) blurUtilsClass.callStaticMethod("fastBlurDirectly", 1.0f, mLauncher.window)
            }
        }
    }

}
