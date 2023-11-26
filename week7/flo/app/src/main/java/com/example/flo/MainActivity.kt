package com.example.flo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding: ActivityMainBinding
    private var song=Song()
    private var gson:Gson= Gson()
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FLO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //db 연결
        inputDummySongs()

        //선언된 binding.view id.이벤트 전달
        binding.mainPlayerCl.setOnClickListener {
            //이벤트를 받으면 해당 activity로 이동
            //startActivity(Intent(this,SongActivity::class.java))

            //intent 담기
            val editor=getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()
            val intent=Intent(this,SongActivity::class.java)
            startActivity(intent)
        }
        initBottomNavigation()
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    public fun setMiniPlayer(song:Song){
        binding.mainMiniplayerTitleTv.text=song.title
        binding.mainMiniplayerSingerTv.text=song.singer
        binding.mainMiniplayerProgressSb.progress=(song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
//        val songJson=sharedPreferences.getString("songData",null)
//
//        song= if(songJson==null){
//            Song("라일락","아이유(IU)",0,60,false,"music_lilac")
//        }else {
//            gson.fromJson(songJson, Song::class.java)
//        }
        val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId=spf.getInt("songId",0)

        val songDB=SongDatabase.getInstance(this)!!
        song=if (songId==0){
            songDB.songDao().getSongs(1)
        }else{
            songDB.songDao().getSongs(songId)
        }

        Log.d("song ID",song.id.toString())
        setMiniPlayer(song)
    }

    private fun inputDummySongs(){
        val songDB=SongDatabase.getInstance(this)!!
        val songs= songDB.songDao().getSongs()
        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단(BTS)",
                0,
                182,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
//                1
            )
        )
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유(IU)",
                0,
                214,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
//                0
            )
        )
        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파(AESPA)",
                0,
                235,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
//                2
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단(BTS)",
                0,
                252,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
//                3
            )
        )
        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드(MOMOLAND)",
                0,
                210,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
//                4
            )
        )

        val _songs=songDB.songDao().getSongs()
        Log.d("DB data",_songs.toString())
    }
}