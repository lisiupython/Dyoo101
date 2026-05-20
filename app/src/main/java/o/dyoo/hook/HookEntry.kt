package o.dyoo.hook

import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import o.dyoo.hook.dexkit.DouyinFinder
import o.dyoo.hook.impl.VideoHook
import o.dyoo.hook.impl.ImageHook
import o.dyoo.hook.impl.WatermarkHook
import o.dyoo.hook.impl.PopupHook
import o.dyoo.hook.impl.CleanModeHook

/**
 * Dyoo 模块入口 - libxposed API 101
 *
 * 继承 XposedModule，通过生命周期回调管理 Hook
 */
class HookEntry : XposedModule() {

    companion object {
        private const val TAG = "Dyoo.HookEntry"
        private const val TARGET_PACKAGE = "com.ss.android.ugc.aweme"

        // 模块实例引用
        lateinit var module: XposedModule
            private set
    }

    init {
        module = this
        Log.i(TAG, "Dyoo 模块已加载 (libxposed API 101)")
    }

    /**
     * 模块加载回调 - 在模块被加载到目标进程时调用
     */
    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        Log.i(TAG, "模块加载: process=${param.processName}, framework=${frameworkName}")
    }

    /**
     * 包加载回调 - 安装 Hook 的主要时机
     */
    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        val packageName = param.packageName
        Log.i(TAG, "包加载: $packageName")

        if (packageName != TARGET_PACKAGE) return

        Log.i(TAG, "检测到抖音，开始安装 Hook")

        try {
            val classLoader = param.classLoader  // ✅ 应用实际使用的 ClassLoader

            // 1. 运行时搜索抖音关键类
            DouyinFinder.init(classLoader)

            // 2. 注册各模块 Hook
            VideoHook.setup(this, classLoader)
            ImageHook.setup(this, classLoader)
            WatermarkHook.setup(this, classLoader)
            PopupHook.setup(this, classLoader)
            CleanModeHook.setup(this, classLoader)

            Log.i(TAG, "所有 Hook 安装完成")
        } catch (e: Throwable) {
            Log.e(TAG, "Hook 安装失败: ${e.message}", e)
        }
    }
}
