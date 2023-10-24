package com.example.musicplayer

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

class PlayerActivity : AppCompatActivity() {
    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition : Int = 0
        var mediaPlayer : MediaPlayer? = null
        var isPlaying: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        setContentView(R.layout.activity_player)

        val backBtn : ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        initializeLayout()
        val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtn)
        playPause.setOnClickListener {
            if (isPlaying) pauseMusic()
            else playMusic()
        }
        val previous : ExtendedFloatingActionButton = findViewById(R.id.previousbtnPA)
        val next : ExtendedFloatingActionButton = findViewById(R.id.nextbtnPA)
        previous.setOnClickListener {
            prevNextSong(false)
        }
        next.setOnClickListener {
            prevNextSong(true)
        }
    }

    private fun setLayout(){
        val songName : TextView = findViewById(R.id.songNamePA)
        songName.text = musicListPA[songPosition].title
    }

    private fun createMediaPlayer(){
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtn)
            playPause.setIconResource(R.drawable.baseline_pause_24)
        }
        catch (e:Exception){
            return
        }
    }
    private fun initializeLayout(){
        songPosition = intent.getIntExtra("index", 0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
                createMediaPlayer()
            }
        }
    }

    private fun playMusic(){
        val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtn)
        playPause.setIconResource(R.drawable.baseline_pause_24)
        isPlaying = true
        mediaPlayer!!.start()
    }
    private fun pauseMusic(){
        val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtn)
        playPause.setIconResource(R.drawable.baseline_play_arrow_24)
        isPlaying = false
        mediaPlayer!!.pause()
    }
    private fun prevNextSong(increment: Boolean){
        if(increment){
            setSongPosition(true)
            setLayout()
            createMediaPlayer()
        }
        else{
            setSongPosition(false)
            setLayout()
            createMediaPlayer()
        }
    }
    private fun setSongPosition(increment: Boolean) {
        if (increment){
            if (musicListPA.size - 1 == songPosition) {
                songPosition = 0
            }
            else {
                ++songPosition
            }
        }
        else{
            if (0 == songPosition) {
                songPosition = musicListPA.size-1
            }
            else {
                --songPosition
            }
        }
    }
}