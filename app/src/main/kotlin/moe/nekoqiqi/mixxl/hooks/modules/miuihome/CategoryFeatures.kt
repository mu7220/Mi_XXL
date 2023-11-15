package moe.nekoqiqi.mixxl.hooks.modules.miuihome

import android.view.View
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClassOrNull
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import moe.nekoqiqi.mixxl.hooks.modules.BaseHook
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.callMethodAs
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.findClass
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.getObjectFieldAs
import moe.nekoqiqi.mixxl.utils.KotlinXposedHelper.hookAfterMethod
import moe.nekoqiqi.mixxl.utils.XSharedPreferences.getBoolean

object CategoryFeatures : BaseHook() {
    override fun init() {

        if (getBoolean("miuihome_hide_allapps_category_all", false)) {
            val baseAllAppsCategoryListContainerClass = loadClassOrNull("com.miui.home.launcher.allapps.category.BaseAllAppsCategoryListContainer")
            val allAppsCategoryListContainer = loadClass("com.miui.home.launcher.allapps.category.AllAppsCategoryListContainer")
            try {
                baseAllAppsCategoryListContainerClass!!.methodFinder().filterByName("buildSortCategoryList").first()
            } catch (e: Exception) {
                allAppsCategoryListContainer.methodFinder().filterByName("buildSortCategoryList").first()
            }.createHook {
                after {
                    val list = it.result as ArrayList<*>
                    if (list.size > 1) {
                        list.removeAt(0)
                        it.result = list
                    }
                }
            }
        }

        if (getBoolean("miuihome_hide_allapps_category_paging_edit", false)) {
            "com.miui.home.launcher.allapps.AllAppsGridAdapter".hookAfterMethod(
                "onBindViewHolder", "com.miui.home.launcher.allapps.AllAppsGridAdapter.ViewHolder".findClass(), Int::class.javaPrimitiveType
            ) {
                if (it.args[0].callMethodAs<Int>("getItemViewType") == 64) {
                    it.args[0].getObjectFieldAs<View>("itemView").visibility = View.INVISIBLE
                }
            }
        }
    }

}
