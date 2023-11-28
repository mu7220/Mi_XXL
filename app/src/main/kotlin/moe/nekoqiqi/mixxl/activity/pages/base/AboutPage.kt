package moe.nekoqiqi.mixxl.activity.pages.base

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri.parse
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.TextSummaryV
import moe.nekoqiqi.mixxl.BuildConfig.BUILD_TIME
import moe.nekoqiqi.mixxl.BuildConfig.BUILD_TYPE
import moe.nekoqiqi.mixxl.BuildConfig.VERSION_NAME
import moe.nekoqiqi.mixxl.R
import java.text.SimpleDateFormat
import java.util.Locale

@BMPage("AboutPage", hideMenu = false)
class AboutPage : BasePage() {
    override fun getTitle(): String {
        setTitle(getString(R.string.about_page))
        return getString(R.string.about_page)
    }

    override fun onCreate() {
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.app_name,
                tips = "构建时间：${
                    SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ).format(BUILD_TIME.toLong())
                }\n模块版本：${VERSION_NAME} (${BUILD_TYPE})",
                onClickListener = {
                    activity.startActivity(
                        Intent(
                            ACTION_VIEW,
                            parse("https://github.com/mu7220/Mi_XXL")
                        )
                    )
                }
            )
        )
        Line()
        TitleText(textId = R.string.developer_title)
        ImageWithText(
            authorHead = getDrawable(R.drawable.ic_lingqiqi),
            authorName = "Neko Qiqi",
            authorTips = getString(R.string.developer_desc),
            onClickListener = {
                activity.startActivity(
                    Intent(
                        ACTION_VIEW,
                        parse("https://github.com/mu7220")
                    )
                )
            })
    }
}