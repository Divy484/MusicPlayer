package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityPlayerBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class PlayerActivity : AppCompatActivity(), ServiceConnection {
    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition : Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //for service
        val intentService = Intent(this, MusicService::class.java)
        bindService(intentService, this, BIND_AUTO_CREATE)
        startService(intentService)

        val backBtn : ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtnPA)
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
        initializeLayout()
    }

    private fun setLayout(){
        val songName : TextView = findViewById(R.id.songNamePA)
        songName.text = musicListPA[songPosition].title
    }

    private fun createMediaPlayer(){
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtnPA)
            playPause.setIconResource(R.drawable.baseline_pause_24)
            musicService!!.showNotification(R.drawable.baseline_pause_24)
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
            }
            "MainActivity" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()
            }
        }
    }

    private fun playMusic(){
        val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtnPA)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
        playPause.setIconResource(R.drawable.baseline_pause_24)
        musicService!!.showNotification(R.drawable.baseline_pause_24)
    }
    private fun pauseMusic(){
        val playPause : ExtendedFloatingActionButton = findViewById(R.id.playPausebtnPA)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
        playPause.setIconResource(R.drawable.baseline_play_arrow_24)
        musicService!!.showNotification(R.drawable.baseline_play_arrow_24)
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


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (musicService == null) {
            val binder = service as MusicService.MyBinder
            musicService = binder.currentService()
        }
        createMediaPlayer()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}