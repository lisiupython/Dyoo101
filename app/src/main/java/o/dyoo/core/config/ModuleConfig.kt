package o.dyoo.core.config

import android.content.Context
import android.content.SharedPreferences

/**
 * 模块配置
 * 管理所有设置项
 */
object ModuleConfig {

    private const val PREF_NAME = "dyoo_prefs"
    private var _prefs: SharedPreferences? = null

    private val prefs: SharedPreferences
        get() {
            if (_prefs == null) {
                try {
                    val context = Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication")
                        .invoke(null) as Context
                    _prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                } catch (_: Throwable) {
                    // Fallback: 使用反射获取 ActivityThread
                    try {
                        val activityThread = Class.forName("android.app.ActivityThread")
                            .getMethod("currentActivityThread")
                            .invoke(null)
                        val app = activityThread.javaClass
                            .getMethod("getApplication")
                            .invoke(activityThread) as Context
                        _prefs = app.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    } catch (_: Throwable) {
                        throw IllegalStateException("Cannot access SharedPreferences in hook context")
                    }
                }
            }
            return _prefs!!
        }

    // 功能开关
    var isVideoDownloadEnabled: Boolean
        get() = prefs.getBoolean("video_download", true)
        set(v) = prefs.edit().putBoolean("video_download", v).apply()

    var isImageDownloadEnabled: Boolean
        get() = prefs.getBoolean("image_download", true)
        set(v) = prefs.edit().putBoolean("image_download", v).apply()

    var isWatermarkRemoveEnabled: Boolean
        get() = prefs.getBoolean("watermark_remove", true)
        set(v) = prefs.edit().putBoolean("watermark_remove", v).apply()

    var isWebDavEnabled: Boolean
        get() = prefs.getBoolean("webdav_enabled", false)
        set(v) = prefs.edit().putBoolean("webdav_enabled", v).apply()

    // WebDav
    var webDavUrl: String
        get() = prefs.getString("webdav_url", "") ?: ""
        set(v) = prefs.edit().putString("webdav_url", v).apply()

    var webDavUsername: String
        get() = prefs.getString("webdav_username", "") ?: ""
        set(v) = prefs.edit().putString("webdav_username", v).apply()

    var webDavPassword: String
        get() = prefs.getString("webdav_password", "") ?: ""
        set(v) = prefs.edit().putString("webdav_password", v).apply()

    // 保存路径
    var savePath: String
        get() = prefs.getString("save_path", "") ?: ""
        set(v) = prefs.edit().putString("save_path", v).apply()

    // 悬浮窗
    var showFloatingButton: Boolean
        get() = prefs.getBoolean("floating_button", true)
        set(v) = prefs.edit().putBoolean("floating_button", v).apply()

    // 下载质量 (0=高清 1=标清)
    var downloadQuality: Int
        get() = prefs.getInt("download_quality", 0)
        set(v) = prefs.edit().putInt("download_quality", v).apply()

    // 清爽模式
    var isCleanModeEnabled: Boolean
        get() = prefs.getBoolean("clean_mode", false)
        set(v) = prefs.edit().putBoolean("clean_mode", v).apply()

    // 定时退出（分钟，0=关闭）
    var autoExitMinutes: Int
        get() = prefs.getInt("auto_exit", 0)
        set(v) = prefs.edit().putInt("auto_exit", v).apply()
}
