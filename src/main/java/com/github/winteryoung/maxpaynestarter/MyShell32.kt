package com.github.winteryoung.maxpaynestarter

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.win32.StdCallLibrary

/**
 * @author Winter Young
 * @since 2016/3/6
 */
interface MyShell32 : StdCallLibrary {
    class ShellExecInfo() : Structure() {
        override fun getFieldOrder(): MutableList<Any?>? {
            return arrayListOf(
                    "cbSize",
                    "fMask",
                    "hwnd",
                    "lpVerb",
                    "lpFile",
                    "lpParameters",
                    "lpDirectory",
                    "nShow",
                    "hInstApp",
                    "lpIDList",
                    "lpClass",
                    "hkeyClass",
                    "dwHotKey",
                    "hIcon",
                    "hProcess"
            )
        }

        @JvmField
        var cbSize = size()
        @JvmField
        var fMask: Int = 0
        @JvmField
        var hwnd: Pointer? = null
        @JvmField
        var lpVerb: String? = null
        @JvmField
        var lpFile: String? = null
        @JvmField
        var lpParameters: String? = null
        @JvmField
        var lpDirectory: String? = null
        @JvmField
        var nShow: Int = 0
        @JvmField
        var hInstApp: Pointer? = null
        @JvmField
        var lpIDList: Pointer? = null
        @JvmField
        var lpClass: String? = null
        @JvmField
        var hkeyClass: Pointer? = null
        @JvmField
        var dwHotKey: Int = 0
        @JvmField
        var hIcon: Pointer? = null
        @JvmField
        var hProcess: Pointer? = null
    }

    fun ShellExecuteEx(execInfo: ShellExecInfo): Boolean

    companion object {
        val INSTANCE = Native.loadLibrary("Shell32", MyShell32::class.java) as MyShell32
    }
}