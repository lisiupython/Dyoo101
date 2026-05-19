package o.dyoo.app

import android.app.Application

/**
 * Dyoo Application
 * 模块配置应用入口
 */
class DyooApplication : Application() {

    companion object {
        lateinit var instance: DyooApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
