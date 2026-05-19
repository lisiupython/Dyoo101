package o.dyoo.hook.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import o.dyoo.core.config.ModuleConfig
import o.dyoo.core.download.Downloader

/**
 * 图片下载 Hook
 *
 * 策略：
 * Hook ImageView.setImageURI(Uri) - Android 稳定 API
 * 抖音图片通过 Glide/自定义加载器传入 URI，Hook 捕获图片 URL
 */
object ImageHook {
    private const val TAG = "Dyoo.ImageHook"
    var lastImageUrl: String? = null

    /**
     * 安装 Hook
     * @param module XposedModule 实例
     * @param classLoader 目标应用的 ClassLoader
     */
    fun setup(module: XposedModule, classLoader: ClassLoader) {
        if (!ModuleConfig.isImageDownloadEnabled) return
        Log.i(TAG, "初始化图片下载 Hook")

        try {
            val method = ImageView::class.java.getDeclaredMethod("setImageURI", Uri::class.java)

            module.hook(method).intercept(XposedInterface.Hooker { chain ->
                val args = chain.getArgs()
                val uri = args[0] as? Uri
                uri?.toString()?.let { url ->
                    if (url.startsWith("http")) {
                        lastImageUrl = url
                        Log.d(TAG, "捕获图片URL: $url")
                    }
                }
                chain.proceed()
            })
            Log.i(TAG, "图片 Hook 成功")
        } catch (e: Throwable) {
            Log.e(TAG, "图片 Hook 失败: ${e.message}")
        }
    }

    fun saveCurrentImage(context: Context) {
        lastImageUrl?.let { Downloader.downloadImage(it, context) }
            ?: Toast.makeText(context, "未捕获到图片链接", Toast.LENGTH_SHORT).show()
    }
}
