# 第一课：修改你的第一个页面

本课目标：你能找到页面代码，独立修改文字和字号，并解释哪个 `attr` 在设置哪个组件。

## 第一步：先确认运行结果

按项目 README 在 Android Studio 中选择 **KuiklyLearning - Full Install**，运行 `androidApp` 模块。初始示例会显示白底黑字“你好，Kuikly！”。如果你已经改过文字或颜色，应以你当前代码为准。

这行文字由 Kuikly 渲染。Android 的 `MainActivity` 只是承载它的容器。

## 第二步：找到页面文件

打开 `shared/src/commonMain/kotlin/com/example/kuiklylearning/pages/HelloWorldPage.kt`。

`shared` 表示共享模块；`commonMain` 放可共享的 Kotlin 代码。当前工程先编译 Android，后面接入其他平台时可以继续使用这里的页面。

先记住这四个词：

| 名称 | 你可以怎样理解 |
| --- | --- |
| `Pager` | 一个 Kuikly 页面的基础类；这里不是 Android 的 ViewPager |
| `body` | 描述页面里有什么 |
| `Text` | 在页面上放一段文字 |
| `attr` | 设置它所属组件的属性 |

## 第三步：只改欢迎语

找到：

```kotlin
text("你好，Kuikly！")
```

把引号里的内容改成你想说的一句话，例如：

```kotlin
text("今天开始学习 Kuikly")
```

点击 Android Studio 的 Run，重新编译安装。手机上应该出现新文字。

这里使用随 APK 编译的 JVM 模式；修改代码后需要重新运行应用，保存文件本身不会更新手机上的页面。

## 第四步：只改字号

找到 `fontSize(28f)`，改成 `fontSize(36f)`，再次运行。

`f` 是 Kotlin 的 Float 数字后缀。先观察字号变大的效果，不必急着记布局单位。

## 第五步：读懂两个 attr

最外层的 `attr` 属于页面：

```kotlin
attr {
    backgroundColor(Color.WHITE)
    allCenter()
}
```

它把页面背景设为白色，让里面的内容居中。

`Text` 里面的 `attr` 属于这段文字：

```kotlin
Text {
    attr {
        text("你好，Kuikly！")
        fontSize(28f)
        color(Color.BLACK)
    }
}
```

它设置文字内容、字号和颜色。读代码时沿着大括号向外看，判断这一组属性属于谁。

## 完成后回答

1. 要修改文字内容，改哪一行？
2. 要把文字居中，改页面的 `attr` 还是文字的 `attr`？
3. `@Page("HelloWorld")` 是显示给用户看的标题，还是供框架查找页面的名字？

把你的答案或修改后的代码发给我。下一课在这个页面上增加内容，学习组件如何排列。

## 遇到问题

- 改完手机没变化：选择 **KuiklyLearning - Full Install**，点击绿色三角形 Run；已有 `androidApp` 配置需勾选 **Always install with package manager**。等待安装完成后再看，详见[部署问题说明](troubleshooting-deployment.md)。
- 修改 `@Page` 名称后打不开：`MainActivity` 默认请求的名字也必须一致。第一课先保留 `HelloWorld`。
- Kotlin 文件里的 `Text` 报红：等 Gradle Sync 完成，确认导入的是 `com.tencent.kuikly.core.views.Text`。
- Java 版本错误：切换 Gradle JDK 为 17，或使用 `scripts/build.ps1` 自动选择本机 JDK。
- 下载依赖失败：检查 IDE/Gradle 的代理和网络；本项目没有写死代理地址。

参考：[官方第一个页面教程](https://kuikly.tds.qq.com/QuickStart/hello-world.html)、[组件属性说明](https://kuikly.tds.qq.com/DevGuide/attr.html)。
