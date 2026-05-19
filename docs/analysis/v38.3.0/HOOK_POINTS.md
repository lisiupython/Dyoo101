# Dyoo Hook 点分析 - 真实数据分析

**抖音版本：** v38.3.0 (versionCode: 380300)
**分析日期：** 2026-03-29
**分析方法：** APK 反编译 (strings 提取 DEX 文件)
**包名：** com.ss.android.ugc.aweme
**DEX 数量：** 59 个

---

## 📌 Hook 点总览

| # | 功能 | Hook 类 | Hook 方法 | 稳定性 | Hook 类型 |
|---|------|---------|-----------|--------|----------|
| 1 | 视频下载 | DownloadManager | enqueue() | ⭐⭐⭐⭐⭐ | 系统 API |
| 2 | 视频 URL 捕获 | okhttp3.RealCall | execute() | ⭐⭐⭐⭐⭐ | OkHttp |
| 3 | 图片保存 | ImageView | setImageURI() | ⭐⭐⭐⭐⭐ | 系统 API |
| 4 | 去水印 | okhttp3.Request.Builder | url() | ⭐⭐⭐⭐⭐ | OkHttp |
| 5 | 视频播放管理 | VideoPlayManager | (动态搜索) | ⭐⭐⭐⭐ | 抖音核心 |
| 6 | 视频详情页 | DetailActivity | onCreate() | ⭐⭐⭐ | 抖音 UI |
| 7 | Feed 推荐流 | FeedRecommendFragment | onCreateView() | ⭐⭐⭐ | 抖音 UI |
| 8 | Feed 关注流 | FeedFollowFragment | onCreateView() | ⭐⭐⭐ | 抖音 UI |
| 9 | 搜索结果 | SearchResultActivity | onCreate() | ⭐⭐⭐ | 抖音 UI |
| 10 | 评论区 | CommentFeedActivity | onCreate() | ⭐⭐⭐ | 抖音 UI |
| 11 | 主页 | MainActivity | onCreate() | ⭐⭐⭐ | 抖音 UI |
| 12 | 下载服务 | DownloadServiceImpl | (动态搜索) | ⭐⭐⭐ | 抖音服务 |
| 13 | 分享服务 | ShareServiceImpl | (动态搜索) | ⭐⭐⭐ | 抖音服务 |
| 14 | 悬浮窗 | Activity | onResume()/onPause() | ⭐⭐⭐⭐⭐ | 系统 API |
| 15 | 清爽模式 | MediaPlayer | start()/pause() | ⭐⭐⭐⭐⭐ | 系统 API |

---

## 📌 详细分析

### 1. 视频下载 (DownloadManager)
```
Hook 类: android.app.DownloadManager
Hook 方法: enqueue(DownloadManager.Request)
Hook 时机: beforeHook
Hook 策略: 捕获 Request 对象中的 mUri 字段，提取视频 URL
稳定性: ⭐⭐⭐⭐⭐ (系统 API，永不混淆)
```

### 2. 视频 URL 捕获 (OkHttp)
```
Hook 类: okhttp3.internal.connection.RealCall
Hook 方法: execute()
Hook 时机: afterHook
Hook 策略: 从 Response 对象获取 Request.url()，过滤包含 play_addr 的请求
相关类:
  - okhttp3.Cache$CacheRequestImpl
  - okhttp3.Cache$CacheResponseBody
  - okhttp3.internal.http.HttpCodec
  - okhttp3.internal.http1.Http1Codec
  - okhttp3.internal.http2.Http2Codec
稳定性: ⭐⭐⭐⭐⭐ (OkHttp 公开 API)
```

### 3. 图片保存 (ImageView)
```
Hook 类: android.widget.ImageView
Hook 方法: setImageURI(Uri)
Hook 时机: beforeHook
Hook 策略: 捕获 Uri 参数中的图片 URL
稳定性: ⭐⭐⭐⭐⭐ (系统 API，永不混淆)
```

### 4. 去水印 (OkHttp Builder)
```
Hook 类: okhttp3.Request.Builder
Hook 方法: url(String)
Hook 时机: beforeHook
Hook 策略: 检测 URL 中的水印参数 (watermark, wm_aid, wm_tt)
处理: 移除水印参数后重新设置 URL
稳定性: ⭐⭐⭐⭐⭐ (OkHttp 公开 API)
```

