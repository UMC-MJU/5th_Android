package com.example.flo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import kotlinx.android.synthetic.main.activity_song.*

class SongActivity:AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //activity 종료 방법
        binding.songDownIb.setOnClickListener{
            //songDownIb를 눌렀을 때 할 작업 선언하는 것

            //2주과제용//
            val resultIntent=Intent()
            resultIntent.putExtra("nowPlayingTitle",binding.songMusicTitleTv.text.toString())
            setResult(Activity.RESULT_OK,resultIntent)
            //
            finish()

            //2주과제용
//            Toast.makeText(this,intent.getStringExtra("nowPlayingTitle"), Toast.LENGTH_SHORT).show()
//            Log.d("nowPlaying",""+intent.getStringExtra("nowPlayingTitle"))
        }

        binding.songPlayIb.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIb.setOnClickListener{
            setPlayerStatus(true)
        }

        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
//            binding.songMusicTitleTv.text=intent.getStringExtra("title")
//            binding.songSingerNameTv.text=intent.getStringExtra("singer")
            binding.songMusicTitleTv.setText(intent.getStringExtra("title"))
            binding.songSingerNameTv.setText(intent.getStringExtra("singer"))
        }

//        intent.putExtra("nowPlayingTitle",binding.songMusicTitleTv.text.toString())

        //3주차 과제
        binding.songRepeatIv.setOnClickListener{
            if(binding.songRepeatIv.isClickable){//뭘로 해야할지 모르겠음
                binding.songRepeatIv.setImageResource(R.drawable.btn_playlist_select_on)//임시
            }else{
                binding.songRepeatIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            }
        }
        binding.songRandomIv.setOnClickListener({
            binding.songRandomIv.setImageResource(R.drawable.btn_playlist_select_on)//임시
        })
        //////////////////
    }

    fun setPlayerStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songPlayIb.visibility=View.VISIBLE
            binding.songPauseIb.visibility=View.GONE
        }else{
            binding.songPlayIb.visibility=View.GONE
            binding.songPauseIb.visibility=View.VISIBLE
        }
    }
}