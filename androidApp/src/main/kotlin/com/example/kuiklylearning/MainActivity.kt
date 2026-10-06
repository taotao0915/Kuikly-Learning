package com.example.kuiklylearning

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.tencent.kuikly.core.render.android.css.ktx.toMap
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegator
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegatorDelegate
import org.json.JSONObject

/** Android 宿主：提供一个容器，把 shared 中的 Kuikly 页面显示出来。 */
class MainActivity : AppCompatActivity() {
    private val pageDelegator = KuiklyRenderViewBaseDelegator(
        object : KuiklyRenderViewBaseDelegatorDelegate {}
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 外层处理状态栏、导航栏和键盘，内层留给 Kuikly 渲染页面。
        val root = FrameLayout(this)
        val container = FrameLayout(this)
        root.addView(container, FrameLayout.LayoutParams(-1, -1))
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val safeArea = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime()
            )
            view.setPadding(safeArea.left, safeArea.top, safeArea.right, safeArea.bottom)
            insets
        }
        setContentView(root)
        ViewCompat.requestApplyInsets(root)

        // "HelloWorld" 与 HelloWorldPage 的 @Page("HelloWorld") 一一对应。
        // 默认使用 JVM 模式，页面随 APK 编译，不需要额外启动服务。
        val pageName = intent.getStringExtra("pageName") ?: "HelloWorld"
        val pageData = intent.getStringExtra("pageData")?.let { JSONObject(it).toMap() } ?: emptyMap()
        pageDelegator.onAttach(container, "", pageName, pageData)
    }

    override fun onResume() {
        super.onResume()
        pageDelegator.onResume()
    }

    override fun onPause() {
        pageDelegator.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        pageDelegator.onDetach()
        super.onDestroy()
    }
}
