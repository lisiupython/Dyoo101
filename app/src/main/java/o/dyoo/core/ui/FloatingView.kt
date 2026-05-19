package o.dyoo.core.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import o.dyoo.core.config.ModuleConfig
import o.dyoo.core.download.Downloader
import o.dyoo.hook.impl.VideoHook
import o.dyoo.hook.impl.ImageHook

/**
 * Dyoo 悬浮窗
 * 在抖音界面显示快捷操作按钮
 */
object FloatingView {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var panelView: View? = null
    private var isShowing = false
    private var isPanelShowing = false

    /**
     * 显示悬浮按钮
     */
    fun show(activity: Activity) {
        if (isShowing) return

        try {
            windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val button = createFloatingButton(activity)
            val params = createLayoutParams()

            windowManager?.addView(button, params)
            floatingView = button
            isShowing = true

        } catch (e: Throwable) {
            Log.e("Dyoo", "Dyoo: Show floating view failed: ${e.message}")
        }
    }

    /**
     * 隐藏悬浮窗
     */
    fun hide() {
        try {
            hidePanel()
            floatingView?.let {
                windowManager?.removeView(it)
            }
        } catch (_: Throwable) {}
        floatingView = null
        isShowing = false
    }

    /**
     * 创建悬浮按钮
     */
    private fun createFloatingButton(activity: Activity): View {
        val button = ImageButton(activity).apply {
            // 使用内置图标
            setImageResource(android.R.drawable.ic_menu_more)
            setBackgroundColor(Color.parseColor("#CC2196F3"))
            alpha = 0.85f
            setPadding(12, 12, 12, 12)
            scaleType = android.widget.ImageView.ScaleType.CENTER_INSIDE

            setOnClickListener {
                togglePanel(activity)
            }

            // 拖动支持
            var initialX = 0
            var initialY = 0
            var initialTouchX = 0f
            var initialTouchY = 0f
            var isDrag = false

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = (layoutParams as? WindowManager.LayoutParams)?.x ?: 0
                        initialY = (layoutParams as? WindowManager.LayoutParams)?.y ?: 0
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDrag = false
                        false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - initialTouchX
                        val dy = event.rawY - initialTouchY
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                            isDrag = true
                            val params = layoutParams as? WindowManager.LayoutParams ?: return@setOnTouchListener true
                            params.x = initialX - dx.toInt()
                            params.y = initialY + dy.toInt()
                            windowManager?.updateViewLayout(view, params)
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isDrag) view.performClick()
                        true
                    }
                    else -> false
                }
            }
        }

        // 圆形背景
        val wrapper = FrameLayout(activity).apply {
            val size = 140 // dp
            val density = activity.resources.displayMetrics.density
            val px = (size * density).toInt()
            val lp = FrameLayout.LayoutParams(px, px)
            button.layoutParams = lp
            addView(button)
        }

        return wrapper
    }

    /**
     * 创建 WindowManager 参数
     */
    private fun createLayoutParams(): WindowManager.LayoutParams {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 24
            y = 300
        }
    }

    /**
     * 切换功能面板
     */
    private fun togglePanel(activity: Activity) {
        if (isPanelShowing) {
            hidePanel()
        } else {
            showPanel(activity)
        }
    }

    /**
     * 显示功能面板
     */
    private fun showPanel(activity: Activity) {
        if (isPanelShowing) return

        try {
            val panel = createPanel(activity)
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.CENTER
            }

            windowManager?.addView(panel, params)
            panelView = panel
            isPanelShowing = true
        } catch (e: Throwable) {
            Log.e("Dyoo", "Dyoo: Show panel failed: ${e.message}")
        }
    }

    /**
     * 隐藏功能面板
     */
    private fun hidePanel() {
        try {
            panelView?.let { windowManager?.removeView(it) }
        } catch (_: Throwable) {}
        panelView = null
        isPanelShowing = false
    }

    /**
     * 创建功能面板
     */
    private fun createPanel(activity: Activity): View {
        val density = activity.resources.displayMetrics.density
        val padding = (16 * density).toInt()

        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#F0FFFFFF"))
            setPadding(padding, padding, padding, padding)

            // 圆角背景
            background = android.graphics.drawable.GradientDrawable().apply {
                setColor(Color.parseColor("#F0FFFFFF"))
                cornerRadius = 16 * density
            }
        }

        // 标题
        val title = TextView(activity).apply {
            text = "Dyoo"
            textSize = 20f
            setTextColor(Color.parseColor("#2196F3"))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, padding)
        }
        layout.addView(title)

        // 功能按钮列表
        val buttons = listOf(
            Pair("下载视频") { actionDownloadVideo(activity) },
            Pair("保存图片") { actionSaveImage(activity) },
            Pair("复制链接") { actionCopyLink(activity) },
            Pair("定时退出") { actionExitTimer(activity) },
            Pair("关闭面板") { hidePanel() }
        )

        for ((text, action) in buttons) {
            val btn = android.widget.Button(activity).apply {
                this.text = text
                textSize = 16f
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.parseColor("#2196F3"))
                setOnClickListener {
                    action()
                    hidePanel()
                }

                val lp = LinearLayout.LayoutParams(
                    (220 * density).toInt(),
                    (48 * density).toInt()
                ).apply {
                    bottomMargin = (8 * density).toInt()
                }
                layoutParams = lp
            }
            layout.addView(btn)
        }

        return layout
    }

    // === 操作方法 ===

    private fun actionDownloadVideo(activity: Activity) {
        VideoHook.downloadCurrentVideo(activity)
    }

    private fun actionSaveImage(activity: Activity) {
        ImageHook.saveCurrentImage(activity)
    }

    private fun actionCopyLink(activity: Activity) {
        VideoHook.copyCurrentLink(activity)
    }

    private fun actionExitTimer(activity: Activity) {
        val options = arrayOf("5 分钟", "10 分钟", "30 分钟", "60 分钟", "关闭")
        android.app.AlertDialog.Builder(activity)
            .setTitle("定时退出")
            .setItems(options) { _, which ->
                val minutes = when (which) {
                    0 -> 5; 1 -> 10; 2 -> 30; 3 -> 60; 4 -> 0
                    else -> 0
                }
                if (minutes > 0) {
                    Toast.makeText(activity, "${minutes} 分钟后自动退出抖音", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        activity.finishAffinity()
                    }, minutes * 60 * 1000L)
                } else {
                    Toast.makeText(activity, "已取消定时退出", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }
}
