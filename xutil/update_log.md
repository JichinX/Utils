###v1.0.5
>1. 修改 DownLoadTool下载过程中 进度Dialog不显示问题
>2. 添加 SuperActivity添加跳转时 携带参数的startActivtyForResult跳转

###v1.0.4
>1. 优化 代码编译由Android studio3.0 金丝雀版 将至 2.3.3稳定版
>2. 优化 对自定义ActionBar，将控件提供暴漏接口，方便自定义处理
>3. 优化 取消下载时不必要的log输出
>4. 优化 取消自定义ActionBar，对4.4-5.0系统进行状态栏透明的处理，避免，软键盘弹出造成的各种不适情况

###v1.0.3
>1. 添加 下载逻辑重新编写，使用Retrofit中的@Streaming操作符，避免下载大文件导致OOM
