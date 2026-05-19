package o.dyoo.core.download

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import android.util.Log
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import o.dyoo.core.config.ModuleConfig
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

/**
 * 下载器
 * 负责视频和图片的下载
 */
object Downloader {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    /**
     * 下载视频
     */
    fun downloadVideo(url: String, context: Context) {
        scope.launch {
            try {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "开始下载视频...", Toast.LENGTH_SHORT).show()
                }

                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "com.ss.android.ugc.aweme/310200 (Linux; U; Android 14)")
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw Exception("HTTP ${response.code}")
                }

                val fileName = "Dyoo_Video_${System.currentTimeMillis()}.mp4"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ 使用 MediaStore
                    saveToMediaStore(context, fileName, "video/mp4", response.body!!.byteStream())
                } else {
                    // Android 9 及以下
                    val dir = if (ModuleConfig.savePath.isNotEmpty()) {
                        File(ModuleConfig.savePath)
                    } else {
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                    }
                    dir.mkdirs()
                    val file = File(dir, fileName)
                    FileOutputStream(file).use { out ->
                        response.body!!.byteStream().copyTo(out)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "下载成功: $fileName", Toast.LENGTH_SHORT).show()
                }

                // WebDav 自动上传
                if (ModuleConfig.isWebDavEnabled) {
                    // 上传逻辑
                }

            } catch (e: Throwable) {
                Log.e("Dyoo", "Dyoo download video failed: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "下载失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 下载图片
     */
    fun downloadImage(url: String, context: Context) {
        scope.launch {
            try {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "正在保存图片...", Toast.LENGTH_SHORT).show()
                }

                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "com.ss.android.ugc.aweme/310200")
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw Exception("HTTP ${response.code}")
                }

                val ext = when {
                    url.contains(".webp") -> "webp"
                    url.contains(".png") -> "png"
                    else -> "jpg"
                }
                val fileName = "Dyoo_Image_${System.currentTimeMillis()}.$ext"
                val mimeType = "image/${ext}"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveToMediaStore(context, fileName, mimeType, response.body!!.byteStream())
                } else {
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    dir.mkdirs()
                    val file = File(dir, fileName)
                    FileOutputStream(file).use { out ->
                        response.body!!.byteStream().copyTo(out)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "图片保存成功: $fileName", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Throwable) {
                Log.e("Dyoo", "Dyoo download image failed: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "保存失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Android 10+ MediaStore 保存
     */
    private fun saveToMediaStore(context: Context, fileName: String, mimeType: String, input: java.io.InputStream) {
        val collection = if (mimeType.startsWith("video")) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val uri = context.contentResolver.insert(collection, values) ?: throw Exception("Failed to create MediaStore entry")

        context.contentResolver.openOutputStream(uri)?.use { out ->
            input.copyTo(out)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri, values, null, null)
        }

        Log.i("Dyoo", "Dyoo: Saved to MediaStore: $fileName")
    }
}
