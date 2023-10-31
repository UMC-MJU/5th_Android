package com.example.w1_flo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.w1_flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding

    lateinit var song: Song
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("album_songTohome", "IU 5th Album 'LILAC'")
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        var repeatOn: Boolean = false
        binding.songRepeatIv.setOnClickListener {
            if (!repeatOn) {
                binding.songRepeatIv.setColorFilter(Color.parseColor("#3f3fff"))
                repeatOn = true
            } else {
                binding.songRepeatIv.clearColorFilter()
                repeatOn = false
            }
        }
        var randomOn: Boolean = false
        binding.songRandomIv.setOnClickListener {
            if (!randomOn) {
                binding.songRandomIv.setColorFilter(Color.parseColor("#3f3fff"))
                randomOn = true
            } else {
                binding.songRandomIv.clearColorFilter()
                randomOn = false
            }
        }


    }

    fun setPlayerStatus(isPlaying: Boolean) {

        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE

        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    private fun startTimer() {
        timer=Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("artist")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("artist")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)

            )

        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("artist")
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress=(song.second*1000/song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {

        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            while (true) {
                if (second >= playTime)
                    break

                if (isPlaying) {
                    sleep(50)
                    mills += 50

                    runOnUiThread {
                        binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                    }
                    if (mills % 1000 == 0f) {
                        runOnUiThread {
                            binding.songStartTimeTv.text =
                                String.format("%02d:%02d", second / 60, second % 60)
                        }
                        second++
                    }
                }
            }
        }
    }
}