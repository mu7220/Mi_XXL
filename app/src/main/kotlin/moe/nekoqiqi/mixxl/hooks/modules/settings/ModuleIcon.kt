package moe.nekoqiqi.mixxl.hooks.modules.settings

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.UserHandle
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.EzXHelper.addModuleAssetPath
import com.github.kyuubiran.ezxhelper.EzXHelper.moduleRes
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import de.robv.android.xposed.XposedHelpers
import moe.nekoqiqi.mixxl.BuildConfig
import moe.nekoqiqi.mixxl.R
import moe.nekoqiqi.mixxl.activity.MainActivity
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getInt
import moe.nekoqiqi.mixxl.utils.voyager.LazyClass.MiuiSettingsCls


object ModuleIcon : BaseHook() {
    override fun init() {
        if (getInt("settings_icon_mode", 0) == 0) return
        val preferenceActivityCls =
            loadClass("com.android.settingslib.miuisettings.preference.PreferenceActivity\$Header")

        MiuiSettingsCls.methodFinder()
            .filterByName("updateHeaderList")
            .first().createHook {
                after { param ->
                    val mIntent = Intent()
                    mIntent.putExtra("isDisplayHomeAsUpEnabled", true)
                    mIntent.setClassName(
                        BuildConfig.APPLICATION_ID,
                        MainActivity::class.java.canonicalName!!
                    )
                    val header = XposedHelpers.newInstance(preferenceActivityCls)
                    XposedHelpers.setLongField(header, "id", 666)
                    XposedHelpers.setObjectField(header, "intent", mIntent)
                    XposedHelpers.setObjectField(
                        header,
                        "title",
                        moduleRes.getString(R.string.app_name)
                    )
                    val mContext = param.thisObject as Activity
                    addModuleAssetPath(mContext)
                    XposedHelpers.setObjectField(header, "iconRes", R.drawable.settings_launcher)
                    val bundle = Bundle()
                    val users = ArrayList<UserHandle>()
                    users.add(XposedHelpers.newInstance(UserHandle::class.java, 0) as UserHandle)
                    bundle.putParcelableArrayList("header_user", users)
                    XposedHelpers.setObjectField(header, "extras", bundle)
                    val headers = param.args[0] as MutableList<Any>
                    var position = 0
                    for (head in headers) {
                        position++
                        val id = XposedHelpers.getLongField(head, "id")
                        if (id == -1L) {
                            headers.add(position - 1, header)
                            return@after
                        }
                    }
                    if (headers.size > 25) {
                        headers.add(25, header)
                    } else {
                        headers.add(header)
                    }
                }
            }
    }
}