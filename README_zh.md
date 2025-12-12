![Now in Android](docs/images/nia-splash.jpg "Now in Android")

<a href="https://play.google.com/store/apps/details?id=com.google.samples.apps.nowinandroid"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>

Now in Android 应用
==================

**了解此应用的设计和构建方式，请参阅[设计案例研究](https://goo.gle/nia-figma)、[架构学习之旅](docs/ArchitectureLearningJourney.md)和[模块化学习之旅](docs/ModularizationLearningJourney.md)。**

这是 [Now in Android](https://developer.android.com/series/now-in-android) 应用的代码仓库。它是一个**正在进行中的项目** 🚧。

**Now in Android** 是一个功能完整的 Android 应用，完全使用 Kotlin 和 Jetpack Compose 构建。它遵循 Android 设计和开发的最佳实践，旨在为开发者提供有用的参考。作为一款运行中的应用，它通过提供定期的新闻更新，帮助开发者跟上 Android 开发领域的最新动态。

该应用目前正在开发中。`prodRelease` 版本已在 [Play Store 上架](https://play.google.com/store/apps/details?id=com.google.samples.apps.nowinandroid)。

# 功能特性

**Now in Android** 显示来自 [Now in Android](https://developer.android.com/series/now-in-android) 系列的内容。用户可以浏览最近的视频、文章和其他内容的链接。用户还可以关注他们感兴趣的主题，并在发布符合其关注兴趣的新内容时收到通知。

## 截图

![显示"For You"界面、"Interests"界面和"Topic detail"界面的截图](docs/images/screenshots.png "显示"For You"界面、"Interests"界面和"Topic detail"界面的截图")

# 开发环境

**Now in Android** 使用 Gradle 构建系统，可以直接导入到 Android Studio 中（请确保您使用的是[此处](https://developer.android.com/studio)提供的最新稳定版本）。

将运行配置更改为 `app`。

![image](https://user-images.githubusercontent.com/873212/210559920-ef4a40c5-c8e0-478b-bb00-4879a8cf184a.png)

可以构建和运行 `demoDebug` 和 `demoRelease` 构建变体（`prod` 变体使用后端服务器，目前不公开提供）。

![image](https://user-images.githubusercontent.com/873212/210560507-44045dc5-b6d5-41ca-9746-f0f7acf22f8e.png)

运行起来后，您可以参考以下学习之旅，更好地了解所使用的库和工具、UI、测试、架构等方法背后的原因，以及项目中所有这些不同部分如何组合在一起形成一个完整的应用程序。

# 架构

**Now in Android** 应用遵循 [官方架构指南](https://developer.android.com/topic/architecture)，并在 [架构学习之旅](docs/ArchitectureLearningJourney.md) 中有详细描述。

# 模块化

**Now in Android** 应用已完全模块化，您可以在 [模块化学习之旅](docs/ModularizationLearningJourney.md) 中找到详细的指导和所用模块化策略的描述。

# 构建

应用包含常规的 `debug` 和 `release` 构建变体。

此外，`app` 的 `benchmark` 变体用于测试启动性能并生成基线配置文件（详见下文）。

`app-nia-catalog` 是一个独立的应用程序，显示为 **Now in Android** 定制的组件列表。

该应用还使用 [产品风格](https://developer.android.com/studio/build/build-variants#product-flavors) 来控制应用内容的加载来源。

`demo` 风格使用静态本地数据，允许立即构建和探索 UI。

`prod` 风格向后端服务器发出真实的网络调用，提供最新的内容。目前没有公开的后端可用。

正常开发时使用 `demoDebug` 变体。进行 UI 性能测试时使用 `demoRelease` 变体。

# 测试

为了便于组件测试，**Now in Android** 使用带有 [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) 的依赖注入。

大多数数据层组件被定义为接口。然后，具体的实现（具有各种依赖关系）被绑定以向应用中的其他组件提供这些接口。在测试中，**Now in Android** 明确不使用任何模拟库。相反，生产实现可以通过 Hilt 的测试 API（或通过手动构造函数注入进行 `ViewModel` 测试）替换为测试替身。

这些测试替身实现了与生产实现相同的接口，并通常提供简化的（但仍现实的）实现以及额外的测试钩子。这导致了不那么脆弱的测试，可能执行更多的生产代码，而不仅仅是验证针对模拟对象的特定调用。

示例：
- 在仪器测试中，使用临时文件夹存储用户的偏好设置，每次测试后都会清除。
  这样就可以使用真正的 `DataStore` 并执行所有相关代码，而不是模拟数据更新流。

- 每个存储库都有 `Test` 实现，它们实现了正常的完整存储库接口，同时还提供仅限测试的钩子。
  `ViewModel` 测试使用这些 `Test` 存储库，因此可以使用仅限测试的钩子来操作 `Test` 存储库的状态并验证结果行为，而不是检查是否调用了特定的存储库方法。

要运行测试，请执行以下 gradle 任务：

- `testDemoDebug` 对 `demoDebug` 变体运行所有本地测试。屏幕截图测试将失败（见下文解释）。为避免这种情况，请在运行单元测试之前运行 `recordRoborazziDemoDebug`。
- `connectedDemoDebugAndroidTest` 对 `demoDebug` 变体运行所有仪器测试。

> [!NOTE]
> 您不应运行 `./gradlew test` 或 `./gradlew connectedAndroidTest`，因为这将在_所有_构建变体上执行测试，这既不必要又会导致失败，因为只支持 `demoDebug` 变体。其他变体没有任何测试（尽管将来可能会改变）。

## 屏幕截图测试
屏幕截图测试会对应用中的屏幕或 UI 组件进行截图，并将其与先前记录的正确渲染的屏幕截图进行比较。

例如，Now in Android 有[屏幕截图测试](https://github.com/android/nowinandroid/blob/main/app/src/testDemo/kotlin/com/google/samples/apps/nowinandroid/ui/NiaAppScreenSizesScreenshotTests.kt)来验证导航在不同屏幕尺寸上正确显示（[已知正确的屏幕截图](https://github.com/android/nowinandroid/tree/main/app/src/testDemo/screenshots)）。

Now In Android 使用 [Roborazzi](https://github.com/takahirom/roborazzi) 来运行某些屏幕和 UI 组件的屏幕截图测试。处理屏幕截图测试时，以下 gradle 任务很有用：

- `verifyRoborazziDemoDebug` 运行所有屏幕截图测试，将屏幕截图与已知的正确屏幕截图进行验证。
- `recordRoborazziDemoDebug` 记录新的"已知正确"屏幕截图。当您对 UI 进行更改并手动验证其正确渲染时，请使用此命令。屏幕截图将存储在 `modulename/src/test/screenshots` 中。
- `compareRoborazziDemoDebug` 在失败的测试和已知的正确图像之间创建比较图像。这些也可以在 `modulename/src/test/screenshots` 中找到。

> [!NOTE]
> **关于失败的屏幕截图测试的说明**
> 此仓库中存储的已知正确屏幕截图是在 CI 上使用 Linux 记录的。其他平台可能会（而且可能会）生成略有不同的图像，导致屏幕截图测试失败。
> 在非 Linux 平台上工作时，解决方法是在开始工作前在 `main` 分支上运行 `recordRoborazziDemoDebug`。进行更改后，`verifyRoborazziDemoDebug` 将仅识别合法的更改。

有关屏幕截图测试的更多信息，请[查看此演讲](https://www.droidcon.com/2023/11/15/easy-screenshot-testing-with-compose/)。

# 用户界面
该应用使用 [Material 3 指南](https://m3.material.io/)进行设计。了解更多设计过程并获取设计文件，请参阅 [Now in Android Material 3 案例研究](https://goo.gle/nia-figma)（设计资源[也可作为 PDF 获取](docs/Now-In-Android-Design-File.pdf)）。

屏幕和 UI 元素完全使用 [Jetpack Compose](https://developer.android.com/jetpack/compose) 构建。

该应用有两个主题：

- 动态色彩 - 使用基于[用户当前色彩主题](https://material.io/blog/announcing-material-you)的颜色（如果支持）
- 默认主题 - 在不支持动态色彩时使用预定义颜色

每个主题都支持深色模式。

该应用使用自适应布局来[支持不同的屏幕尺寸](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes)。

在此了解有关 [UI 架构的更多信息](docs/ArchitectureLearningJourney.md#ui-layer)。

# 性能

## 基准测试

在 `benchmarks` 模块中找到所有使用 [`Macrobenchmark`](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview) 编写的测试。该模块还包含生成基线配置文件的测试。

## 基线配置文件

此应用的基线配置文件位于 [`app/src/main/baseline-prof.txt`](app/src/main/baseline-prof.txt)。
它包含启用应用启动期间关键用户路径 AOT 编译的规则。
有关基线配置文件的更多信息，请阅读[此文档](https://developer.android.com/studio/profile/baselineprofiles)。

> [!NOTE]
> 对于涉及更改应用启动代码的发布版本，需要重新生成基线配置文件。

要生成基线配置文件，请选择 `benchmark` 构建变体并在 AOSP Android 模拟器上运行 `BaselineProfileGenerator` 基准测试。
然后将生成的基线配置文件从模拟器复制到 [`app/src/main/baseline-prof.txt`](app/src/main/baseline-prof.txt)。

## Compose 编译器指标

运行以下命令获取并分析 compose 编译器指标：

```bash
./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true
```

报告文件将添加到 [build/compose-reports](build/compose-reports)。指标文件也将添加到 [build/compose-metrics](build/compose-metrics)。

有关 Compose 编译器指标的更多信息，请参阅[此博客文章](https://medium.com/androiddevelopers/jetpack-compose-stability-explained-79c10db270c8)。

# 许可证

**Now in Android** 根据 Apache 许可证（版本 2.0）分发。有关更多信息，请参阅[许可证](LICENSE)。