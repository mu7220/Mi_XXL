package moe.nekoqiqi.mixxl.hooks

import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.LogExtensions.logexIfThrow
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.hooks.modules.framework.MaxWallpaperScale
import moe.nekoqiqi.mixxl.hooks.modules.framework.RemoveScreenshotRestriction
import moe.nekoqiqi.mixxl.hooks.modules.framework.RemoveSmallWindowRestriction1
import moe.nekoqiqi.mixxl.hooks.modules.framework.corepatch.CorePatchMainHook
import moe.nekoqiqi.mixxl.hooks.modules.guardprovider.AntiDefraudAppManager
import moe.nekoqiqi.mixxl.hooks.modules.mediaeditor.RemoveCropRestriction
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.AlwaysShowStatusBarClock
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.AnimDurationRatio
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.BlurWhenOpenFolder
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.CategoryFeatures
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.DisableRecentViewWallpaperDarkening
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.DoubleTapToSleep
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.MinusOneOverlapMode
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.RecentViewRemoveCardAnim
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.RemoveSmallWindowRestriction2
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.ScrollIconName
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.SetDeviceLevel
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.ShortcutAddSmallWindow
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.ShowRealMemory
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.TaskViewCardSize
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.UnlockAnim
import moe.nekoqiqi.mixxl.hooks.modules.miuihome.UseCompleteBlur
import moe.nekoqiqi.mixxl.hooks.modules.packageinstaller.AllowUpdateSystemApp
import moe.nekoqiqi.mixxl.hooks.modules.packageinstaller.DisableCountCheck
import moe.nekoqiqi.mixxl.hooks.modules.packageinstaller.RemovePackageInstallerAds
import moe.nekoqiqi.mixxl.hooks.modules.packageinstaller.ShowMoreApkInfo
import moe.nekoqiqi.mixxl.hooks.modules.personalassistant.BlurWhenGotoMinusOne
import moe.nekoqiqi.mixxl.hooks.modules.powerkeeper.DisableDynamicRefreshRate
import moe.nekoqiqi.mixxl.hooks.modules.securitycenter.RemoveMacroBlacklist
import moe.nekoqiqi.mixxl.hooks.modules.securitycenter.ShowBatteryTemperature
import moe.nekoqiqi.mixxl.hooks.modules.securitycenter.SkipWarningWaitTime
import moe.nekoqiqi.mixxl.hooks.modules.settings.NotificationImportance
import moe.nekoqiqi.mixxl.hooks.modules.systemui.DisableBluetoothRestrict
import moe.nekoqiqi.mixxl.hooks.modules.systemui.LockScreenShowChargingInfo
import moe.nekoqiqi.mixxl.hooks.modules.systemui.LockScreenShowSeconds
import moe.nekoqiqi.mixxl.hooks.modules.systemui.RemoveLockScreenCamera
import moe.nekoqiqi.mixxl.hooks.modules.systemui.RemoveLockScreenMinus
import moe.nekoqiqi.mixxl.hooks.modules.systemui.RemoveSmallWindowRestriction3
import moe.nekoqiqi.mixxl.hooks.modules.systemui.ShowWifiStandard
import moe.nekoqiqi.mixxl.hooks.modules.systemui.StatusBarShowChargingInfo
import moe.nekoqiqi.mixxl.hooks.modules.systemui.StatusBarShowSeconds
import moe.nekoqiqi.mixxl.hooks.modules.systemui.UseNewHD
import moe.nekoqiqi.mixxl.hooks.modules.thememanager.RemoveThemeManagerAds
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

private const val TAG = "Miui XXL"
private val PACKAGE_NAME_HOOKED = setOf(
    "android",
    "com.android.settings",
    "com.android.systemui",
    "com.android.thememanager",
    "com.miui.gallery",
    "com.miui.guardprovider",
    "com.miui.home",
    "com.miui.mediaeditor",
    "com.miui.packageinstaller",
    "com.miui.personalassistant",
    "com.miui.powerkeeper",
    "com.miui.screenshot",
    "com.miui.securitycenter"
)

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelper.initZygote(startupParam)
        CorePatchMainHook().initZygote(startupParam)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName in PACKAGE_NAME_HOOKED) {
            // Init EzXHelper
            EzXHelper.initHandleLoadPackage(lpparam)
            EzXHelper.setLogTag(TAG)
            EzXHelper.setToastTag(TAG)
            // Init hooks
            when (lpparam.packageName) {
                "android" -> {
                    CorePatchMainHook().handleLoadPackage(lpparam)
                    initHooks(
                        MaxWallpaperScale,
                        RemoveSmallWindowRestriction1,
                        RemoveScreenshotRestriction,
                    )
                }

                "com.android.settings" -> {
                    initHooks(
                        NotificationImportance,
                    )
                }

                "com.android.systemui" -> {
                    initHooks(
                        StatusBarShowSeconds,
                        LockScreenShowChargingInfo,
                        RemoveLockScreenMinus,
                        RemoveLockScreenCamera,
                        DisableBluetoothRestrict,
                        RemoveSmallWindowRestriction3,
                        ShowWifiStandard,
                        LockScreenShowSeconds,
                        UseNewHD,
                        StatusBarShowChargingInfo,
                    )
                }

                "com.android.thememanager" -> {
                    initHooks(
                        RemoveThemeManagerAds,
                    )
                }

                "com.miui.gallery" -> {
                    initHooks(
                        RemoveCropRestriction,
                    )
                }

                "com.miui.guardprovider" -> {
                    AntiDefraudAppManager().handleLoadPackage(lpparam)
                }

                "com.miui.home" -> {
                    initHooks(
                        SetDeviceLevel,
                        DoubleTapToSleep,
                        ScrollIconName,
                        AnimDurationRatio,
                        UnlockAnim,
                        DisableRecentViewWallpaperDarkening,
                        RecentViewRemoveCardAnim,
                        CategoryFeatures,
                        ShortcutAddSmallWindow,
                        RemoveSmallWindowRestriction2,
                        BlurWhenOpenFolder,
                        AlwaysShowStatusBarClock,
                        TaskViewCardSize,
                        UseCompleteBlur,
                        MinusOneOverlapMode,
                        ShowRealMemory,
                    )
                }

                "com.miui.mediaeditor" -> {
                    initHooks(
                        RemoveCropRestriction,
                    )
                }

                "com.miui.packageinstaller" -> {
                    AllowUpdateSystemApp().handleLoadPackage(lpparam)
                    initHooks(
                        RemovePackageInstallerAds,
                        ShowMoreApkInfo,
                        DisableCountCheck,
                    )
                }

                "com.miui.personalassistant" -> {
                    initHooks(
                        BlurWhenGotoMinusOne,
                    )
                }

                "com.miui.powerkeeper" -> {
                    initHooks(
                        DisableDynamicRefreshRate,
                    )
                }

                "com.miui.screenshot" -> {
                    initHooks(
                        RemoveCropRestriction,
                    )
                }

                "com.miui.securitycenter" -> {
                    RemoveMacroBlacklist().handleLoadPackage(lpparam)
                    initHooks(
                        SkipWarningWaitTime,
                        ShowBatteryTemperature,
                    )
                }

                else -> return
            }
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed init hook: ${it.javaClass.simpleName}")
        }
    }

}
