package o.dyoo.core.download

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import o.dyoo.core.config.ModuleConfig
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * WebDav 客户端
 * 使用 OkHttp 实现 WebDav PUT 上传
 */
object WebDavClient {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    fun upload(file: File, callback: ((Boolean, String) -> Unit)? = null) {
        if (!ModuleConfig.isWebDavEnabled) {
            callback?.invoke(false, "WebDav 未启用")
            return
        }

        val serverUrl = ModuleConfig.webDavUrl
        val username = ModuleConfig.webDavUsername
        val password = ModuleConfig.webDavPassword

        if (serverUrl.isBlank() || username.isBlank()) {
            callback?.invoke(false, "WebDav 配置不完整")
            return
        }

        scope.launch {
            try {
                val baseUrl = if (serverUrl.endsWith("/")) serverUrl else "$serverUrl/"
                val remotePath = "${baseUrl}Dyoo/${file.name}"

                // 先创建目录（MKCOL）
                val dirUrl = "${baseUrl}Dyoo/"
                val mkcolReq = Request.Builder()
                    .url(dirUrl)
                    .method("MKCOL", null)
                    .header("Authorization", Credentials.basic(username, password))
                    .build()
                try { client.newCall(mkcolReq).execute().close() } catch (_: Throwable) {}

                // 上传文件（PUT）
                val mimeType = guessContentType(file.name)
                val putReq = Request.Builder()
                    .url(remotePath)
                    .put(file.asRequestBody(mimeType.toMediaType()))
                    .header("Authorization", Credentials.basic(username, password))
                    .build()

                val response = client.newCall(putReq).execute()
                val success = response.isSuccessful
                response.close()

                if (success) {
                    Log.i("Dyoo", "Dyoo WebDav: Uploaded ${file.name}")
                    withContext(Dispatchers.Main) { callback?.invoke(true, "上传成功") }
                } else {
                    throw Exception("HTTP ${response.code}")
                }

            } catch (e: Throwable) {
                Log.e("Dyoo", "Dyoo WebDav: Upload failed: ${e.message}")
                withContext(Dispatchers.Main) { callback?.invoke(false, "上传失败: ${e.message}") }
            }
        }
    }

    fun test(url: String, username: String, password: String, callback: (Boolean, String) -> Unit) {
        scope.launch {
            try {
                val req = Request.Builder()
                    .url(if (url.endsWith("/")) url else "$url/")
                    .method("PROPFIND", null)
                    .header("Authorization", Credentials.basic(username, password))
                    .header("Depth", "0")
                    .build()

                val response = client.newCall(req).execute()
                val success = response.isSuccessful
                response.close()

                withContext(Dispatchers.Main) {
                    callback(success, if (success) "连接成功" else "HTTP ${response.code}")
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) { callback(false, "连接失败: ${e.message}") }
            }
        }
    }

    private fun guessContentType(fileName: String): String = when {
        fileName.endsWith(".mp4", true) -> "video/mp4"
        fileName.endsWith(".jpg", true) || fileName.endsWith(".jpeg", true) -> "image/jpeg"
        fileName.endsWith(".png", true) -> "image/png"
        fileName.endsWith(".webp", true) -> "image/webp"
        else -> "application/octet-stream"
    }
}
