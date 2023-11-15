package moe.nekoqiqi.mixxl.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import kotlin.system.exitProcess
import moe.nekoqiqi.mixxl.R
import moe.nekoqiqi.mixxl.activity.pages.AndroidPage
import moe.nekoqiqi.mixxl.activity.pages.MainPage
import moe.nekoqiqi.mixxl.activity.pages.MediaEditorPage
import moe.nekoqiqi.mixxl.activity.pages.MiuiHomePage
import moe.nekoqiqi.mixxl.activity.pages.PackageInstallerPage
import moe.nekoqiqi.mixxl.activity.pages.PersonalAssistantPage
import moe.nekoqiqi.mixxl.activity.pages.PowerKeeperPage
import moe.nekoqiqi.mixxl.activity.pages.SecurityCenterPage
import moe.nekoqiqi.mixxl.activity.pages.SettingsPage
import moe.nekoqiqi.mixxl.activity.pages.SystemUIPage
import moe.nekoqiqi.mixxl.activity.pages.ThemeManagerPage
import moe.nekoqiqi.mixxl.utils.BackupUtils

class MainActivity : MIUIActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        checkLSPosed()
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("Mi_xxl_Config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this) {
                setTitle(R.string.tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    init {
        activity = this
        registerPage(MainPage::class.java)
        registerPage(AndroidPage::class.java)
        registerPage(MiuiHomePage::class.java)
        registerPage(PowerKeeperPage::class.java)
        registerPage(SecurityCenterPage::class.java)
        registerPage(SystemUIPage::class.java)
        registerPage(ThemeManagerPage::class.java)
        registerPage(SettingsPage::class.java)
        registerPage(MediaEditorPage::class.java)
        registerPage(PersonalAssistantPage::class.java)
        registerPage(PackageInstallerPage::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && resultCode == RESULT_OK) {
            when (requestCode) {
                BackupUtils.CREATE_DOCUMENT_CODE -> {
                    BackupUtils.handleCreateDocument(activity, data.data)
                }

                BackupUtils.OPEN_DOCUMENT_CODE -> {
                    BackupUtils.handleReadDocument(activity, data.data)
                }

            }
        }
    }

}
