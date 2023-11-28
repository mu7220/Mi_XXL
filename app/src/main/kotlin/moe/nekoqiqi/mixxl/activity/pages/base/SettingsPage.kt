package moe.nekoqiqi.mixxl.activity.pages.base

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.MIUIActivity.Companion.safeSP
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import moe.nekoqiqi.mixxl.R
import moe.nekoqiqi.mixxl.activity.MainActivity
import moe.nekoqiqi.mixxl.utils.AppUtils
import moe.nekoqiqi.mixxl.utils.BackupUtils


@BMPage("SettingsPage", hideMenu = false)
class SettingsPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(R.string.settings_page))
        return getString(R.string.settings_page)
    }

    override fun onCreate() {
        // 应用图标
        TitleText(textId = R.string.icon_title)
        TextWithSwitch(TextV(textId = R.string.hide_desktop_icon), SwitchV("hide_desktop_icon", onClickListener = {
            val pm = MIUIActivity.activity.packageManager
            val mComponentEnabledState: Int = if (it) {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            }
            pm.setComponentEnabledSetting(ComponentName(MIUIActivity.activity, MainActivity::class.java.name + "Alias"), mComponentEnabledState, PackageManager.DONT_KILL_APP)
        }))
        val iconMode: HashMap<Int, String> = hashMapOf<Int, String>().also {
            it[0] = getString(R.string.array_close)
            it[1] = getString(R.string.array_top)
            it[2] = getString(R.string.array_bottom)
        }
        TextWithSpinner(
            TextV(textId = R.string.icon_show),
            SpinnerV(
                iconMode[safeSP.getInt(
                    "settings_icon_mode",
                    0
                )].toString()
            ) {
                add(iconMode[0].toString()) {
                    safeSP.putAny("settings_icon_mode", 0)
                }
                add(iconMode[1].toString()) {
                    safeSP.putAny("settings_icon_mode", 1)
                }
                /*add(iconMode[2].toString()) {
                    safeSP.putAny("settings_icon_mode", 2)
                }*/
            }
        )
        Line()
        // 模块备份
        TitleText(textId = R.string.backup_title)
        TextWithArrow(TextV(textId = R.string.backup, onClickListener = {
            BackupUtils.backup(activity, activity.createDeviceProtectedStorageContext().getSharedPreferences(
                AppUtils.perfFileName(), Context.MODE_WORLD_READABLE))
        }))
        TextWithArrow(TextV(textId = R.string.recovery, onClickListener = {
            BackupUtils.recovery(activity, activity.createDeviceProtectedStorageContext().getSharedPreferences(
                AppUtils.perfFileName(), Context.MODE_WORLD_READABLE))
        }))
        TextWithArrow(TextV(textId = R.string.reset_module) {
            MIUIDialog(activity) {
                setTitle(R.string.tips)
                setMessage(R.string.reset_module_summary)
                setLButton(R.string.cancel) {
                    dismiss()
                }
                setRButton(R.string.done) {
                    activity.getSharedPreferences(AppUtils.perfFileName(), Activity.MODE_WORLD_READABLE).edit().clear().apply()
                    Toast.makeText(activity, activity.getString(R.string.reset_module_finished), Toast.LENGTH_LONG).show()
                }
            }.show()
        })
        Line()
        // 模块作用域
        TitleText(textId = R.string.restart_title)
        TextWithArrow(TextV(textId = R.string.restart_all_scope) {
            MIUIDialog(activity) {
                setTitle(R.string.tips)
                setMessage(R.string.restart_scope_summary)
                setLButton(R.string.cancel) {
                    dismiss()
                }
                setRButton(R.string.done) {
                    val command = arrayOf(
                        "am force-stop com.android.thememanager",
                        "am force-stop com.android.settings",
                        "am force-stop com.miui.gallery",
                        "am force-stop com.miui.guardprovider",
                        "am force-stop com.miui.home",
                        "am force-stop com.miui.mediaeditor",
                        "am force-stop com.miui.packageinstaller",
                        "am force-stop com.miui.personalassistant",
                        "am force-stop com.miui.powerkeeper",
                        "am force-stop com.miui.screenshot",
                        "am force-stop com.miui.securitycenter",
                        "killall com.android.systemui"
                    )
                    AppUtils.exec(command)
                    Toast.makeText(activity, getString(R.string.restart_scope_finished), Toast.LENGTH_SHORT).show()
                    Thread.sleep(500)
                    dismiss()
                }
            }.show()
        })
        TextWithArrow(TextV(textId = R.string.reboot_system) {
            MIUIDialog(activity) {
                setTitle(R.string.tips)
                setMessage(R.string.reboot_system_summary)
                setLButton(R.string.cancel) {
                    dismiss()
                }
                setRButton(R.string.done) {
                    AppUtils.exec("reboot")
                }
            }.show()
        })
    }
}