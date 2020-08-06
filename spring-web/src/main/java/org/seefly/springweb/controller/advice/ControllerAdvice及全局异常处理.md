# ControllerAdvice的使用及原理
>  [原文](https://zhuanlan.zhihu.com/p/73087879?from_voters_page=true)

## 主要用法
### 1、用于全局异常捕获
将`@ControllerAdvice`注解在类上，并且在方法上使用`@ExceptionHandler`，表示当前方法可以处理指定类型的全局异常
详细见 `GlobaleExceptionHandler`

### 2、参数格式化
详见`DataBinderHandler`

### 3、@ModelAttribute