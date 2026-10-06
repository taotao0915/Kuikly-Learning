# 修改代码后，手机仍显示旧内容

## 已确认的现象

2026-09-16，本机 Android Studio Panda 4 在 Android 12 真机上运行本项目时出现以下现象：

- `HelloWorldPage.kt` 已修改文字和颜色。
- Android Studio 新生成的 APK 中已包含修改后的文字，说明编译阶段已经生效。
- 手机仍显示旧的黑色文字。
- Android Studio 日志记录了 `Deploying with optimistic install` 和 `Installer request:overlayinstall`，即使用补丁部署。
- 改用系统包管理器完整覆盖安装，再重新启动后，新文字和红色立即生效。整个过程没有卸载，也没有清除应用数据；手机记录的首次安装时间未变。

因此，这次故障定位在优化部署路径。还不能仅凭这些证据判断具体是 IDE、设备系统还是框架与补丁部署的兼容问题；不需要修改页面逻辑或关闭 Gradle 增量编译。

## 以后怎样运行

推荐在 Android Studio 顶部运行配置下拉框中选择 **KuiklyLearning - Full Install**，然后点击绿色三角形 Run。

这个共享配置位于 `.run/KuiklyLearning-Full-Install.run.xml`，它做三件事：

1. 编译 `androidApp` 及它依赖的 `shared`。
2. 通过系统包管理器覆盖安装完整 APK。
3. 重新启动应用。

它不卸载应用，也不开启 Clear app storage。安装可能比补丁部署稍慢。

如果继续使用原来的 `androidApp` 配置：

1. 打开 **Run → Edit Configurations**。
2. 选中 `androidApp`。
3. 在安装选项中勾选 **Always install with package manager**（部分版本后面还有 “disables deploy optimizations on Android 11 and later”）。
4. 保持 **Clear app storage** 关闭，点击 Apply / OK。
5. 用绿色三角形 Run 重新运行。

不需要每次卸载，也不需要每次 Clean Project。这里使用随 APK 编译的 JVM 页面，学习阶段先用完整 Run 验证修改。

## 命令行备用方法

在项目根目录运行：

```powershell
.\scripts\build.ps1 -Action install
```

脚本使用 Gradle `installDebug` 完成构建和覆盖安装。安装后，从手机桌面重新打开 KuiklyLearning。若连接了多台设备，请只保留本次学习用的设备。

## 官方说明

Android 官方说明：Android 11 及以后版本的优化部署可能使应用运行旧代码，例如两次部署之间清除应用数据的情况；**Always install with package manager** 会跳过这些优化，确保运行时部署最新代码。这里没有证据表明你清除了数据，只使用同一选项绕过已经观察到的补丁部署问题。

来源：[Android Studio：构建和运行应用](https://developer.android.com/studio/run)。
