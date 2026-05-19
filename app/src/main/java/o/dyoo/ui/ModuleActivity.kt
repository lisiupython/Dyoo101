package o.dyoo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import o.dyoo.core.config.ModuleConfig
import o.dyoo.databinding.ActivityModuleBinding

/**
 * Dyoo 高级设置界面
 */
class ModuleActivity : AppCompatActivity() {

    private lateinit var b: ActivityModuleBinding
    private val vm: ModuleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityModuleBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.title = "高级设置"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViews()
        observeState()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    private fun setupViews() {
        b.switchFloating.setOnCheckedChangeListener { _, checked -> vm.setFloatingButton(checked) }
        b.switchCleanMode.setOnCheckedChangeListener { _, checked -> vm.setCleanMode(checked) }

        b.rbQualityHigh.setOnClickListener { vm.setDownloadQuality(0) }
        b.rbQualityLow.setOnClickListener { vm.setDownloadQuality(1) }

        b.btnSaveAutoExit.setOnClickListener {
            val minutes = b.etAutoExit.text.toString().toIntOrNull() ?: 0
            vm.setAutoExitMinutes(minutes)
            Toast.makeText(this, if (minutes > 0) "将在 ${minutes} 分钟后退出" else "已关闭定时退出", Toast.LENGTH_SHORT).show()
        }

        b.btnReset.setOnClickListener {
            android.app.AlertDialog.Builder(this)
                .setTitle("重置设置")
                .setMessage("确定要重置所有设置吗？")
                .setPositiveButton("确定") { _, _ ->
                    resetAll()
                    Toast.makeText(this, "已重置", Toast.LENGTH_SHORT).show()
                    recreate()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    private fun observeState() {
        vm.floatingButton.observe(this) { b.switchFloating.isChecked = it }
        vm.cleanMode.observe(this) { b.switchCleanMode.isChecked = it }
        vm.downloadQuality.observe(this) { quality ->
            b.rbQualityHigh.isChecked = quality == 0
            b.rbQualityLow.isChecked = quality == 1
        }
        vm.autoExitMinutes.observe(this) { b.etAutoExit.setText(it.toString()) }
    }

    private fun resetAll() {
        ModuleConfig.isVideoDownloadEnabled = true
        ModuleConfig.isImageDownloadEnabled = true
        ModuleConfig.isWatermarkRemoveEnabled = true
        ModuleConfig.isWebDavEnabled = false
        ModuleConfig.webDavUrl = ""
        ModuleConfig.webDavUsername = ""
        ModuleConfig.webDavPassword = ""
        ModuleConfig.savePath = ""
        ModuleConfig.showFloatingButton = true
        ModuleConfig.downloadQuality = 0
        ModuleConfig.autoExitMinutes = 0
    }
}
