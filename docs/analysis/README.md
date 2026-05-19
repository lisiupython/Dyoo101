# Dyoo Hook 点分析

## 目录结构

```
docs/analysis/
├── v38.3.0/                  - 抖音 v38.3.0 版本分析 (当前版本)
│   ├── HOOK_POINTS.md        - 所有 Hook 点详细分析
│   ├── VERSION_INFO.md       - 版本信息
│   ├── obfuscated_classes.md - 已知混淆类名
│   └── compatibility.md      - 版本兼容性指南
├── v34.6.0/                  - 旧版本 (可删除)
├── v35.0.0/                  - 未来版本
├── v36.0.0/                  - 未来版本
└── ...
```

## 使用说明

1. **查看当前版本分析**: 打开对应版本文件夹
2. **版本对比**: 使用 Git diff 或对比工具查看变化
3. **更新 Hook 点**: 在对应版本文件夹中更新 HOOK_POINTS.md
4. **运行时搜索**: 使用 DouyinFinder 进行运行时类搜索

## 分析方法

| 方法 | 说明 | 工具 |
|------|------|------|
| 静态分析 | 提取 DEX 文件字符串 | strings 命令 |
| 动态分析 | 运行时搜索混淆类 | DouyinFinder |
| 开源对比 | 参考其他 Xposed 模块 | GitHub |
| 日志分析 | 查看运行时日志 | Logcat |

## 注意事项

- 核心业务类 (Aweme, Video, MainActivity, ...) 类名保留
- 辅助类和内部类可能混淆
- OkHttp 类名保留 (第三方库)
- 使用运行时搜索 (DouyinFinder) 避免依赖混淆类名
- 定期更新分析结果

---

**维护者：** Dyoo 项目
**最后更新：** 2026-03-29
