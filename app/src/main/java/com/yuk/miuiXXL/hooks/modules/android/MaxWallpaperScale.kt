package com.yuk.miuiXXL.hooks.modules.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAllConstructorAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.yuk.miuiXXL.hooks.modules.BaseHook
import com.yuk.miuiXXL.utils.findClass
import com.yuk.miuiXXL.utils.getFloat

object MaxWallpaperScale : BaseHook() {
    override fun init() {

        findMethod("com.android.server.wm.WallpaperController") {
            name == "zoomOutToScale" && parameterTypes[0] == Float::class.java
        }.hookBefore {
            val value = getFloat("android_max_wallpaper_scale", 1.2f)
            it.thisObject.putObject("mMaxWallpaperScale", value)
        }
        "com.android.server.wm.WallpaperController".findClass().hookAllConstructorAfter {
            val value = getFloat("android_max_wallpaper_scale", 1.2f)
            it.thisObject.putObject("mMaxWallpaperScale", value)
        }
    }

}
