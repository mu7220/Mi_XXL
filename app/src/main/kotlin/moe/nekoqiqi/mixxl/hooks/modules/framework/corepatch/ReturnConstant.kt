package moe.nekoqiqi.mixxl.hooks.modules.framework.corepatch

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences

class ReturnConstant(private val prefs: XSharedPreferences, private val prefsKey: String, private val value: Any?) : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
        super.beforeHookedMethod(param)
        prefs.reload()
        if (prefs.getBoolean(prefsKey, false)) {
            param.result = value
        }
    }

}
