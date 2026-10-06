# KuiklyLearning

这是用于逐步学习 Kuikly 的独立 Android 工程。当前示例包含欢迎语、昵称卡片布局和点击计数。

页面使用 Kuikly 自研 DSL。项目采用 Kotlin Multiplatform 的目录结构，当前只启用 Android 目标，方便在 Windows 上开始学习。iOS、鸿蒙、Web 宿主尚未接入。

## 先运行

1. 在 Android Studio 中选择 **File → Open**，打开刚克隆的仓库根目录。该目录直接包含 `settings.gradle.kts`、`androidApp/` 和 `shared/`，无需再进入同名子目录。
2. 在 **Settings → Build, Execution, Deployment → Build Tools → Gradle** 中，把 Gradle JDK 设为 **JDK 17**。
3. 这台电脑可以使用：`D:\Android\Other AS Version\android-studio-2023.2.1.14-windows\android-studio\jbr`。
4. 等待 Gradle Sync 完成，选择 **KuiklyLearning - Full Install** 和你的 Android 手机或模拟器，点击绿色三角形 Run。这个配置会构建并完整覆盖安装，保留应用数据。
5. 页面显示欢迎语、灰色昵称卡片和点击次数；点击卡片，次数会增加。

若没有运行配置：Run → Edit Configurations → + → Android App，Module 选择 `KuiklyLearning.androidApp`，Launch 选择 Default Activity，并勾选 **Always install with package manager**。已有的 `androidApp` 配置也可以直接勾选这个选项；保持 Clear app storage 关闭。

首次同步需要联网下载依赖。已配置 Google Maven、Maven Central 和腾讯 Kuikly 官方 Maven；不需要安装 Kuikly IDE 插件，也不需要额外启动前端服务。

## 用命令行构建

在本目录打开 PowerShell：

```powershell
# 自动寻找本机 JDK 与 Android SDK，并编译 APK
.\scripts\build.ps1

# 编译并运行 Android Lint 检查
.\scripts\build.ps1 -Action verify

# 安装到已连接的 Android 设备
.\scripts\build.ps1 -Action install
```

脚本只在本次运行中设置环境变量，不修改电脑的全局 Java 配置。换电脑时可以传入 `-JdkHome` 和 `-SdkHome`；Android SDK 需要安装 API 35。

如果终端已有 JDK 17，也可以直接运行：

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

APK 输出：`androidApp/build/outputs/apk/debug/androidApp-debug.apk`。

## 主要学习文件

打开 [HelloWorldPage.kt](shared/src/commonMain/kotlin/com/example/kuiklylearning/pages/HelloWorldPage.kt)。从这个页面学习页面注册、布局属性、嵌套容器、点击事件与 `observable` 状态更新。

详细操作见 [第一课：修改你的第一个页面](docs/01-first-page.md)。

## 项目结构

```text
KuiklyLearning/
├── shared/                   我们写 Kuikly 页面和业务逻辑的地方
│   └── src/commonMain/kotlin/com/example/kuiklylearning/pages/
│       └── HelloWorldPage.kt 当前学习页面
├── androidApp/               Android 启动入口与渲染器接入
│   └── src/main/kotlin/com/example/kuiklylearning/
│       ├── MainActivity.kt   创建容器，打开 HelloWorld 页面
│       ├── LearningApplication.kt  初始化日志、线程、页面路由等能力
│       └── LearningImageAdapter.kt 后面 Image 课程需要的图片加载器
├── docs/                     中文课程说明
├── scripts/build.ps1         本机命令行构建入口
├── gradle.properties         Kuikly 版本统一配置
└── settings.gradle.kts       模块和依赖仓库配置
```

启动顺序：Android 打开 `MainActivity` → 容器请求 `HelloWorld` → KSP 生成的入口找到 `@Page("HelloWorld")` → Kuikly 执行页面代码并渲染文字。

`shared/build/generated/ksp/` 中的入口是编译器生成的，不要手动编辑。我们新增页面时使用不同的 `@Page` 名称，重新构建即可注册。

## 构建验证记录

2026-09-09 在本机通过 `scripts/build.ps1 -Action verify` 完成：

- `:androidApp:assembleDebug` 成功，生成约 4.8 MB 的 APK。
- `:androidApp:lintDebug` 完成，0 个错误、4 条警告：目标 SDK 和两项 AndroidX 依赖的版本提示，以及备份规则配置提示。当前保留这套已通过构建的版本组合。
- KSP 已生成 `KuiklyCoreEntry`，其中正确注册了 `HelloWorld` 页面。

2026-09-16 已在连接的 Android 12 手机上验证覆盖更新：不卸载、不清除数据，完整安装 Android Studio 已构建的最新 APK 后，页面显示学习者修改的红色文字。手机的首次安装时间保持不变，更新时间正常变化。

## 修改页面后仍显示旧内容

本机已遇到 Android Studio 的 `overlayinstall` 补丁部署未生效：新 APK 包含新文字，手机仍显示旧文字。使用 **KuiklyLearning - Full Install** 配置运行，可以改走系统包管理器完整覆盖安装。这个配置保存在 `.run/KuiklyLearning-Full-Install.run.xml`。

具体排查证据、现有运行配置的修改方法和命令行备用办法，见 [修改代码不生效的处理](docs/troubleshooting-deployment.md)。学习阶段用绿色三角形 Run 验证修改。

## 当前版本

| 工具或依赖 | 版本 |
| --- | --- |
| Kuikly | 2.27.0-2.1.21 |
| Kotlin | 2.1.21 |
| KSP | 2.1.21-2.0.1 |
| Android Gradle Plugin | 8.7.3 |
| Gradle Wrapper | 8.9 |
| 推荐构建 JDK | 17 |
| compileSdk / targetSdk | 35 / 35 |
| minSdk | 23（Android 6.0） |

Kuikly 版本后缀 `2.1.21` 对应 Kotlin 版本。`core`、`core-render-android`、`core-annotations` 和 `core-ksp` 均读取同一个 `kuiklyVersion`，避免版本混用。

## 后面的学习顺序

1. 第一页：理解 `Pager`、`body`、`Text`、`attr`，修改文字和字号。
2. 资料卡片：认识 `View`、`Image`、排列、边距和颜色。
3. 计数器：点击事件与 `observable` 数据更新。
4. 待办列表：列表、条件显示、新增和删除。
5. 详情页面：跳转、参数和生命周期。
6. 网络数据：加载、成功和失败状态。
7. 阅读源码：跟踪一次页面创建和一次响应式更新。

当前代码已实现基础布局和点击计数，后续课程继续在这个工程中添加。

## 官方参考

- [第一个 Kuikly 页面](https://kuikly.tds.qq.com/QuickStart/hello-world.html)
- [KMP 工程接入](https://kuikly.tds.qq.com/QuickStart/common.html)
- [Android 渲染器接入](https://kuikly.tds.qq.com/QuickStart/android.html)
- [官方框架仓库](https://github.com/Tencent-TDS/KuiklyUI)
- [官方依赖仓库](https://mirrors.tencent.com/repository/maven-tencent/com/tencent/kuikly-open/core/maven-metadata.xml)

工程接入依据官方说明和发布版本的 API 编写；框架通过 Maven 依赖使用，不需要编译整个 KuiklyUI 源码仓库。学习时下载的框架参考源码可放在被 Git 忽略的 `.reference/` 中，它不参与工程构建。
