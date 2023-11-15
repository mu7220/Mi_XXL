package moe.nekoqiqi.mixxl.hooks.modules.powerkeeper

import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookBeforeMethod
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object DisableDynamicRefreshRate : BaseHook() {
    override fun init() {

        if (!getBoolean("powerkeeper_disable_dynamic_refresh_rate", false)) return
        "com.miui.powerkeeper.statemachine.DisplayFrameSetting".hookBeforeMethod("isFeatureOn") {
            it.result = false
        }
        "com.miui.powerkeeper.statemachine.DisplayFrameSetting".hookBeforeMethod(
            "setScreenEffect", String::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
        ) {
            it.result = null
        }
        "com.miui.powerkeeper.statemachine.DisplayFrameSetting".hookBeforeMethod(
            "setScreenEffectInternal", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java
        ) {
            it.result = null
        }
    }

}