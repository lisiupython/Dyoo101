# v38.3.0 已知混淆类名

## 混淆类名映射表

抖音 v38.3.0 使用了多层混淆策略：

1. **保留类名**: 核心业务类 (Aweme, Video, MainActivity, DetailActivity, ...)
2. **混淆类名**: 辅助类使用单字母或随机字母命名
3. **方法名保留**: 关键方法名 (play_addr, handleDiggClick, share) 保留

## 核心保留类名 (不混淆)

### Feed 模型
```
Lcom/ss/android/ugc/aweme/feed/model/Aweme
Lcom/ss/android/ugc/aweme/feed/model/Video
Lcom/ss/android/ugc/aweme/feed/model/BitRate
Lcom/ss/android/ugc/aweme/feed/model/AwemeUrl
Lcom/ss/android/ugc/aweme/feed/model/ImageInfo
Lcom/ss/android/ugc/aweme/feed/model/LongVideo
Lcom/ss/android/ugc/aweme/feed/model/MixStruct
Lcom/ss/android/ugc/aweme/feed/model/VPAInfo
Lcom/ss/android/ugc/aweme/feed/model/LogData
Lcom/ss/android/ugc/aweme/feed/model/Material
Lcom/ss/android/ugc/aweme/feed/model/Anchor
Lcom/ss/android/ugc/aweme/feed/model/Extra
Lcom/ss/android/ugc/aweme/feed/model/AdAck
Lcom/ss/android/ugc/aweme/feed/model/AdQpons
Lcom/ss/android/ugc/aweme/feed/model/QponInfo
Lcom/ss/android/ugc/aweme/feed/model/Preload
Lcom/ss/android/ugc/aweme/feed/model/GameInfo
Lcom/ss/android/ugc/aweme/feed/model/ItemType
Lcom/ss/android/ugc/aweme/feed/model/LodeLade
Lcom/ss/android/ugc/aweme/feed/model/MVStruct
Lcom/ss/android/ugc/aweme/feed/model/PkgInfos
Lcom/ss/android/ugc/aweme/feed/model/TwistInfo
Lcom/ss/android/ugc/aweme/feed/model/BoxData
Lcom/ss/android/ugc/aweme/feed/model/BannerTip
Lcom/ss/android/ugc/aweme/feed/model/LogPbBean
```

### 主页 Activity
```
Lcom/ss/android/ugc/aweme/main/MainActivity
Lcom/ss/android/ugc/aweme/main/MainFragment
Lcom/ss/android/ugc/aweme/main/MainPageFragment
Lcom/ss/android/ugc/aweme/main/BaseMainHelper
Lcom/ss/android/ugc/aweme/main/TabChangeManager
Lcom/ss/android/ugc/aweme/main/BaseTabChangeManager
Lcom/ss/android/ugc/aweme/main/IMainActivity
Lcom/ss/android/ugc/aweme/main/IMainFragment
Lcom/ss/android/ugc/aweme/main/IMainPageService
Lcom/ss/android/ugc/aweme/main/ITabChangeManager
Lcom/ss/android/ugc/aweme/main/MainPageServiceImpl
Lcom/ss/android/ugc/aweme/main/MainPopViewTrigger
Lcom/ss/android/ugc/aweme/main/MainTabPreferences
Lcom/ss/android/ugc/aweme/main/FeedbackManager
Lcom/ss/android/ugc/aweme/main/KevaCleanAB
Lcom/ss/android/ugc/aweme/main/PrivacyPolicyDialog
Lcom/ss/android/ugc/aweme/main/landing/LandingType
Lcom/ss/android/ugc/aweme/main/BackToHomepageTrigger
Lcom/ss/android/ugc/aweme/main/IMainPageMobHelper
```

