# Dyoo Hook 点分析文档

## 分析方法

由于抖音每次混淆类名，**静态反编译不可靠**。本模块采用以下策略：

### 1. 稳定 Hook 点（不依赖混淆）
| Hook 点 | 类 | 方法 | 说明 |
|---------|---|------|------|
| DownloadManager | android.app.DownloadManager | enqueue() | 系统 API，捕获视频下载 URL |
| OkHttp 请求构建 | okhttp3.Request.Builder | url(String) | OkHttp 公开 API，去水印 |
| OkHttp 响应 | okhttp3.internal.connection.RealCall | execute() | 捕获视频响应 URL |
| ImageView | android.widget.ImageView | setImageURI(Uri) | 捕获图片 URL |
| MediaPlayer | android.media.MediaPlayer | start()/pause()/stop() | 检测播放状态 |
| Activity 生命周期 | android.app.Activity | onCreate()/onResume()/onPause() | UI 控制 |

### 2. 运行时搜索（DouyinFinder）
| 搜索目标 | 搜索策略 | 说明 |
|---------|---------|------|
| 视频数据模型 | 包含 "play_addr" 字段的类 | 抖音视频播放地址字段 |
| 播放控制器 | 包含 "video_player" 字段的类 | 视频播放状态控制 |
| 下载辅助类 | 包含 "enqueue" 方法的类 | 视频下载封装 |
| 水印构建类 | 包含 "watermark" 关键字的类 | 水印 URL 生成 |

### 3. Hook 策略详解

#### 视频下载
```
捕获路径: DownloadManager.enqueue() → OkHttp.execute() → DouyinFinder 搜索
冗余捕获: 多个路径捕获同一 URL，确保可靠性
```

#### 去水印
```
拦截点: okhttp3.Request.Builder.url(String)
处理: 移除 URL 中的 watermark/wm_aid/wm_tt 参数
```

#### 清爽模式
```
检测点: MediaPlayer.start() → 视频开始播放
        MediaPlayer.pause()/stop() → 视频暂停/停止
UI控制: 遍历视图树，隐藏非视频组件
触摸交互: 触摸屏幕 → 临时显示 UI 3秒 → 自动隐藏
```

#### 图片下载
```
捕获点: ImageView.setImageURI(Uri)
过滤: 只捕获 HTTP/HTTPS URL
```

## 已知类名（可能因版本变化）

以下是从现有模块中收集的抖音混淆前类名（仅供参考）：

- `com.ss.android.ugc.aweme.feed.model.Aweme` - 视频数据模型
- `com.ss.android.ugc.aweme.feed.model.Video` - 视频对象
- `com.ss.android.ugc.aweme.player.IPlayerManager` - 播放器管理器
- `com.ss.android.ugc.aweme.download.impl.DownloadManager` - 下载管理器
- `com.ss.android.ugc.aweme.watermark.WaterMarkService` - 水印服务

## 兼容性

- **系统 API**: Android 5.0+ (所有版本)
- **OkHttp**: 3.x, 4.x (抖音使用 4.x)
- **YukiHookAPI**: 1.1.9
- **LSPosed**: 全版本
