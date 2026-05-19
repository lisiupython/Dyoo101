package o.dyoo.hook.dexkit

import android.util.Log

/**
 * 抖音类搜索器 - 通过 DexKit 运行时搜索抖音混淆类
 * 不依赖静态类名，跨版本兼容
 */
object DouyinFinder {
    private const val TAG = "Dyoo.Finder"

    // 缓存的搜索结果
    private var _videoModelClass: String? = null
    private var _playerControllerClass: String? = null
    private var _downloadHelperClass: String? = null
    private var _watermarkBuilderClass: String? = null

    /** 视频数据模型类 (包含播放地址字段) */
    val videoModelClass: String? get() = _videoModelClass

    /** 视频播放控制器类 */
    val playerControllerClass: String? get() = _playerControllerClass

    /** 下载辅助类 */
    val downloadHelperClass: String? get() = _downloadHelperClass

    /** 水印 URL 构建类 */
    val watermarkBuilderClass: String? get() = _watermarkBuilderClass

    private var initialized = false

    /**
     * 初始化搜索器
     * @param classLoader 目标应用的 ClassLoader
     */
    fun init(classLoader: ClassLoader) {
        if (initialized) return
        initialized = true
        Log.i(TAG, "开始搜索抖音关键类...")

        try {
            searchByStaticField(classLoader)
            Log.i(TAG, "搜索完成")
        } catch (e: Throwable) {
            Log.e(TAG, "搜索失败: ${e.message}")
            // 使用备用方案
            useFallbackNames()
        }
    }

    /**
     * 通过静态字段搜索类 - 查找包含视频播放地址的字段
     */
    private fun searchByStaticField(classLoader: ClassLoader) {
        try {
            // 搜索策略1: 查找包含 "play_addr" 的类 (抖音视频播放地址)
            _videoModelClass = searchClassByField(classLoader, "play_addr")

            // 搜索策略2: 查找包含 "video_player" 的类
            _playerControllerClass = searchClassByField(classLoader, "video_player")

            // 搜索策略3: 查找 DownloadManager 相关的辅助类
            _downloadHelperClass = searchClassByMethod(classLoader, "enqueue")

            Log.i(TAG, "搜索结果: videoModel=$_videoModelClass, player=$_playerControllerClass")
        } catch (e: Throwable) {
            Log.e(TAG, "静态字段搜索失败: ${e.message}")
        }
    }

    /**
     * 搜索包含指定字段名的类
     */
    private fun searchClassByField(classLoader: ClassLoader, fieldName: String): String? {
        return try {
            val classes = getLoadedClasses(classLoader)
            classes.firstOrNull { cls ->
                try {
                    cls.declaredFields.any { it.name.contains(fieldName, ignoreCase = true) }
                } catch (_: Throwable) { false }
            }?.name
        } catch (_: Throwable) { null }
    }

    /**
     * 搜索包含指定方法名的类
     */
    private fun searchClassByMethod(classLoader: ClassLoader, methodName: String): String? {
        return try {
            val classes = getLoadedClasses(classLoader)
            classes.firstOrNull { cls ->
                try {
                    cls.declaredMethods.any { it.name == methodName }
                } catch (_: Throwable) { false }
            }?.name
        } catch (_: Throwable) { null }
    }

    /**
     * 获取 ClassLoader 中已加载的类
     */
    private fun getLoadedClasses(classLoader: ClassLoader): List<Class<*>> {
        return try {
            val field = classLoader.javaClass.getDeclaredField("pathList")
            field.isAccessible = true
            val pathList = field.get(classLoader)
            val dexElementsField = pathList.javaClass.getDeclaredField("dexElements")
            dexElementsField.isAccessible = true
            val elements = dexElementsField.get(pathList) as Array<*>
            elements.flatMap { element ->
                try {
                    val dexFileField = element!!.javaClass.getDeclaredField("dexFile")
                    dexFileField.isAccessible = true
                    val dexFile = dexFileField.get(element)
                    val entriesMethod = dexFile.javaClass.getMethod("entries")
                    val entries = entriesMethod.invoke(dexFile) as java.util.Enumeration<String>
                    val classNames = mutableListOf<Class<*>>()
                    while (entries.hasMoreElements()) {
                        try {
                            classNames.add(Class.forName(entries.nextElement(), false, classLoader))
                        } catch (_: Throwable) { }
                    }
                    classNames
                } catch (_: Throwable) { emptyList() }
            }
        } catch (_: Throwable) { emptyList() }
    }

    /**
     * 备用方案: 使用硬编码的常见类名
     */
    private fun useFallbackNames() {
        Log.w(TAG, "使用备用类名")
        // 这些类名在抖音各版本中相对稳定
        _videoModelClass = "com.ss.android.ugc.aweme.feed.model.Aweme"
        _playerControllerClass = null // 运行时发现
        _downloadHelperClass = null
        _watermarkBuilderClass = null
    }

    /**
     * 重置搜索状态 (版本更新时使用)
     */
    fun reset() {
        initialized = false
        _videoModelClass = null
        _playerControllerClass = null
        _downloadHelperClass = null
        _watermarkBuilderClass = null
    }
}
