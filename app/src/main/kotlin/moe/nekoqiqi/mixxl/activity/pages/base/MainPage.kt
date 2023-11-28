package moe.nekoqiqi.mixxl.activity.pages.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import moe.nekoqiqi.mixxl.R
import moe.nekoqiqi.mixxl.utils.AppUtils.exec
import moe.nekoqiqi.mixxl.utils.AppUtils.perfFileName
import moe.nekoqiqi.mixxl.utils.BackupUtils

@BMMainPage("Mi XXL")
class MainPage : BasePage() {
    @SuppressLint("WorldReadableFiles")
    override fun onCreate() {
        Page(this.getDrawable(R.drawable.ic_android), TextSummaryV(textId = R.string.android, tipsId = R.string.android_reboot), round = 8f, onClickListener = { showFragment("AndroidPage") })
        Page(this.getDrawable(R.drawable.ic_systemui), TextSummaryV(textId = R.string.systemui), round = 8f, onClickListener = { showFragment("SystemUIPage") })
        Page(this.getDrawable(R.drawable.ic_settings), TextSummaryV(textId = R.string.settings), round = 8f, onClickListener = { showFragment("SystemSettingsPage") })
        Line()
        Page(this.getDrawable(R.drawable.ic_miuihome), TextSummaryV(textId = R.string.miuihome), round = 8f, onClickListener = { showFragment("MiuiHomePage") })
        Page(this.getDrawable(R.drawable.ic_personalassistant), TextSummaryV(textId = R.string.personalassistant), round = 8f, onClickListener = { showFragment("PersonalAssistantPage") })
        Page(this.getDrawable(R.drawable.ic_securitycenter), TextSummaryV(textId = R.string.securitycenter), round = 8f, onClickListener = { showFragment("SecurityCenterPage") })
        Page(this.getDrawable(R.drawable.ic_thememanager), TextSummaryV(textId = R.string.thememanager), round = 8f, onClickListener = { showFragment("ThemeManagerPage") })
        Page(this.getDrawable(R.drawable.ic_mediaeditor), TextSummaryV(textId = R.string.mediaeditor), round = 8f, onClickListener = { showFragment("MediaEditorPage") })
        Page(this.getDrawable(R.drawable.ic_powerkeeper), TextSummaryV(textId = R.string.powerkeeper), round = 8f, onClickListener = { showFragment("PowerKeeperPage") })
        Page(this.getDrawable(R.drawable.ic_packageinstaller), TextSummaryV(textId = R.string.packageinstaller), round = 8f, onClickListener = { showFragment("PackageInstallerPage") })
        Line()
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.settings_page,
                onClickListener = { showFragment("SettingsPage") })
        )
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.about_page,
                onClickListener = { showFragment("AboutPage") })
        )
    }

}
