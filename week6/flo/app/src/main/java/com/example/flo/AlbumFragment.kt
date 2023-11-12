package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment:Fragment() {
    lateinit var binding:FragmentAlbumBinding
    private var gson:Gson= Gson()

    private val information= arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAlbumBinding.inflate(inflater,container,false)

//        //선택한 앨범타이틀과 가수 문자 가져옴-6챕터 이전
//        binding.albumMusicTitleTv.text=arguments?.getString("title")
//        binding.albumSingerNameTv.text=arguments?.getString("singer")
        val albumJson=arguments?.getString("album")
        val album=gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        //뒤로가기 버튼
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter=AlbumVPAdapter(this)
        binding.albumContentVp.adapter=albumAdapter

        //tab layout과 view pager2 연결하는 중재자
        //인자값으론 탭레이아웃,view pager를 넣는다.
        //괄호 안에는 탭레이아웃에 어떤 텍스트가 들어갈지 넣는다.
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab, position->
            tab.text=information[position]//information이라는 배열에 이름을 넣고 순서대로 붙임
        }.attach() //붙이기


        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumMusicAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text=album.title.toString()
        binding.albumSingerNameTv.text=album.singer.toString()
    }
}