### 5. 视频播放管理 (VideoPlayManager)
```
真实类名: Lcom/ss/android/ugc/aweme/player/VideoPlayManager
相关类:
  - Lcom/ss/android/ugc/aweme/player/sdk/api/IPlayer
  - Lcom/ss/android/ugc/aweme/player/sdk/api/IPlayable
  - Lcom/ss/android/ugc/aweme/player/sdk/api/INetClient
  - Lcom/ss/android/ugc/aweme/player/PlayerHelper
  - Lcom/ss/android/ugc/aweme/player/player/PageContext
  - Lcom/ss/android/ugc/aweme/player/player/LoadingState
  - Lcom/ss/android/ugc/aweme/player/player/ScaleType
  - Lcom/ss/android/ugc/aweme/player/player/SeekState
稳定性: ⭐⭐⭐⭐ (抖音核心播放器，类名保留)
Hook 时机: 动态搜索后 hook
```

### 6. 视频详情页 (DetailActivity)
```
真实类名: Lcom/ss/android/ugc/aweme/detail/ui/DetailActivity
相关类:
  - Lcom/ss/android/ugc/aweme/detail/ui/DetailFragment
  - Lcom/ss/android/ugc/aweme/detail/api/DetailApi
  - Lcom/ss/android/ugc/aweme/detail/model/BatchDetailList
  - Lcom/ss/android/ugc/aweme/detail/arch/DetailFeedRouter
  - Lcom/ss/android/ugc/aweme/detail/PlayletDetailActivity
  - Lcom/ss/android/ugc/aweme/detail/ab/DetailWaterMaskExp
Hook 方法: onCreate()
Hook 时机: afterHook
稳定性: ⭐⭐⭐ (抖音 UI 类，可能变化)
```

### 7. Feed 推荐流 (FeedRecommendFragment)
```
真实类名: Lcom/ss/android/ugc/aweme/feed/ui/FeedRecommendFragment
相关类:
  - Lcom/ss/android/ugc/aweme/feed/ui/BaseFeedListFragment
  - Lcom/ss/android/ugc/aweme/feed/panel/BaseListFragmentPanel
  - Lcom/ss/android/ugc/aweme/feed/panel/FragmentPanel
  - Lcom/ss/android/ugc/aweme/feed/ui/FeedActionSheet
稳定性: ⭐⭐⭐ (抖音 UI 类，可能变化)
Hook 方法: 动态搜索后 hook
```

### 8. Feed 关注流 (FeedFollowFragment)
```
真实类名: Lcom/ss/android/ugc/aweme/feed/ui/FeedFollowFragment
相关类:
  - Lcom/ss/android/ugc/aweme/feed/ui/FeedFamiliarFragment
Hook 方法: 动态搜索后 hook
稳定性: ⭐⭐⭐ (抖音 UI 类，可能变化)
```

### 9. 搜索结果页 (SearchResultActivity)
```
真实类名: Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivity
相关类:
  - Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivityForPad
  - Lcom/ss/android/ugc/aweme/search/activity/SearchPartShowResultActivity
  - Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivityTranslucent
  - Lcom/ss/android/ugc/aweme/search/activity/SearchResultForCommodityActivity
  - Lcom/ss/android/ugc/aweme/search/common/activity/ECSearchActivity
  - Lcom/ss/android/ugc/aweme/search/vision/PhotoSearchActivity
  - Lcom/ss/android/ugc/aweme/search/vision/VideoSearchActivity
Hook 方法: onCreate()
稳定性: ⭐⭐⭐ (抖音 UI 类，可能变化)
```

### 10. 评论区 (CommentFeedActivity)
```
真实类名: Lcom/ss/android/ugc/aweme/commentfeed/CommentFeedActivity
相关类:
  - Lcom/ss/android/ugc/aweme/comment/ui/ImageDetailActivity
  - Lcom/ss/android/ugc/aweme/comment/ui/GifEmojiDetailActivity
  - Lcom/ss/android/ugc/aweme/comment/keyboard/refactor/evaluate/EvaluationKeyboardActivity
Hook 方法: onCreate()
稳定性: ⭐⭐⭐ (抖音 UI 类，可能变化)
```

