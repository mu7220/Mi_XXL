package moe.nekoqiqi.mixxl.hooks.modules.packageinstaller

import android.view.View
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callMethodOrNullAs
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClassOrNull
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object RemovePackageInstallerAds : BaseHook() {
    override fun init() {

        if (!getBoolean("packageinstaller_remove_ads", false)) return
        val miuiSettingsCompatClass = "com.android.packageinstaller.compat.MiuiSettingsCompat".findClassOrNull()
        val mSafeModeTipViewObjectClass = "com.miui.packageInstaller.ui.listcomponets.SafeModeTipViewObject".findClassOrNull()
        val mSafeModeTipViewObjectViewHolderClass = "com.miui.packageInstaller.ui.listcomponets.SafeModeTipViewObject\$ViewHolder".findClassOrNull()
        try {
            miuiSettingsCompatClass!!.methodFinder().filterByName("isPersonalizedAdEnabled").filterByReturnType(Boolean::class.java).toList().createHooks {
                returnConstant(false)
            }
        } catch (_: Throwable) {
        }
        try {
            mSafeModeTipViewObjectClass!!.methodFinder().filterByParamTypes(mSafeModeTipViewObjectViewHolderClass).toList().createHooks {
                after {
                    it.args[0].callMethodOrNullAs<View>("getClContentView")?.visibility = View.GONE
                }
            }
        } catch (_: Throwable) {
        }
    }

}