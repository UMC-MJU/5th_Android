package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayNewMusicAlbum1Iv.setOnClickListener{

            //2주차 미션
            //fragment->fragment 데이터 전달
            var albumFragment=AlbumFragment()
            var bundle=Bundle()
            bundle.putString("title",binding.homeTodayNewMusicAlbumtTitle1Tv.text.toString())
            bundle.putString("singer",binding.homeTodayNewMusicAlbumtSinger1Tv.text.toString())
            albumFragment.arguments=bundle
            ///////////////////////

            //(MainActivity 안에서!) (homeFragment의 main_frm를 AlbumFragment)로 대체(replace)하겠다
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,albumFragment)
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}