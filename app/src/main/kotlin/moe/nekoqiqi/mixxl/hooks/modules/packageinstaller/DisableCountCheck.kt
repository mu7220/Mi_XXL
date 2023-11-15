package moe.nekoqiqi.mixxl.hooks.modules.packageinstaller

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClass
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object DisableCountCheck : BaseHook() {
    override fun init() {

        if (!getBoolean("packageinstaller_disable_count_check", false)) return
        val riskControlRulesClass = "com.miui.packageInstaller.model.RiskControlRules".findClass()

        try {
            riskControlRulesClass.methodFinder().filterByName("getCurrentLevel").first().createHook {
                returnConstant(0)
            }
        } catch (t: Throwable) {
            Log.ex(t)
        }

    }

}