### 11. 主页 (MainActivity)
```
真实类名: Lcom/ss/android/ugc/aweme/main/MainActivity
相关类:
  - Lcom/ss/android/ugc/aweme/main/MainFragment
  - Lcom/ss/android/ugc/aweme/main/MainPageFragment
  - Lcom/ss/android/ugc/aweme/main/BaseMainHelper
  - Lcom/ss/android/ugc/aweme/main/TabChangeManager
Hook 方法: onCreate()
稳定性: ⭐⭐⭐ (抖音核心 Activity，类名保留)
```

### 12. 下载服务 (DownloadServiceImpl)
```
真实类名: Lcom/ss/android/ugc/aweme/download/impl/component_impl/DownloadServiceImpl
相关类:
  - Lcom/ss/android/ugc/aweme/download/DownloadImageManager
  - Lcom/ss/android/ugc/aweme/download/XDownloadApi
  - Lcom/ss/android/ugc/aweme/download/component_api/AbsDownloadTask
  - Lcom/ss/android/ugc/aweme/download/component_api/DownloadServiceManager
  - Lcom/ss/android/ugc/aweme/download/component_api/service/IDownloadService
  - Lcom/ss/android/ugc/aweme/download/component_api/listener/IAutoUnzipListener
  - Lcom/ss/android/ugc/aweme/download/NoticeDownloadRemindDialog
  - Lcom/ss/android/ugc/aweme/download/SaveSettingsKevaService
  - Lcom/ss/android/ugc/aweme/download/component_api/DownloadScene
稳定性: ⭐⭐⭐ (抖音核心服务，类名保留)
Hook 方法: 动态搜索后 hook
```

### 13. 分享服务 (ShareServiceImpl)
```
真实类名: Lcom/ss/android/ugc/aweme/share/ShareServiceImpl
相关类:
  - Lcom/ss/android/ugc/aweme/share/ShareService
  - Lcom/ss/android/ugc/aweme/share/ShareActionImpl
  - Lcom/ss/android/ugc/aweme/share/ShareExtService
  - Lcom/ss/android/ugc/aweme/share/ShareChannelImpl
  - Lcom/ss/android/ugc/aweme/share/CommonSharePanel
  - Lcom/ss/android/ugc/aweme/share/MiscDownloadAddrs
  - Lcom/ss/android/ugc/aweme/share/utils/Wrapper
  - Lcom/ss/android/ugc/aweme/sharer/ShareContent
  - Lcom/ss/android/ugc/aweme/sharer/ui/SharePackage
稳定性: ⭐⭐⭐ (抖音核心服务，类名保留)
Hook 方法: 动态搜索后 hook
```

### 14. 悬浮窗 (Activity Lifecycle)
```
Hook 类: android.app.Activity
Hook 方法: onResume() / onPause()
Hook 时机: afterHook / beforeHook
Hook 策略: 在 Activity 生命周期中显示/隐藏悬浮窗
稳定性: ⭐⭐⭐⭐⭐ (系统 API，永不混淆)
```

### 15. 清爽模式 (MediaPlayer)
```
Hook 类: android.media.MediaPlayer
Hook 方法: start() / pause() / stop()
Hook 时机: afterHook
Hook 策略: 检测到视频播放后隐藏所有非视频 UI 组件
稳定性: ⭐⭐⭐⭐⭐ (系统 API，永不混淆)
```

---

## 📌 抖音包结构 (v38.3.0)

