package o.dyoo.hook.impl

import android.app.Activity
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.interfaces.MethodHooker
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
            module.hook(onResumeMethod, object : MethodHooker {
                override fun before(args: Array<Any?>): Any? = null

                override fun after(result: Any?): Any? {
                    (this@after as? Activity)?.let {
                        if (it.packageName == "com.ss.android.ugc.aweme") {
                            FloatingView.show(it)
                        }
                    }
                    return result
                }
            })

            // Hook Activity.onPause
            val onPauseMethod = Activity::class.java.getDeclaredMethod("onPause")
            module.hook(onPauseMethod, object : MethodHooker {
                override fun before(args: Array<Any?>): Any? {
                    (this@before as? Activity)?.let {
                        if (it.packageName == "com.ss.android.ugc.aweme") {
                            FloatingView.hide()
                        }
                    }
                    return null
                }

                override fun after(result: Any?): Any? = result
            })
        } catch (e: Throwable) {
            // 静默处理
        }
    }
}
