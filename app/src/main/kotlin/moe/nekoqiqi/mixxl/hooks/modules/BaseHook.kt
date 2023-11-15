package moe.nekoqiqi.mixxl.hooks.modules

abstract class BaseHook {
    var isInit: Boolean = false
    abstract fun init()
}
