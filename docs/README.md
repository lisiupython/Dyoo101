# Dyoo 文档

## 目录结构

```
docs/
├── analysis/               - 抖音版本分析和 Hook 点
│   ├── v34.6.0/           - v34.6.0 版本分析
│   └── README.md          - 分析文档说明
├── HOOK_ANALYSIS.md        - Hook 点分析总结
├── build.md               - 构建说明
└── usage.md               - 使用说明
```

## 快速开始

1. **查看 Hook 点分析**: [analysis/v34.6.0/HOOK_POINTS.md](analysis/v34.6.0/HOOK_POINTS.md)
2. **了解兼容性**: [analysis/v34.6.0/compatibility.md](analysis/v34.6.0/compatibility.md)
3. **构建项目**: [build.md](build.md)
4. **使用模块**: [usage.md](usage.md)

## 主要功能

| 功能 | 文件 | 说明 |
|------|------|------|
| 视频下载 | VideoHook.kt | 捕获视频 URL |
| 图片下载 | ImageHook.kt | 捕获图片 URL |
| 去水印 | WatermarkHook.kt | 移除水印参数 |
| 分享解锁 | PopupHook.kt | 解锁分享限制 |
| 清爽模式 | CleanModeHook.kt | 隐藏 UI 组件 |
| 悬浮窗 | PopupHook.kt | 快捷操作按钮 |

## 贡献

如需更新 Hook 点分析：
1. fork 项目
2. 在对应版本文件夹中更新分析
3. 提交 PR

---

**维护者：** Dyoo 项目
**最后更新：** 2026-03-29
