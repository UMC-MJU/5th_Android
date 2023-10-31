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

    //전역변수
    lateinit var binding: ActivitySongBinding
    lateinit var song:Song
    lateinit var timer:Timer
    ///////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer()

        //activity 종료 방법
        binding.songDownIb.setOnClickListener{
            //2주과제용//
//            val resultIntent=Intent()
//            resultIntent.putExtra("nowPlayingTitle",binding.songMusicTitleTv.text.toString())
//            setResult(Activity.RESULT_OK,resultIntent)
            //
            finish()
        }

        binding.songPlayIb.setOnClickListener { setPlayerStatus(true) }
        binding.songPauseIb.setOnClickListener{ setPlayerStatus(false) }

        //3주차 과제
//        binding.songRepeatIv.setOnClickListener{
//            if(binding.songRepeatIv.isClickable){//뭘로 해야할지 모르겠음
//                binding.songRepeatIv.setImageResource(R.drawable.btn_playlist_select_on)//임시
//            }else{
//                binding.songRepeatIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
//            }
//        }
//        binding.songRandomIv.setOnClickListener({
//            binding.songRandomIv.setImageResource(R.drawable.btn_playlist_select_on)//임시
//        })
        //////////////////
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong(){
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){//노래 있는지 없는지 확인
            song=Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false)
            )
        }
        startTimer()
    }
    private fun setPlayer(){
        //초기화된 song ViewRendering
        binding.songMusicTitleTv.text=intent.getStringExtra("title")
        binding.songSingerNameTv.text=intent.getStringExtra("singer")
        binding.songStartTimeTv.text=String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songProgressSb.progress=(song.second*1000/song.playTime)
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying:Boolean){
        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying

        if(isPlaying){
            binding.songPauseIb.visibility=View.VISIBLE
            binding.songPlayIb.visibility=View.GONE
        }else{
            binding.songPlayIb.visibility=View.VISIBLE
            binding.songPauseIb.visibility=View.GONE
        }
    }

    private fun startTimer(){
        timer=Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime:Int, var isPlaying:Boolean=true):Thread(){
        private var second:Int=0
        private var mills:Float=0f
        private var pt:Int=playTime

        override fun run() {
            super.run()
            try {
                while (true){
                    if(second>=playTime){
                        runOnUiThread{
                            setPlayerStatus(false)

                        }
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills+=50

                        runOnUiThread{//progress bar 진행
                            binding.songProgressSb.progress=((mills/playTime)*100).toInt()
                        }
                        if (mills%1000==0f){//mills가 1초가 될때마다 second+1
                            runOnUiThread{
                                binding.songStartTimeTv.text=String.format("%02d:%02d",second/60,second%60)
                                binding.songEndTimeTv.text=String.format("%02d:%02d", pt/60,pt%60)
                            }
                            second++
                            pt--
                        }
                    }
                }

            }catch (e:InterruptedException){
                Log.d("Song","Thread 종료. ${e.message}")
            }
        }

    }
}