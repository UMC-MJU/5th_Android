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
    private var song:Song=Song()
    private var gson:Gson= Gson()
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FLO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //선언된 binding.view id.이벤트 전달
        binding.mainPlayerCl.setOnClickListener {
            //이벤트를 받으면 해당 activity로 이동
            //startActivity(Intent(this,SongActivity::class.java))

            //intent 담기
            val intent=Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)
            startActivity(intent)
//            getResult.launch(intent)
        }

        initBottomNavigation()

        Log.d("Song",song.title+song.singer)
    }

    //2주차 미션-call back 등록
//    val getResult=registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ){result:ActivityResult-> //result는 ActivityResult의 value parameter
//        if(result.resultCode==RESULT_OK){
//            val nowPlayingTitle=result.data?.getStringExtra("nowPlayingTitle")!!
//            Toast.makeText(this,nowPlayingTitle,Toast.LENGTH_SHORT).show()
//        }
//    }
//    /////////////////////////

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
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        val songJson=sharedPreferences.getString("songData",null)
        val albumJson=sharedPreferences.getString("album",null)

        song= if(songJson==null){
            Song("라일락","아이유(IU)",0,60,false,"music_lilac")
        }else {
            gson.fromJson(songJson, Song::class.java)
        }
        setMiniPlayer(song)
    }
}