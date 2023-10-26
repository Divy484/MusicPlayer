package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        setContentView(R.layout.activity_playlist)

        val backBtnPLA : ImageButton = findViewById(R.id.backBtnPLA)
        backBtnPLA.setOnClickListener {
            finish()
        }
    }
}