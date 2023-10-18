package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermission()
        setTheme(R.style.coolPinkNav)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val shufflebtn : Button = findViewById(R.id.shuffle_btn)
        shufflebtn.setOnClickListener {
            startActivity(Intent(this,PlayerActivity::class.java))
            finish()
        }

        val favBtn : Button = findViewById(R.id.favourite_btn)
        favBtn.setOnClickListener {
            startActivity(Intent(this,FavouriteActivity::class.java))
            finish()
        }

        val playlistBtn : Button = findViewById(R.id.playlist_btn)
        playlistBtn.setOnClickListener {
            startActivity(Intent(this,PlaylistActivity::class.java))
            finish()
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navFeedback -> Toast.makeText(baseContext, "Feedback", Toast.LENGTH_SHORT).show()
                R.id.navSettings -> Toast.makeText(baseContext, "Settings", Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(baseContext, "About", Toast.LENGTH_SHORT).show()
                R.id.navExit -> exitProcess(1)
            }
            true
        }

        val musicRV : RecyclerView = findViewById(R.id.musicRV)
        val musicList = ArrayList<String>()
        musicList.add("1st Song")
        musicList.add("2nd Song")
        musicList.add("3rd Song")
        musicList.add("4th Song")
        musicList.add("5th Song")
        musicRV.setHasFixedSize(true)
        musicRV.setItemViewCacheSize(13)
        musicRV.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity, musicList)
        musicRV.adapter = musicAdapter

        val totalSongs : TextView = findViewById(R.id.totalSongs)
        totalSongs.text = "Total Songs : "+musicAdapter.itemCount
    }
    //for request permission
    private fun requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
}