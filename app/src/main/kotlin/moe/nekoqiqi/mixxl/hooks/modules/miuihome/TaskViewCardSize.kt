package moe.nekoqiqi.mixxl.hooks.modules.miuihome

import android.graphics.RectF
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callStaticMethod
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClass
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookAfterMethod
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getInt

object TaskViewCardSize : BaseHook() {
    override fun init() {

        if (!getBoolean("miuihome_task_view_card_size_binding", false)) return
        val value = getInt("miuihome_task_view_card_size_vertical", 100).toFloat() / 100f
        val value1 = getInt("miuihome_task_view_card_size_horizontal1", 100).toFloat() / 100f
        val value2 = getInt("miuihome_task_view_card_size_horizontal2", 100).toFloat() / 100f

        "com.miui.home.recents.views.TaskStackViewsAlgorithmVertical".hookAfterMethod(
            "scaleTaskView", RectF::class.java
        ) {
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod(
                "scaleRectAboutCenter", it.args[0], value
            )
        }

        "com.miui.home.recents.views.TaskStackViewsAlgorithmHorizontal".hookAfterMethod(
            "scaleTaskView", RectF::class.java,
        ) {
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod(
                "scaleRectAboutCenter", it.args[0], if (it.thisObject.callMethod("isLandscapeVisually") as Boolean) value2 else value1
            )
        }
    }

}
