package moe.nekoqiqi.mixxl.hooks.modules.guardprovider

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import moe.nekoqiqi.mixxl.utils.helper.DexKit.dexKitBridge
import org.luckypray.dexkit.DexKitBridge

class AntiDefraudAppManager : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        dexKitBridge.findMethod {
            matcher {
                usingStrings = listOf("AntiDefraudAppManager", "https://flash.sec.miui.com/detect/app")
            }
        }.firstOrNull()?.getMethodInstance(lpparam.classLoader)?.createHook {
            replace {
                return@replace null
            }
        }
    }

}
