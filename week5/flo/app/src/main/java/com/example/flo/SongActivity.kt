package com.example.flo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_song.*

class SongActivity:AppCompatActivity() {

    //전역변수
    lateinit var binding: ActivitySongBinding
    lateinit var song:Song
    lateinit var timer:Timer
    private var mediaPlayer: MediaPlayer?=null
    private var gson: Gson=Gson()
    private var isRepeating: Boolean = false
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
        binding.songRepeatIv.setOnClickListener{setRepeatStatus()};

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

    private fun setRepeatStatus() {
        if(isRepeating){
            isRepeating=false
            binding.songRepeatIv.setColorFilter(Color.parseColor("#00000000"))
        }
        else{
            isRepeating=true
            binding.songRepeatIv.setColorFilter(Color.parseColor("#7c30d900"))
        }
    }

    private fun initSong(){
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){//노래 있는지 없는지 확인
            song=Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!
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
        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music)
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying:Boolean){
        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying

        if(isPlaying){
            binding.songPauseIb.visibility=View.VISIBLE
            binding.songPlayIb.visibility=View.GONE
            mediaPlayer?.start()
        }else{
            binding.songPlayIb.visibility=View.VISIBLE
            binding.songPauseIb.visibility=View.GONE
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
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
                            if(isRepeating){
                                timer.interrupt()
                                mediaPlayer?.release()
                                mediaPlayer=null
                                initSong()
                                setPlayer()
                                setPlayerStatus(true)
                            }else{
                                setPlayerStatus(false)
                            }
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

    //사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second=((binding.songProgressSb.progress*song.playTime)/100)/1000
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        val editor=sharedPreferences.edit() //에디터
        val songJson=gson.toJson(song)
        editor.putString("songData",songJson)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt() //interrupt를 걸고 있긴 함...
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer=null //미디어플레이어 해제
    }
}