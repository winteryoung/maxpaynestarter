package com.github.winteryoung.maxpaynestarter

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import java.util.*

fun main(args: Array<String>) {
    println("Starting Max Payne...")
    ProcessBuilder("MaxPayne.exe", if (args.size > 0) args[0] else "").start()
    println("Detecting Max Payne game window...")
    Timer().let { timer ->
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (tryMaxWindow()) {
                    timer.cancel()
                }
            }
        }, 0, 1000)
    }
}

private fun tryMaxWindow(): Boolean {
    var foundWindow = false

    User32.INSTANCE.EnumWindows({ wnd, data ->
        getWindowText(wnd).let { text ->
            if (text.toLowerCase().startsWith("Max Payne".toLowerCase())) {
                if (User32.INSTANCE.IsWindowVisible(wnd)) {
                    getClassName(wnd).let { wndClass ->
                        if (wndClass == "Afx:400000:3") {
                            foundWindow = true
                            Thread.sleep(5000)
                            println("Detected Max Payne window: [$wndClass] $text")
                            removeWndBorder(wnd)
                            maxMaxPayneWnd(wnd)
                        }
                    }
                }
            }
        }
        true
    }, null)

    return foundWindow
}

private fun removeWndBorder(wnd: WinDef.HWND) {
    User32.INSTANCE.GetWindowLong(wnd, WinUser.GWL_STYLE).let { style ->
        style and WinUser.WS_OVERLAPPEDWINDOW.inv()
    }.let { style ->
        User32.INSTANCE.SetWindowLong(wnd, WinUser.GWL_STYLE, style)
    }
}

private fun maxMaxPayneWnd(wnd: WinDef.HWND) {
    User32.INSTANCE.ShowWindow(wnd, WinUser.SW_SHOWMAXIMIZED).let { ok ->
        if (!ok) {
            throw Exception("Can't max Max Payne window. Need admin privilege to work.")
        }
    }
}

fun getWindowText(wnd: WinDef.HWND): String {
    CharArray(512).let { textBuf ->
        User32.INSTANCE.GetWindowText(wnd, textBuf, textBuf.size)
        return Native.toString(textBuf)
    }
}

fun getClassName(wnd: WinDef.HWND): String {
    CharArray(512).let { clsName ->
        User32.INSTANCE.GetClassName(wnd, clsName, clsName.size)
        return Native.toString(clsName)
    }
}

fun showWindow(wnd: WinDef.HWND, cmdShow: Int) {
    User32.INSTANCE.ShowWindow(wnd, cmdShow).let { ok ->
        if (!ok) {
            throw Exception("Can't max Max Payne window. Need admin privilege to work.")
        }
    }
}