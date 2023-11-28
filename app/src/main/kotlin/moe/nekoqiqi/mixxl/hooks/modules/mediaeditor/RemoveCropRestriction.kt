package moe.nekoqiqi.mixxl.hooks.modules.mediaeditor

import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import de.robv.android.xposed.XposedBridge
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean
import moe.nekoqiqi.mixxl.utils.helper.DexKit.dexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Modifier

object RemoveCropRestriction : BaseHook() {
    private val screenshot by lazy {
        dexKitBridge.findMethod {
            matcher {
                usingNumbers(0.5f, 200)
                modifiers = Modifier.PRIVATE
                returnType = "int"
                paramCount = 0
            }
        }.map { it.getMethodInstance(EzXHelper.safeClassLoader) }.first()
    }
    private val mScreenCropViewMethodToNew by lazy {
        dexKitBridge.findMethod {
            matcher {
                declaredClass {
                    addUsingString("not in bound", StringMatchType.Equals)
                }
                usingNumbers(0.5f, 200)
                returnType = "int"
                modifiers = Modifier.FINAL
            }
        }.map { it.getMethodInstance(EzXHelper.safeClassLoader) }.toList()
    }

    private val mScreenCropViewMethodToOld by lazy {
        dexKitBridge.findMethod {
            matcher {
                usingNumbers(0.5f, 200)
                returnType = "int"
                modifiers = Modifier.FINAL
                paramCount = 0
            }
        }.firstOrNull()?.getMethodInstance(EzXHelper.safeClassLoader)
    }

    override fun init() {
        if (!getBoolean("mediaeditor_remove_crop_restriction", false)) return

        when (EzXHelper.hostPackageName) {
            "com.miui.mediaeditor" -> {
                mScreenCropViewMethodToNew.createHooks {
                    returnConstant(0)
                }
                if (mScreenCropViewMethodToOld != null) {
                    mScreenCropViewMethodToOld!!.createHook {
                        returnConstant(0)
                    }
                }
            }
            "com.miui.screenshot" -> screenshot.createHook { returnConstant(0) }
        }
    }
}
