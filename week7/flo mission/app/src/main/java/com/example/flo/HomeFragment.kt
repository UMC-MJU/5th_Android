package com.example.flo

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas=ArrayList<Album>()
    private lateinit var songDB: SongDatabase
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp,
                Song("Butter","방탄소년단",0,60,false,"music_butter")))
            add(Album("Lilac","아이유(IU)",R.drawable.img_album_exp2,
                Song("Lilac","아이유(IU)",0,60,false,"music_lilac")))
            add(Album("Next Level","에스파(AESPA)",R.drawable.img_album_exp3,
                Song("Next Level","에스파(AESPA)",0,60,false,"music_next")))
            add(Album("Boy with Luv","방탄소년단(BTS)",R.drawable.img_album_exp4,
                Song("Boy with Luv","방탄소년단(BTS)",0,60,false,"music_boy")))
            add(Album("BBoom BBoom","모모랜드(MOMOLAND)",R.drawable.img_album_exp5,
                Song("BBoom BBoom","모모랜드(MOMOLAND)",0,60,false,"music_bboom")))
            add(Album("Weekend","태연(Tae Yeon)",R.drawable.img_album_exp6,
                Song("Weekend","태연(Tae Yeon)",0,60,false,"music_flu")))
        }

        val albumRVAdapter=AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter=albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        albumRVAdapter.setMyItemClickListener(object:AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
            //mini player 정보 변경
            override fun onPlayAlbum(album: Album) {
                album.songs?.let { (activity as MainActivity).setMiniPlayer(it) }
            }
        })

        val bannerAdapter=BannerVPAdapter(this)
        //fragment 두 개 넣음
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        //view pager와 adapter 연결
        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL //움직일 방향 지정

        val pannelAdapter=BannerVPAdapter(this)
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        binding.homePannelBackgroundVp.adapter=pannelAdapter
        binding.homeBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        //indicator 추가
        binding.homeIndicator.setViewPager(binding.homePannelBackgroundVp)

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }
}
