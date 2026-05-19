package o.dyoo.hook.impl

import android.app.Activity
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import o.dyoo.core.config.ModuleConfig
import o.dyoo.core.ui.FloatingView

object PopupHook {
    /**
     * 安装 Hook
     * @param module XposedModule 实例
     * @param classLoader 目标应用的 ClassLoader
     */
    fun setup(module: XposedModule, classLoader: ClassLoader) {
        if (!ModuleConfig.showFloatingButton) return

        try {
            // Hook Activity.onResume
            val onResumeMethod = Activity::class.java.getDeclaredMethod("onResume")
            module.hook(onResumeMethod).intercept(XposedInterface.Hooker { chain ->
                val result = chain.proceed()
                (chain.getThisObject() as? Activity)?.let {
                    if (it.packageName == "com.ss.android.ugc.aweme") {
                        FloatingView.show(it)
                    }
                }
                result
            })

            // Hook Activity.onPause
            val onPauseMethod = Activity::class.java.getDeclaredMethod("onPause")
            module.hook(onPauseMethod).intercept(XposedInterface.Hooker { chain ->
                (chain.getThisObject() as? Activity)?.let {
                    if (it.packageName == "com.ss.android.ugc.aweme") {
                        FloatingView.hide()
                    }
                }
                chain.proceed()
            })
        } catch (e: Throwable) {
            // 静默处理
        }
    }
}
