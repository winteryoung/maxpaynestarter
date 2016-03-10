package com.github.winteryoung.maxpaynestarter

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import java.util.*

fun main(args: Array<String>) {
    var metMaxPayneStarterWindow = false

    println("Starting Max Payne...")
    ProcessBuilder("MaxPayne.exe", if (args.size > 0) args[0] else "").start()

    println("Detecting Max Payne game window...")
    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            when (loopWindows()) {
                "maxPayneInGame" -> {
                    timer.cancel()
                }
                "maxPayne" -> {
                    metMaxPayneStarterWindow = true
                }
                "notMaxPayne" -> {
                    if (metMaxPayneStarterWindow) {
                        timer.cancel()
                    }
                }
            }
        }
    }, 0, 1000)
}

private fun loopWindows(): String {
    val windows = ArrayList<WinDef.HWND>().apply {
        User32.INSTANCE.EnumWindows({ wnd, data ->
            add(wnd)
            true
        }, null)
    }

    for (window in windows) {
        when (procWindow(window)) {
            "maxPayne" -> {
                return "maxPayne"
            }
            "maxPayneInGame" -> {
                return "maxPayneInGame"
            }
        }
    }

    return "notMaxPayne"
}

private fun procWindow(wnd: WinDef.HWND): String {
    val wndClass = getClassName(wnd)
    val text = getWindowText(wnd)

    if (text.toLowerCase().startsWith("Max Payne".toLowerCase())) {
        if (User32.INSTANCE.IsWindowVisible(wnd)) {
            when (wndClass) {
                "Afx:400000:3" -> {
                    println("Detected Max Payne")
                    Thread.sleep(5000)
                    removeWndBorder(wnd)
                    maxMaxPayneWnd(wnd)
                    return "maxPayneInGame"
                }
                "#32770" -> {
                    return "maxPayne"
                }
            }
        }
    }
    return "notMaxPayne"
}

private fun removeWndBorder(wnd: WinDef.HWND) {
    User32.INSTANCE.GetWindowLong(wnd, WinUser.GWL_STYLE).let { style ->
        style and WinUser.WS_OVERLAPPEDWINDOW.inv()
    }.let { style ->
        User32.INSTANCE.SetWindowLong(wnd, WinUser.GWL_STYLE, style)
    }
}

private fun maxMaxPayneWnd(wnd: WinDef.HWND) {
    User32.INSTANCE.ShowWindow(wnd, WinUser.SW_SHOWMAXIMIZED)
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
