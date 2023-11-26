package com.example.flo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song=Song(binding.mainMiniplayerTitleTv.text.toString(),binding.mainMiniplayerSingerTv.text.toString())

        //선언된 binding.view id.이벤트 전달
        binding.mainPlayerCl.setOnClickListener {
            //이벤트를 받으면 해당 activity로 이동
//            startActivity(Intent(this,SongActivity::class.java))
            //intent 담기
            val intent=Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("nowPlayingTitle", String())
//            startActivity(intent)
            getResult.launch(intent)
        }

        initBottomNavigation()

        Log.d("Song",song.title+song.singer)
    }

    //2주차 미션-call back 등록
    val getResult=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result:ActivityResult-> //result는 ActivityResult의 value parameter
        if(result.resultCode==RESULT_OK){
            val nowPlayingTitle=result.data?.getStringExtra("nowPlayingTitle")!!
            Toast.makeText(this,nowPlayingTitle,Toast.LENGTH_SHORT).show()
        }
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
}