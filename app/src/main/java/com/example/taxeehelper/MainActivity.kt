package com.example.taxeehelper

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToggle = findViewById<Button>(R.id.btnToggle)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // Tombol untuk membuka menu Aksesibilitas Sistem
        btnSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        // Tombol untuk Menyalakan/Mematikan fungsi Auto-Click
        btnToggle.setOnClickListener {
            AutoClickerService.isRunning = !AutoClickerService.isRunning
            if (AutoClickerService.isRunning) {
                tvStatus.text = "Status: AKTIF (Siap Menangkap Orderan)"
                btnToggle.text = "Matikan Auto-Click"
            } else {
                tvStatus.text = "Status: NONAKTIF"
                btnToggle.text = "Nyalakan Auto-Click"
            }
        }
    }
}
