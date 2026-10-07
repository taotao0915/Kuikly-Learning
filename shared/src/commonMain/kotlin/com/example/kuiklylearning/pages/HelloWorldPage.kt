package com.example.kuiklylearning.pages

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.pager.Pager
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

// Android 宿主通过 HelloWorld 这个名字打开页面。
@Page("HelloWorld")
internal class HelloWorldPage : Pager() {

    // 页面状态：初始为 0。修改它后，读取它的响应式属性会更新。
    private var clickCount by observable(0)

    // 函数定义：把“次数加一”这项操作放在一起，调用时才执行。
    private fun incrementCount() {
        clickCount += 1
    }

    // 函数定义：把次数直接设为 0。计数文字会跟随状态更新。
    private fun resetCount() {
        clickCount = 0
    }

    // 本节：在点击事件中调用函数，分别完成计数和重置。
    override fun body(): ViewBuilder {
        // 保存当前页面的引用，方便在嵌套的组件、属性和事件代码块里访问页面状态。
        val page = this
        return {
            // 页面包含欢迎语、昵称卡片、计数文字和重置按钮。
            attr {
                backgroundColor(Color.WHITE)
                flexDirectionColumn() // 把页面中的组件从上往下排列。
                allCenter() // 让整组内容在页面中水平、垂直居中。
            }

            // 第一个组件：欢迎语。
            Text {
                attr {
                    text("你好，Kuikly！")
                    fontSize(28f)
                    color(Color.BLACK)
                }
            }

            // 第二个组件：灰色昵称卡片，内部的两个 Text 仍然左右排列。
            View {
                // 这里的 attr 设置昵称容器，不会改变外层页面的排列方向。
                attr {
                    width(280f) // 卡片宽度，包含左右内边距。
                    height(100f) // 卡片高度，包含上下内边距；不会改变文字字号。
                    flexDirectionRow() // 横排：主轴是水平方向，交叉轴是竖直方向。
                    justifyContentCenter() // 主轴居中：两个 Text 连同中间的间距作为一整组水平居中。
                    alignItemsCenter() // 交叉轴居中：每个 Text 在竖直方向居中对齐。
                    backgroundColor(Color.GRAY) // 灰色背景覆盖容器内部，包括 padding 区域。
                    padding(16f) // 在指定宽高内部四周各留 16f，内容可用区域为 248 × 68。
                    marginTop(24f) // 外边距：容器与上方欢迎语的间距，不属于灰色背景。
                }

                // event 与 attr 并列：attr 设置组件属性，event 登记事件发生后要执行的代码。
                event {
                    click {
                        page.incrementCount() // 函数调用：点击后执行“次数加一”。
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
                    text("已点击卡片 ${page.clickCount} 次")
                    fontSize(18f)
                    color(Color.BLACK)
                    marginTop(16f) // 计数文字与上方昵称容器的间距。
                }
            }

            // 第四个组件：始终显示的重置按钮，由 View 和 Text 组成。
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
                        page.resetCount() // 函数调用：点击后执行“次数归零”。
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
