package com.example.kuiklylearning

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tencent.kuikly.core.render.android.adapter.IKRLogAdapter
import com.tencent.kuikly.core.render.android.adapter.IKRRouterAdapter
import com.tencent.kuikly.core.render.android.adapter.IKRThreadAdapter
import com.tencent.kuikly.core.render.android.adapter.IKRUncaughtExceptionHandlerAdapter
import com.tencent.kuikly.core.render.android.adapter.KuiklyRenderAdapterManager
import org.json.JSONObject
import java.util.concurrent.Executors

/** 框架接入层。第一课只需知道它负责准备日志、线程等 Android 能力。 */
class LearningApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val workerPool = Executors.newFixedThreadPool(2)
        with(KuiklyRenderAdapterManager) {
            krImageAdapter = LearningImageAdapter(this@LearningApplication)
            krLogAdapter = object : IKRLogAdapter {
                override val asyncLogEnable = false
                override fun i(tag: String, msg: String) { Log.i(tag, msg) }
                override fun d(tag: String, msg: String) { Log.d(tag, msg) }
                override fun e(tag: String, msg: String) { Log.e(tag, msg) }
            }
            krThreadAdapter = object : IKRThreadAdapter {
                override fun executeOnSubThread(task: () -> Unit) {
                    workerPool.execute(task)
                }
            }
            krUncaughtExceptionHandlerAdapter = object : IKRUncaughtExceptionHandlerAdapter {
                override fun uncaughtException(throwable: Throwable) {
                    // 学习阶段让异常直接暴露，便于从 Logcat 定位错误。
                    Log.e("KuiklyLearning", "Kuikly page failed", throwable)
                    throw throwable
                }
            }
            krRouterAdapter = object : IKRRouterAdapter {
                override fun openPage(context: Context, pageName: String, pageData: JSONObject) {
                    val intent = Intent(context, MainActivity::class.java)
                        .putExtra("pageName", pageName)
                        .putExtra("pageData", pageData.toString())
                    if (context !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                override fun closePage(context: Context) {
                    (context as? Activity)?.finish()
                }
            }
        }
    }
}
