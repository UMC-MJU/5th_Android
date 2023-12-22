package com.example.flo.ui.main

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.*
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.data.local.SongDatabase
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity

class MainActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding: ActivityMainBinding
    val songs= arrayListOf<Song>()
    var nowPos=0
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer?=null
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_FLO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()
        initBottomNavigation()

        //선언된 binding.view id.이벤트 전달
        binding.mainPlayerCl.setOnClickListener {
            //이벤트를 받으면 해당 activity로 이동
            //startActivity(Intent(this,SongActivity::class.java))

            //intent 담기
            val editor=getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",songs[nowPos].id)
            editor.apply()
            val intent=Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
        binding.mainSkipPreviousIv.setOnClickListener{moveSong(-1)}
        binding.mainSkipNextIv.setOnClickListener{moveSong(1)}
        binding.mainMiniplayerBtn.setOnClickListener{setPlayerStatus(true)}
        binding.mainPauseBtn.setOnClickListener{setPlayerStatus(false)}

        Log.d("MAIN/JWT_TO_SERVER",getJwt().toString())
    }
    private fun moveSong(direct:Int){
        if(nowPos+direct<0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos+direct>=songs.size){
            Toast.makeText(this,"Last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos+=direct
        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer=null
        setMiniPlayer(songs[nowPos])
    }
    private fun startTimer(){
        timer=Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
        timer.start()
    }

    private fun getJwt(): String? {
        val spf=this.getSharedPreferences("auth2",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","")
    }

    inner class Timer(private val playTime:Int, var isPlaying:Boolean):Thread(){
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
                            binding.mainMiniplayerProgressSb.progress=((mills/playTime)*100).toInt()
                        }
                        if (mills%1000==0f){//mills가 1초가 될때마다 second+1
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
    fun setPlayerStatus(isPlaying:Boolean){
        songs[nowPos].isPlaying=isPlaying
        timer.isPlaying=isPlaying

        if(isPlaying){
            binding.mainPauseBtn.visibility= View.VISIBLE
            binding.mainMiniplayerBtn.visibility= View.GONE
            mediaPlayer?.start()
        }else{
            binding.mainMiniplayerBtn.visibility= View.VISIBLE
            binding.mainPauseBtn.visibility= View.GONE
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
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

    public fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text=song.title
        binding.mainMiniplayerSingerTv.text=song.singer
        binding.mainMiniplayerProgressSb.progress=(song.second*100000)/song.playTime
        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music)
    }

    override fun onStart() {
        super.onStart()
        val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId=spf.getInt("songId",0)

        val songDB= SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
        nowPos=getPlayingSongPosition(songId)
        startTimer()

        Log.d("song ID",songs[nowPos].id.toString())
        if(nowPos==null){
            setMiniPlayer(songs[0])
        }else{
            setMiniPlayer(songs[nowPos])
        }
    }
    private fun getPlayingSongPosition(songId:Int):Int{
        for(i in 0 until songs.size){
            if (songs[i].id==songId){
                return i
            }
        }
        return 0
    }

    private fun inputDummySongs(){
        val songDB= SongDatabase.getInstance(this)!!
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
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()
        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )
        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )
        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )
    }
}