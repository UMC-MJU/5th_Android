package com.example.flo.ui.main.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment:Fragment() {
    lateinit var binding:FragmentAlbumBinding
    private var gson:Gson= Gson()

    private val information= arrayListOf("수록곡","상세정보","영상")

    private var isLiked:Boolean=false

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

        //home에서 넘어온 데이터 반영
        isLiked=isLikeAlbum(album.id) //like 여부 확인
        setInit(album)
        setOnClickListeners(album)

        //뒤로가기 버튼
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter= AlbumVPAdapter(this)
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
        if(isLiked){
            binding.albumLikeBtnIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeBtnIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }
    private fun getJwt():Int{
        //activity의 유무를 fragment는 모르기 때문에 물음표 작성
        val spf=activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }

    private fun likeAlbum(userId:Int,albumId:Int){
        val songDB= SongDatabase.getInstance(requireContext())!!
        val like= Like(userId,albumId)

        songDB.albumDao().likeAlbum(like)
    }
    private fun isLikeAlbum(albumId:Int):Boolean{ //초기 like 여부 설정
        val songDB= SongDatabase.getInstance(requireContext())!!
        val userId=getJwt()

        val likeId:Int?=songDB.albumDao().isLikedAlbum(userId,albumId)
        return likeId!=null //null이 아니면 true, null이면 false
    }
    private fun disLikeAlbum(albumId:Int){
        val songDB= SongDatabase.getInstance(requireContext())!!
        val userId=getJwt()

        songDB.albumDao().disLikeAlbum(userId,albumId)
    }
    private fun setOnClickListeners(album: Album){
        val userId=getJwt()
        binding.albumLikeBtnIv.setOnClickListener{
            if(isLiked){
                binding.albumLikeBtnIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(album.id)
            }else{
                binding.albumLikeBtnIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)
            }
        }
    }
}