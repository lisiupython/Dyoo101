package o.dyoo.hook.impl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import o.dyoo.core.config.ModuleConfig
import o.dyoo.core.download.Downloader

/**
 * 视频下载 Hook
 * 
 * 策略：
 * 1. Hook DownloadManager.enqueue() 捕获视频下载请求的 URL (稳定 API)
 * 2. Hook okhttp3.Response 捕获包含视频播放地址的响应
 */
object VideoHook {
    private const val TAG = "Dyoo.VideoHook"
    var lastVideoUrl: String? = null

    /**
     * 安装 Hook
     * @param module XposedModule 实例
     * @param classLoader 目标应用的 ClassLoader
     */
    fun setup(module: XposedModule, classLoader: ClassLoader) {
        if (!ModuleConfig.isVideoDownloadEnabled) return
        Log.i(TAG, "初始化视频下载 Hook")
        hookDownloadManager(module)
        hookOkHttpForVideo(module, classLoader)
    }

    /**
     * 策略1: Hook DownloadManager - 系统级 API，完全稳定
     */
    private fun hookDownloadManager(module: XposedModule) {
        try {
            val method = android.app.DownloadManager::class.java.getDeclaredMethod(
                "enqueue",
                android.app.DownloadManager.Request::class.java
            )

            module.hook(method).intercept(XposedInterface.Hooker { chain ->
                try {
                    val args = chain.getArgs()
                    val request = args[0] ?: return@Hooker chain.proceed()
                    val uriField = request.javaClass.getDeclaredField("mUri")
                    uriField.isAccessible = true
                    val uri = uriField.get(request) as? String
                    if (!uri.isNullOrEmpty() && uri.contains("douyin")) {
                        lastVideoUrl = uri
                        Log.d(TAG, "捕获视频URL: $uri")
                    }
                } catch (_: Throwable) {}
                chain.proceed()
            })
            Log.i(TAG, "DownloadManager hook 成功")
        } catch (e: Throwable) {
            Log.e(TAG, "Hook DownloadManager 失败: ${e.message}")
        }
    }

    /**
     * 策略2: Hook OkHttp 拦截视频响应 - 网络层 API，稳定
     */
    private fun hookOkHttpForVideo(module: XposedModule, classLoader: ClassLoader) {
        try {
            val realCallClass = classLoader.loadClass("okhttp3.internal.connection.RealCall")
            val executeMethod = realCallClass.getDeclaredMethod("execute")

            module.hook(executeMethod).intercept(XposedInterface.Hooker { chain ->
                val result = chain.proceed()
                try {
                    val response = result
                    val requestField = response?.javaClass?.getDeclaredMethod("request")
                    val request = requestField?.invoke(response)
                    val urlMethod = request?.javaClass?.getMethod("url", String::class.java)
                    val url = urlMethod?.invoke(request) as? String
                    if (url != null && (url.contains(".mp4") || url.contains("video"))) {
                        lastVideoUrl = url
                        Log.d(TAG, "OkHttp捕获视频URL: $url")
                    }
                } catch (_: Throwable) {}
                result
            })
            Log.i(TAG, "OkHttp hook 成功")
        } catch (e: Throwable) {
            Log.w(TAG, "OkHttp hook 失败 (非致命): ${e.message}")
        }
    }

    fun downloadCurrentVideo(context: Context) {
        lastVideoUrl?.let { Downloader.downloadVideo(it, context) }
            ?: Toast.makeText(context, "未捕获到视频链接", Toast.LENGTH_SHORT).show()
    }

    fun copyCurrentLink(context: Context) {
        lastVideoUrl?.let {
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("Dyoo", it))
            Toast.makeText(context, "链接已复制", Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(context, "未捕获到视频链接", Toast.LENGTH_SHORT).show()
    }
}
