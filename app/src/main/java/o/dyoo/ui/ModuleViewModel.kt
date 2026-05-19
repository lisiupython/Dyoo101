package o.dyoo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import o.dyoo.core.config.ModuleConfig

class ModuleViewModel : ViewModel() {

    private val _videoDownload = MutableLiveData(ModuleConfig.isVideoDownloadEnabled)
    val videoDownload: LiveData<Boolean> = _videoDownload

    private val _imageDownload = MutableLiveData(ModuleConfig.isImageDownloadEnabled)
    val imageDownload: LiveData<Boolean> = _imageDownload

    private val _watermarkRemove = MutableLiveData(ModuleConfig.isWatermarkRemoveEnabled)
    val watermarkRemove: LiveData<Boolean> = _watermarkRemove

    private val _webDavEnabled = MutableLiveData(ModuleConfig.isWebDavEnabled)
    val webDavEnabled: LiveData<Boolean> = _webDavEnabled

    private val _webDavUrl = MutableLiveData(ModuleConfig.webDavUrl)
    val webDavUrl: LiveData<String> = _webDavUrl

    private val _webDavUsername = MutableLiveData(ModuleConfig.webDavUsername)
    val webDavUsername: LiveData<String> = _webDavUsername

    private val _webDavPassword = MutableLiveData(ModuleConfig.webDavPassword)
    val webDavPassword: LiveData<String> = _webDavPassword

    private val _floatingButton = MutableLiveData(ModuleConfig.showFloatingButton)
    val floatingButton: LiveData<Boolean> = _floatingButton

    private val _downloadQuality = MutableLiveData(ModuleConfig.downloadQuality)
    val downloadQuality: LiveData<Int> = _downloadQuality

    private val _cleanMode = MutableLiveData(ModuleConfig.isCleanModeEnabled)
    val cleanMode: LiveData<Boolean> = _cleanMode

    private val _autoExitMinutes = MutableLiveData(ModuleConfig.autoExitMinutes)
    val autoExitMinutes: LiveData<Int> = _autoExitMinutes

    fun setVideoDownload(v: Boolean) { ModuleConfig.isVideoDownloadEnabled = v; _videoDownload.value = v }
    fun setImageDownload(v: Boolean) { ModuleConfig.isImageDownloadEnabled = v; _imageDownload.value = v }
    fun setWatermarkRemove(v: Boolean) { ModuleConfig.isWatermarkRemoveEnabled = v; _watermarkRemove.value = v }
    fun setWebDavEnabled(v: Boolean) { ModuleConfig.isWebDavEnabled = v; _webDavEnabled.value = v }
    fun setFloatingButton(v: Boolean) { ModuleConfig.showFloatingButton = v; _floatingButton.value = v }
    fun setCleanMode(v: Boolean) { ModuleConfig.isCleanModeEnabled = v; _cleanMode.value = v }
    fun setDownloadQuality(v: Int) { ModuleConfig.downloadQuality = v; _downloadQuality.value = v }
    fun setAutoExitMinutes(v: Int) { ModuleConfig.autoExitMinutes = v; _autoExitMinutes.value = v }

    fun setWebDavConfig(url: String, user: String, pass: String) {
        ModuleConfig.webDavUrl = url
        ModuleConfig.webDavUsername = user
        ModuleConfig.webDavPassword = pass
        _webDavUrl.value = url
        _webDavUsername.value = user
        _webDavPassword.value = pass
    }
}