### 播放器
```
Lcom/ss/android/ugc/aweme/player/VideoPlayManager
Lcom/ss/android/ugc/aweme/player/PlayerHelper
Lcom/ss/android/ugc/aweme/player/sdk/api/IPlayer
Lcom/ss/android/ugc/aweme/player/sdk/api/IPlayable
Lcom/ss/android/ugc/aweme/player/sdk/api/INetClient
Lcom/ss/android/ugc/aweme/player/sdk/api/IReflect
Lcom/ss/android/ugc/aweme/player/sdk/api/SubTitle
Lcom/ss/android/ugc/aweme/player/sdk/PlayerLog
Lcom/ss/android/ugc/aweme/player/player/PageContext
Lcom/ss/android/ugc/aweme/player/player/LoadingState
Lcom/ss/android/ugc/aweme/player/player/ScaleType
Lcom/ss/android/ugc/aweme/player/player/SeekState
Lcom/ss/android/ugc/aweme/player/queue/PlayMode
Lcom/ss/android/ugc/aweme/player/queue/MediaType
Lcom/ss/android/ugc/aweme/player/queue/CacheReason
Lcom/ss/android/ugc/aweme/player/queue/PreloadType
Lcom/ss/android/ugc/aweme/player/queue/MediaQuality
Lcom/ss/android/ugc/aweme/player/ab/LaunchOpt
Lcom/ss/android/ugc/aweme/player/ab/PlayerLaunchOpt
Lcom/ss/android/ugc/aweme/player/ab/StringLaunchOpt
Lcom/ss/android/ugc/aweme/player/ab/PlayerLibLoadOpt
Lcom/ss/android/ugc/aweme/player/PlayerDebugView
Lcom/ss/android/ugc/aweme/player/TFEDelayCalculator
```

### 下载服务
```
Lcom/ss/android/ugc/aweme/download/XDownloadApi
Lcom/ss/android/ugc/aweme/download/XApkInfoModel
Lcom/ss/android/ugc/aweme/download/DownloadImageManager
Lcom/ss/android/ugc/aweme/download/SaveSettingsKevaService
Lcom/ss/android/ugc/aweme/download/NoticeDownloadRemindDialog
Lcom/ss/android/ugc/aweme/download/component_api/DownloadScene
Lcom/ss/android/ugc/aweme/download/component_api/AbsDownloadTask
Lcom/ss/android/ugc/aweme/download/component_api/DownloadServiceManager
Lcom/ss/android/ugc/aweme/download/component_api/service/IDownloadService
Lcom/ss/android/ugc/aweme/download/component_api/listener/IAutoUnzipListener
Lcom/ss/android/ugc/aweme/download/impl/component_impl/DownloadServiceImpl
Lcom/ss/android/ugc/aweme/download/component_api/ab/DownloadPathExperiment
Lcom/ss/android/ugc/aweme/download/DownloadImageManager$startDownload2$1
Lcom/ss/android/ugc/aweme/download/DownloadImageManager$startDownload3$1
Lcom/ss/android/ugc/aweme/download/DownloadImageManager$startDownload4$1
```

### 分享服务
```
Lcom/ss/android/ugc/aweme/share/ShareServiceImpl
Lcom/ss/android/ugc/aweme/share/ShareService
Lcom/ss/android/ugc/aweme/share/ShareActionImpl
Lcom/ss/android/ugc/aweme/share/ShareExtService
Lcom/ss/android/ugc/aweme/share/ShareChannelImpl
Lcom/ss/android/ugc/aweme/share/CommonSharePanel
Lcom/ss/android/ugc/aweme/share/MiscDownloadAddrs
Lcom/ss/android/ugc/aweme/share/utils/Wrapper
Lcom/ss/android/ugc/aweme/share/H5ShareActivity
Lcom/ss/android/ugc/aweme/share/ShareLinkParams
Lcom/ss/android/ugc/aweme/share/link/UrlShorter
Lcom/ss/android/ugc/aweme/share/DecorateQrChannel
Lcom/ss/android/ugc/aweme/share/ILiveShareService
Lcom/ss/android/ugc/aweme/share/IScreenshotAction
Lcom/ss/android/ugc/aweme/share/AwemeACLStruct
Lcom/ss/android/ugc/aweme/share/InnerImChannel
Lcom/ss/android/ugc/aweme/share/LocalQrChannel
Lcom/ss/android/ugc/aweme/share/command/Schema
Lcom/ss/android/ugc/aweme/share/command/BackPack
Lcom/ss/android/ugc/aweme/sharer/ShareContent
Lcom/ss/android/ugc/aweme/sharer/ui/SharePackage
Lcom/ss/android/ugc/aweme/sharer/Channel
Lcom/ss/android/ugc/aweme/sharer/AbstractChannel
Lcom/ss/android/ugc/aweme/sharer/ForwardChannel
Lcom/ss/android/ugc/aweme/sharer/IntentChannel
Lcom/ss/android/ugc/aweme/sharer/ui/SheetAction
Lcom/ss/android/ugc/aweme/sharer/ui/ITipParams
Lcom/ss/android/ugc/aweme/sharer/ext/QQChannel
Lcom/ss/android/ugc/aweme/sharer/ext/LarkChannel
Lcom/ss/android/ugc/aweme/share/MobEventMap
```

