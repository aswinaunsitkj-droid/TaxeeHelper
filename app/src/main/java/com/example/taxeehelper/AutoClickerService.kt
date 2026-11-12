package com.example.taxeehelper

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoClickerService : AccessibilityService() {

    companion object {
        var isRunning = false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!isRunning) return

        // Hanya aktif jika paket aplikasi target yang terbuka adalah Taxee Driver
        if (event.packageName != "com.taxee.driver") return

        val rootNode: AccessibilityNodeInfo = rootInActiveWindow ?: return
        
        // Eksekusi fungsi pencarian orderan teratas
        findAndClickTopOrder(rootNode)
        
        rootNode.recycle()
    }

    private fun findAndClickTopOrder(node: AccessibilityNodeInfo) {
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue

            val text = child.text?.toString() ?: ""
            val viewId = child.viewIdResourceName ?: ""

            // Mendeteksi elemen kartu order berdasarkan format teks nominal harga ("Rp") atau ID komponen
            if (viewId.contains("order") || text.contains("Rp")) {
                if (child.isClickable) {
                    child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    isRunning = false // Jeda otomatis setelah klik berhasil
                    break
                } else {
                    clickNodeCenter(child)
                    isRunning = false
                    break
                }
            }

            findAndClickTopOrder(child)
            child.recycle()
        }
    }

    private fun clickNodeCenter(node: AccessibilityNodeInfo) {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        val x = ((rect.left + rect.right) / 2).toFloat()
        val y = ((rect.top + rect.bottom) / 2).toFloat()

        val path = Path().apply {
            moveTo(x, y)
        }

        val builder = GestureDescription.Builder()
        val strokeDescription = GestureDescription.StrokeDescription(path, 0, 50)
        builder.addStroke(strokeDescription)

        dispatchGesture(builder.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
            }
            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
            }
        }, null)
    }

    override fun onInterrupt() {
        isRunning = false
    }
}
