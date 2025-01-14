package moe.nekoqiqi.mixxl.hooks.modules.miuihome

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClass
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.getObjectField
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookAfterMethod
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object ScrollIconName : BaseHook() {
    @SuppressLint("DiscouragedApi")
    override fun init() {

        if (!getBoolean("miuihome_scroll_icon_name", false)) return
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val shortcutInfoClass = "com.miui.home.launcher.ShortcutInfo".findClass()

        try {
            "com.miui.home.launcher.ItemIcon".hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            "com.miui.home.launcher.maml.MaMlWidgetView".hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
                mTitleScrolling(mTitle)
            }
            "com.miui.home.launcher.LauncherMtzGadgetView".hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
                mTitleScrolling(mTitle)
            }
            "com.miui.home.launcher.LauncherWidgetView".hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
                mTitleScrolling(mTitle)
            }
            "com.miui.home.launcher.ShortcutIcon".hookAfterMethod(
                "fromXml", Int::class.javaPrimitiveType, launcherClass, ViewGroup::class.java, shortcutInfoClass
            ) {
                val buddyIconView = it.args[3].callMethod("getBuddyIconView", it.args[2]) as View
                val mTitle = buddyIconView.getObjectField("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            "com.miui.home.launcher.ShortcutIcon".hookAfterMethod(
                "createShortcutIcon", Int::class.javaPrimitiveType, launcherClass, ViewGroup::class.java
            ) {
                val buddyIcon = it.result as View
                val mTitle = buddyIcon.getObjectField("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            "com.miui.home.launcher.common.Utilities".hookAfterMethod(
                "adaptTitleStyleToWallpaper", Context::class.java, TextView::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
            ) {
                val mTitle = it.args[1] as TextView
                if (mTitle.id == mTitle.resources.getIdentifier("icon_title", "id", "com.miui.home")) {
                    mTitleScrolling(mTitle)
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun mTitleScrolling(mTitle: TextView) {
        mTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        mTitle.isHorizontalFadingEdgeEnabled = true
        mTitle.setSingleLine()
        mTitle.marqueeRepeatLimit = -1
        mTitle.isSelected = true
        mTitle.setHorizontallyScrolling(true)
    }

}