### 搜索
```
Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivity
Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivityForPad
Lcom/ss/android/ugc/aweme/search/activity/SearchPartShowResultActivity
Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivityTranslucent
Lcom/ss/android/ugc/aweme/search/activity/SearchResultForCommodityActivity
Lcom/ss/android/ugc/aweme/search/activity/SearchResultActivityForDialog
Lcom/ss/android/ugc/aweme/search/activity/SearchResultForCommodityActivity
Lcom/ss/android/ugc/aweme/search/common/activity/ECSearchActivity
Lcom/ss/android/ugc/aweme/search/common/activity/ECSearchStubActivity
Lcom/ss/android/ugc/aweme/search/vision/PhotoSearchActivity
Lcom/ss/android/ugc/aweme/search/vision/VideoSearchActivity
Lcom/ss/android/ugc/aweme/search/vision/CommoditySearchActivity
Lcom/ss/android/ugc/aweme/search/vision/OrderSearchMiddleActivity
Lcom/ss/android/ugc/aweme/search/visualsearch/VisualSearchActivity
Lcom/ss/android/ugc/aweme/search/visualsearch/VisualIntermediateActivity
Lcom/ss/android/ugc/aweme/search/discuss/DiscussSearchLandingActivity
Lcom/ss/android/ugc/aweme/search/halfscreen/SearchResultActivityForDialog
Lcom/ss/android/ugc/aweme/search/mob/CoinActivityFinishMobEvent
Lcom/ss/android/ugc/aweme/search/ab/LifeActivityPageSearchBarTabUiOptExp
Lcom/ss/android/ugc/aweme/searcharticle/detail/ArticleDetailActivity
```

### 评论
```
Lcom/ss/android/ugc/aweme/commentfeed/CommentFeedActivity
Lcom/ss/android/ugc/aweme/comment/ui/ImageDetailActivity
Lcom/ss/android/ugc/aweme/comment/ui/GifEmojiDetailActivity
Lcom/ss/android/ugc/aweme/comment/keyboard/refactor/evaluate/EvaluationKeyboardActivity
```

### Feed UI
```
Lcom/ss/android/ugc/aweme/feed/ui/BaseFeedListFragment
Lcom/ss/android/ugc/aweme/feed/ui/FeedRecommendFragment
Lcom/ss/android/ugc/aweme/feed/ui/FeedFollowFragment
Lcom/ss/android/ugc/aweme/feed/ui/FeedFamiliarFragment
Lcom/ss/android/ugc/aweme/feed/panel/BaseListFragmentPanel
Lcom/ss/android/ugc/aweme/feed/panel/FragmentPanel
Lcom/ss/android/ugc/aweme/feed/panel/IBaseListFragmentPanel
Lcom/ss/android/ugc/aweme/feed/ui/FeedActionSheet
Lcom/ss/android/ugc/aweme/feed/ui/VideoPlayActivity
Lcom/ss/android/ugc/aweme/feed/ui/FeedAvatarView
Lcom/ss/android/ugc/aweme/feed/ui/FeedShareIconView
Lcom/ss/android/ugc/aweme/feed/ui/FeedFollowUserBtn
Lcom/ss/android/ugc/aweme/feed/ui/CustomViewPager
Lcom/ss/android/ugc/aweme/feed/ui/LongPressLayout
Lcom/ss/android/ugc/aweme/feed/ui/AdCouponView
Lcom/ss/android/ugc/aweme/feed/ui/DebugInfoView
Lcom/ss/android/ugc/aweme/feed/ui/DragEndStatus
Lcom/ss/android/ugc/aweme/feed/ui/GroupedAvatars
Lcom/ss/android/ugc/aweme/feed/ui/HotSpotLabelAb
Lcom/ss/android/ugc/aweme/feed/ui/LiveCircleView
Lcom/ss/android/ugc/aweme/feed/ui/VcdTopFlowView
Lcom/ss/android/ugc/aweme/feed/ui/TreasureBoxView
Lcom/ss/android/ugc/aweme/feed/ui/RollingDigitView
Lcom/ss/android/ugc/aweme/feed/ui/RollingCoinNumber
Lcom/ss/android/ugc/aweme/feed/ui/SlideSwitchLayout
Lcom/ss/android/ugc/aweme/feed/ui/CountdownTextView
Lcom/ss/android/ugc/aweme/feed/ui/YearPickerDialog
Lcom/ss/android/ugc/aweme/feed/quick/QEntry
Lcom/ss/android/ugc/aweme/feed/quick/ICoexist
Lcom/ss/android/ugc/aweme/feed/quick/QIEntryModule
```

