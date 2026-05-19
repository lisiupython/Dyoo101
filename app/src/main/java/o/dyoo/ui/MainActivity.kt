package o.dyoo.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import o.dyoo.BuildConfig
import o.dyoo.core.config.ModuleConfig
import o.dyoo.databinding.ActivityMainBinding

/**
 * Dyoo 主设置界面
 */
class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val vm: ModuleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        title = "Dyoo 设置"
        setupViews()
        observeState()
    }

    private fun setupViews() {
        b.tvVersion.text = "Dyoo v${BuildConfig.VERSION_NAME}"

        b.switchVideo.setOnCheckedChangeListener { _, checked -> vm.setVideoDownload(checked) }
        b.switchImage.setOnCheckedChangeListener { _, checked -> vm.setImageDownload(checked) }
        b.switchWatermark.setOnCheckedChangeListener { _, checked -> vm.setWatermarkRemove(checked) }
        b.switchWebdav.setOnCheckedChangeListener { _, checked ->
            vm.setWebDavEnabled(checked)
            b.btnWebdavConfig.isEnabled = checked
        }

        b.btnWebdavConfig.setOnClickListener { showWebDavDialog() }
        b.btnAdvanced.setOnClickListener { startActivity(Intent(this, ModuleActivity::class.java)) }
        b.btnAbout.setOnClickListener { showAbout() }
    }

    private fun observeState() {
        vm.videoDownload.observe(this) { b.switchVideo.isChecked = it }
        vm.imageDownload.observe(this) { b.switchImage.isChecked = it }
        vm.watermarkRemove.observe(this) { b.switchWatermark.isChecked = it }
        vm.webDavEnabled.observe(this) { b.switchWebdav.isChecked = it; b.btnWebdavConfig.isEnabled = it }
    }

    private fun showWebDavDialog() {
        val density = resources.displayMetrics.density
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding((24 * density).toInt(), (16 * density).toInt(), (24 * density).toInt(), 0)
        }

        fun makeHint(hint: String, text: String = ""): EditText {
            return EditText(this).apply {
                this.hint = hint
                setText(text)
                setSingleLine()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = (8 * density).toInt() }
            }
        }

        val etUrl = makeHint("服务器地址 (https://dav.xxx.com/dav/)", ModuleConfig.webDavUrl)
        val etUser = makeHint("用户名", ModuleConfig.webDavUsername)
        val etPass = makeHint("密码", ModuleConfig.webDavPassword).apply { inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD }

        layout.addView(etUrl)
        layout.addView(etUser)
        layout.addView(etPass)

        android.app.AlertDialog.Builder(this)
            .setTitle("WebDav 配置")
            .setView(layout)
            .setPositiveButton("保存") { _, _ ->
                vm.setWebDavConfig(
                    etUrl.text.toString().trim(),
                    etUser.text.toString().trim(),
                    etPass.text.toString()
                )
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showAbout() {
        android.app.AlertDialog.Builder(this)
            .setTitle("关于 Dyoo")
            .setMessage(
                """
                Dyoo v${BuildConfig.VERSION_NAME}
                
                抖音 Xposed 增强模块
                ▸ 视频/图片下载
                ▸ 去水印
                ▸ WebDav 上传
                ▸ 悬浮窗控制
                ▸ 定时退出
                
                基于 libxposed API 101
                免费模块，请勿在大陆平台传播
                """.trimIndent()
            )
            .setPositiveButton("OK", null)
            .show()
    }
}
