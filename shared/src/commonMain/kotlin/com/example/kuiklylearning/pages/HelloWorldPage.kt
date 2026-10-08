package com.example.kuiklylearning.pages

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Animation
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.pager.Pager
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

// Android 宿主通过 HelloWorld 这个名字打开页面。
@Page("HelloWorld")
internal class HelloWorldPage : Pager() {

    // 练习目标：以后调整完成门槛，只需要修改这里，再重新运行。
    // val 表示不能在运行时重新赋值；private 表示只在当前页面内部使用。
    private val targetCount = 5

    // 页面状态：初始为 0。修改它后，读取它的响应式属性会更新。
    private var clickCount by observable(0)

    // 计算属性：每次读取时，都用当前次数计算“是否完成”，不单独保存另一份状态。
    // Boolean 的值是 true 或 false；val 表示不能直接给 isCompleted 赋值。
    private val isCompleted: Boolean
        get() = clickCount >= targetCount

    // 进度条的总宽度，外层底条和内层填充共用这个值。
    private val progressBarWidth = 280f

    // 完成比例：目标次数保持为正整数。计数从 0 增加到目标，比例就从 0f 增加到 1f。
    // 先转成 Float 再除：1 / 5 是整数 0，而 1.toFloat() / 5 约为 0.2。
    private val progress: Float
        get() = clickCount.toFloat() / targetCount

    // 点击时先检查是否完成，达到目标后不再增加次数。
    private fun incrementCount() {
        // 普通 if 控制这次函数调用的执行流程；isCompleted 会按当前次数重新计算。
        if (isCompleted) {
            return // 提前结束 incrementCount()，本次不再执行下面的加一操作。
        }

        // 尚未完成才会走到这里。例如目标为 5，当前为 4 时，可以加到 5。
        clickCount += 1
    }

    // 函数定义：把次数直接设为 0。计数文字、卡片颜色、进度条和按钮会跟随状态更新。
    private fun resetCount() {
        clickCount = 0
    }

    // 本节：给进度条绑定声明式动画；状态决定目标效果，Animation 决定变化过程。
    override fun body(): ViewBuilder {
        // 保存当前页面的引用，方便在嵌套的组件、属性和事件代码块里访问页面状态。
        val page = this
        return {
            // 页面包含欢迎语、昵称卡片、计数文字、进度条，以及完成后才显示的重置按钮。
            attr {
                backgroundColor(Color.WHITE)
                flexDirectionColumn() // 把页面中的组件从上往下排列。
                allCenter() // 整组内容居中；按钮出现或移除后，会按新的总高度重新居中。
            }

            // 第一个组件：欢迎语。
            Text {
                attr {
                    text("你好，Kuikly！")
                    fontSize(28f)
                    color(Color.BLACK)
                }
            }

            // 第二个组件：昵称卡片，达到目标后变绿，内部的两个 Text 仍然左右排列。
            View {
                // 这里的 attr 设置昵称容器，不会改变外层页面的排列方向。
                attr {
                    width(280f) // 卡片宽度，包含左右内边距。
                    height(100f) // 卡片高度，包含上下内边距；不会改变文字字号。
                    flexDirectionRow() // 横排：主轴是水平方向，交叉轴是竖直方向。
                    justifyContentCenter() // 主轴居中：两个 Text 连同中间的间距作为一整组水平居中。
                    alignItemsCenter() // 交叉轴居中：每个 Text 在竖直方向居中对齐。
                    // 读取 isCompleted 时会执行 getter，间接读取 clickCount，颜色仍能响应次数变化。
                    backgroundColor(if (page.isCompleted) Color.GREEN else Color.GRAY)
                    padding(16f) // 在指定宽高内部四周各留 16f，内容可用区域为 248 × 68。
                    marginTop(24f) // 外边距：容器与上方欢迎语的间距，不属于卡片背景。
                }

                // event 与 attr 并列：attr 设置组件属性，event 登记事件发生后要执行的代码。
                event {
                    click {
                        page.incrementCount() // 函数内部先检查完成状态，未完成时才加一。
                    }
                }

                Text {
                    attr {
                        text("昵称：")
                        fontSize(20f)
                        color(Color.BLACK)
                    }
                }

                Text {
                    // 这里的 attr 只设置昵称文字。
                    attr {
                        text("Kuikly学习者")
                        fontSize(20f)
                        color(Color.BLACK)
                        marginLeft(12f) // 昵称文字与左侧“昵称：”之间的间距。
                    }
                }
            }

            // 第三个组件：计数文字。它在昵称 View 的大括号外，属于页面。
            Text {
                attr {
                    // 在 attr 内读取状态，Kuikly 才能跟踪这个属性块对状态的依赖。
                    text(
                        if (page.isCompleted) {
                            "练习完成！已点击 ${page.clickCount} 次"
                        } else {
                            "当前进度：${page.clickCount} / ${page.targetCount} 次"
                        }
                    )
                    fontSize(18f)
                    color(Color.BLACK)
                    marginTop(16f) // 计数文字与上方昵称容器的间距。
                }
            }

            // 第四部分：灰色底条表示总目标，里面的彩色填充表示已完成的比例。
            View {
                attr {
                    width(page.progressBarWidth)
                    height(12f)
                    flexDirectionRow() // 内部填充从左侧开始排列，不设置 allCenter()。
                    backgroundColor(Color.GRAY)
                    marginTop(12f)
                }

                View {
                    attr {
                        // 状态决定最终宽度；animate 决定旧效果如何过渡到新效果。
                        width(page.progressBarWidth * page.progress)
                        height(12f)
                        backgroundColor(if (page.isCompleted) Color.GREEN else Color.BLUE)

                        // 将动画关联到响应式状态 clickCount，次数变化时对填充块应用动画。
                        // easeOut：先快后慢；0.3f 的单位是秒。放在属性设置后，让宽度和背景色平滑变化。
                        // 计数本身立即更新；重置为 0 时，填充也会逐渐缩回去。
                        animate(Animation.easeOut(0.3f), page.clickCount)
                    }
                }
            }

            // 第五部分：达到目标才显示整个重置按钮，与颜色、文字共用 isCompleted 判断。
            // vif 的第一个代码块提供判断条件，第二个代码块负责创建按钮。
            // 在条件块中读取计算属性；它的 getter 会读取 observable 状态，次数变化时会重新判断。
            vif({ page.isCompleted }) {
                // 条件成立时创建 View；不成立时移除它，包括占用的布局空间。
                View {
                    attr {
                        width(160f)
                        height(48f)
                        backgroundColor(Color.BLUE)
                        allCenter() // 让按钮内的文字水平、竖直居中。
                        marginTop(16f)
                    }

                    event {
                        click {
                            // 归零后，完成提示和绿色背景恢复，vif 的条件变为 false，按钮被移除。
                            page.resetCount()
                        }
                    }

                    Text {
                        attr {
                            text("重新开始")
                            fontSize(18f)
                            color(Color.WHITE)
                        }
                    }
                }
            }
        }
    }
}