### 水印
```
Lcom/ss/ugc/aweme/WaterMarkInfo
Lcom/ss/android/ugc/aweme/detail/ab/DetailWaterMaskExp
```

### 详情页
```
Lcom/ss/android/ugc/aweme/detail/ui/DetailActivity
Lcom/ss/android/ugc/aweme/detail/ui/DetailFragment
Lcom/ss/android/ugc/aweme/detail/ui/FadeImageView
Lcom/ss/android/ugc/aweme/detail/ui/CircleProgressBar
Lcom/ss/android/ugc/aweme/detail/api/DetailApi
Lcom/ss/android/ugc/aweme/detail/model/FilterList
Lcom/ss/android/ugc/aweme/detail/model/SearchPOIModel
Lcom/ss/android/ugc/aweme/detail/model/VideoDiaryData
Lcom/ss/android/ugc/aweme/detail/model/BatchDetailList
Lcom/ss/android/ugc/aweme/detail/arch/DetailFeedRouter
Lcom/ss/android/ugc/aweme/detail/PlayletDetailActivity
Lcom/ss/android/ugc/aweme/detail/ab/DetailFeedPageSize
Lcom/ss/android/ugc/aweme/detail/ab/FixBottomViewState
Lcom/ss/android/ugc/aweme/detail/param/DetailFeedParam
Lcom/ss/android/ugc/aweme/detail/widget/HideReason
```

### 广告下载
```
Lcom/ss/android/ugc/aweme/ad/download/AdDownloadService
Lcom/ss/android/ugc/aweme/ad/download/api/IAdDownloadService
Lcom/ss/android/ugc/aweme/ad/download/api/IAdAppDownloadService
Lcom/ss/android/ugc/aweme/ad/download/AppDownloadServiceDelegate
Lcom/ss/android/ugc/aweme/ad/download/AppDownloadServiceDefaultImpl
Lcom/ss/android/ugc/aweme/ad/download/na/MangoReportTask
Lcom/ss/android/ugc/aweme/ad/download/settings/UseHttp1
```

### 其他下载
```
Lcom/ss/android/ugc/aweme/external/DownloadVideoServiceImpl
Lcom/ss/android/ugc/aweme/services/download/IDownloadVideoService
Lcom/ss/android/ugc/aweme/port/in/IDiagnoseVideoDownloadService
Lcom/ss/android/ugc/aweme/bridge/IMusicDownloaderService
Lcom/ss/android/ugc/aweme/download/SaveSettingsKevaService
Lcom/ss/android/ugc/aweme/download/NoticeDownloadRemindDialog
```

### 图片下载
```
Lcom/ss/android/ugc/aweme/image/ILightenDownloadImage
Lcom/ss/android/ugc/aweme/download/DownloadImageManager
Lcom/facebook/net/IDownloadImage
Lcom/bytedance/android/ec/host/api/fresco/DownloadImageCallback
```

---

## 混淆规律

1. **核心业务类保留**: Aweme, Video, MainActivity, DetailActivity 等核心类保留
2. **UI 辅助类混淆**: 部分 UI 辅助类使用混淆命名 (如 detail/widget/h)
3. **方法名保留**: 关键方法名 (play_addr, handleDiggClick, share) 保留
4. **包结构保留**: 包结构保持不变 (com.ss.android.ugc.aweme.*)
5. **内部类混淆**: Kotlin 编译的内部类使用混淆命名
6. **OkHttp 保留**: OkHttp 完整类名保留 (这是第三方库)

---

## 注意事项

- 核心业务类 (Aweme, Video, MainActivity, ...) 类名保留
- 辅助类和内部类可能混淆
- OkHttp 类名保留 (第三方库)
- 方法名保留 (如 play_addr, handleDiggClick)
- 使用运行时搜索 (DouyinFinder) 避免依赖混淆类名

---

**更新日期：** 2026-03-29
**版本：** v38.3.0
**来源：** APK DEX 文件字符串提取