```
com.ss.android.ugc.aweme
├── activity                - Activity 基类
├── bullet.bridge.framework - 桥接框架
├── commentfeed             - 评论区
│   ├── ui                  - 评论 UI
│   └── keyboard            - 评论键盘
├── detail                  - 详情页
│   ├── ui                  - 详情 UI
│   ├── api                 - 详情 API
│   ├── model               - 详情数据模型
│   └── arch                - 详情路由
├── download                - 下载服务
│   ├── impl                - 下载实现
│   └── component_api       - 下载组件 API
├── feed                    - Feed 数据
│   ├── model               - Feed 模型 (Aweme, Video, ...)
│   ├── ui                  - Feed UI
│   ├── panel               - Feed 面板
│   └── event               - Feed 事件
├── im                      - 即时通讯
├── main                    - 主页 (MainActivity, MainFragment)
├── player                  - 播放器
│   ├── sdk                 - 播放器 SDK
│   └── player              - 播放器实现
├── search                  - 搜索
│   ├── activity            - 搜索 Activity
│   └── vision              - 视觉搜索
├── share                   - 分享
│   ├── share               - 分享核心
│   └── sharer              - 分享器
└── xlog                    - 日志系统
```

---

## 📌 关键数据模型

### Aweme (视频核心模型)
```
真实类名: Lcom/ss/android/ugc/aweme/feed/model/Aweme
关键字段:
  - play_addr: 视频播放地址
  - video_url: 视频 URL
  - video: Video 对象 (包含 bitrate, play_addr 等)
  - desc: 视频描述
  - author: 用户信息
  - like_count: 点赞数
  - comment_count: 评论数
  - share_count: 分享数
  - aweme_url: AwemeUrl 对象
```

### Video (视频详情模型)
```
真实类名: Lcom/ss/android/ugc/aweme/feed/model/Video
关键字段:
  - play_addr: 视频播放地址 (VideoUrl 对象)
  - play_addr_265: H265 视频地址
  - play_addr_bytevc1: ByteVC1 视频地址
  - bit_rate: BitRate 数组
  - width: 视频宽度
  - height: 视频高度
  - duration: 视频时长
```

### BitRate (视频比特率)
```
真实类名: Lcom/ss/android/ugc/aweme/feed/model/BitRate
关键字段:
  - play_addr: 播放地址
  - bit_rate: 比特率
  - quality_type: 质量类型
```

### MiscDownloadAddrs (下载地址)
```
真实类名: Lcom/ss/android/ugc/aweme/share/MiscDownloadAddrs
关键字段:
  - download_url: 下载地址
  - is_watermark: 是否有水印
```

---

## 📌 水印相关

```
水印类:
  - Lcom/ss/ugc/aweme/WaterMarkInfo
  - Lcom/ss/android/ugc/aweme/detail/ab/DetailWaterMaskExp
  - Lcom/ss/android/ugc/aweme/share/utils/Wrapper (可能包含水印处理)

水印参数:
  - watermark: 水印标识
  - wm_aid: 水印 ID
  - wm_tt: 水印时间戳

水印处理:
  - DetailWaterMaskExp: 详情页水印设置
  - publishSaveLocalWithoutWatermark: 保存到本地无水印
```

---

## 📌 Hook 稳定性等级说明

| 等级 | 说明 | 适用场景 |
|------|------|---------|
| ⭐⭐⭐⭐⭐ | 系统 API | DownloadManager, MediaPlayer, ImageView, OkHttp |
| ⭐⭐⭐⭐ | 抖音核心播放器 | VideoPlayManager, PlayerHelper |
| ⭐⭐⭐ | 抖音内部 UI/服务 | DetailActivity, FeedFragment, DownloadService |
| ⭐⭐ | 抖音内部方法 | share(), handleDiggClick() |
| ⭐ | 极不稳定 | 无 |

---

## 📌 与 Dyoo 复刻版对应关系

| Dyoo 功能 | 对应 Hook 点 | 实现方式 |
|-----------|-------------|---------|
| 视频下载 | #1, #2, #12 | DownloadManager + OkHttp + DownloadServiceImpl |
| 图片下载 | #3 | ImageView.setImageURI |
| 去水印 | #4, 水印相关 | okhttp3.Builder.url + WaterMarkInfo |
| 分享解锁 | #13 | ShareServiceImpl |
| 清爽模式 | #7, #8, #15 | FeedFragment + MediaPlayer |
| 悬浮窗 | #11, #14 | MainActivity + Activity Lifecycle |

---

**分析完成：** 2026-03-29
**分析工具：** DEX 文件字符串提取
**下次更新：** 抖音版本更新后
