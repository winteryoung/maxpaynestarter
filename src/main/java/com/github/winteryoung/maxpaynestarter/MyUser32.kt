package com.github.winteryoung.maxpaynestarter

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.win32.StdCallLibrary

/**
 * @author Winter Young
 * @since 2016/3/6
 */
interface MyUser32 : StdCallLibrary {
    fun GetDesktopWindow(): WinDef.HWND

    fun BringWindowToTop(wnd: WinDef.HWND): Boolean

    companion object {
        val INSTANCE = Native.loadLibrary("User32", MyUser32::class.java) as MyUser32
    }
}