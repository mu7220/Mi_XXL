package moe.nekoqiqi.mixxl.hooks.modules.systemui

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object RemoveSmallWindowRestriction3 : BaseHook() {
    override fun init() {

        if (!getBoolean("android_remove_small_window_restriction", false)) return
        loadClass("com.android.systemui.statusbar.notification.NotificationSettingsManager").methodFinder().filterByName("canSlide").first().createHook {
            returnConstant(true)
        }
    }

}
