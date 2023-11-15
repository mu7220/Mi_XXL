package moe.nekoqiqi.mixxl.hooks.modules.systemui

import android.view.View
import android.widget.LinearLayout
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.getObjectField
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object RemoveLockScreenCamera : BaseHook() {
    override fun init() {

        if (!getBoolean("systemui_lockscreen_remove_camera", false)) return
        try {
            loadClass("com.android.systemui.statusbar.phone.KeyguardBottomAreaView").methodFinder().filterByName("onFinishInflate").first().createHook {
                after {
                    (it.thisObject.getObjectField("mRightAffordanceViewLayout") as LinearLayout).visibility = View.GONE
                }
            }
        } catch (_: Exception) {
        }
        loadClass("com.android.keyguard.KeyguardMoveRightController").methodFinder().filterByName("onTouchMove").filterByParamCount(2).first().createHook {
            returnConstant(false)
        }
        loadClass("com.android.keyguard.KeyguardMoveRightController").methodFinder().filterByName("reset").first().createHook {
            returnConstant(null)
        }
    }

}
