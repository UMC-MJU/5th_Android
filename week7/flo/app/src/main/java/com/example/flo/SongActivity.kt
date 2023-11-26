package com.example.flo

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity:AppCompatActivity() {

    //전역변수
    lateinit var binding: ActivitySongBinding
    lateinit var timer:Timer
    private var mediaPlayer: MediaPlayer?=null
    private var gson: Gson=Gson()
    private var isRepeating: Boolean = false
    lateinit var foregoundintent:Intent

    val songs= arrayListOf<Song>()
    lateinit var songDB:SongDatabase
    var nowPos=0
    ///////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        //foreground 시작
        foregoundintent= Intent(this,Foreground::class.java)
        ContextCompat.startForegroundService(this,intent)
        //

        super.onCreate(savedInstanceState)
        binding=ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
    }

    //사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second=((binding.songProgressSb.progress*songs[nowPos].playTime)/100)/1000
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        val editor=sharedPreferences.edit() //에디터
        editor.putInt("songId",songs[nowPos].id)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt() //interrupt를 걸고 있긴 함...
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer=null //미디어플레이어 해제
    }

    private fun initPlayList(){
        songDB=SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }
    private fun initSong(){
        val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId=spf.getInt("songId",0)
        nowPos=getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener{
            stopService(foregoundintent)
            finish()
        }
        binding.songPlayIb.setOnClickListener { setPlayerStatus(true) }
        binding.songPauseIb.setOnClickListener{ setPlayerStatus(false) }
        binding.songRepeatIv.setOnClickListener{setRepeatStatus()};
        binding.songSkipNextIb.setOnClickListener{ moveSong(+1) }
        binding.songSkipPreviousIb.setOnClickListener{ moveSong(-1) }
        binding.songLikeIv.setOnClickListener{setLike(songs[nowPos].isLike)}
    }

    private fun setLike(isLike:Boolean){
        songs[nowPos].isLike=!isLike //간단하게 true면 false로 false면 true로
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)
        //새로 랜더링
        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(direct:Int){
        if(nowPos+direct<0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos+direct>=songs.size){
            Toast.makeText(this,"Last song",Toast.LENGTH_SHORT).show()
            return
        }
        nowPos+=direct
        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer=null
        setPlayer(songs[nowPos])
    }

    //모든 노래 목록에서 받은 id의 노래 찾기
    private fun getPlayingSongPosition(songId:Int):Int{
        for(i in 0 until songs.size){
            if (songs[i].id==songId){
                return i
            }
        }
        return 0
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

    private fun setPlayer(song: Song) {
        //초기화된 song ViewRendering
        binding.songMusicTitleTv.text=song.title
        binding.songSingerNameTv.text=song.singer
        binding.songStartTimeTv.text=String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text=String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songMusicAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress=(song.second*1000/song.playTime)
        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music)

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying:Boolean){
        songs[nowPos].isPlaying=isPlaying
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
        timer=Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
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
                                setPlayer(songs[nowPos])
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
}