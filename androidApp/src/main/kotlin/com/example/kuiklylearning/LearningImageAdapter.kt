package com.example.kuiklylearning

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Base64
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tencent.kuikly.core.render.android.KuiklyRenderViewContext
import com.tencent.kuikly.core.render.android.adapter.HRImageLoadOption
import com.tencent.kuikly.core.render.android.adapter.IKRImageAdapter

/** 图片加载接入；后面的 Image 课程会用到。第一课不用修改这里。 */
class LearningImageAdapter(context: Context) : IKRImageAdapter {
    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())

    // 2.27.0 的接口仍要求实现这个抽象方法，新重载默认会转调它。
    @Suppress("OVERRIDE_DEPRECATION")
    override fun fetchDrawable(
        imageLoadOption: HRImageLoadOption,
        callback: (Drawable?) -> Unit,
    ) {
        // 框架可能从工作线程请求图片；Glide 的 CustomTarget 在主线程使用。
        mainHandler.post {
            val source: Any = when {
                imageLoadOption.isAssets() -> "file:///android_asset/" +
                    imageLoadOption.src.removePrefix(HRImageLoadOption.SCHEME_ASSETS)
                imageLoadOption.isBase64() -> {
                    try {
                        Base64.decode(imageLoadOption.src.substringAfter(','), Base64.DEFAULT)
                    } catch (_: IllegalArgumentException) {
                        callback(null)
                        return@post
                    }
                }
                else -> imageLoadOption.src
            }
            Glide.with(appContext).asDrawable().load(source)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        callback(resource)
                    }
                    override fun onLoadFailed(errorDrawable: Drawable?) { callback(null) }
                    override fun onLoadCleared(placeholder: Drawable?) { callback(null) }
                })
        }
    }

    override fun getDrawableWidth(kuiklyRenderViewContext: KuiklyRenderViewContext, drawable: Drawable): Float =
        drawable.intrinsicWidth.toFloat()

    override fun getDrawableHeight(kuiklyRenderViewContext: KuiklyRenderViewContext, drawable: Drawable): Float =
        drawable.intrinsicHeight.toFloat()
}
