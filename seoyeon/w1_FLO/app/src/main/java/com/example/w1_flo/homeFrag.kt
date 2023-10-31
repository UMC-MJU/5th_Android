package com.example.w1_flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.w1_flo.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3


private lateinit var binding:FragmentHomeBinding

class homeFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater,container,false)



        binding.todayAlbum1.setOnClickListener {
            var fragAlbum=albumFrag()
            var bundle=Bundle()
            val song=Song(binding.todayAlbum1Title.text.toString(), binding.todayAlbum1Artist.text.toString())
            bundle.putString("title_album",song.title)
            bundle.putString("artist_album",song.artist)
            fragAlbum.arguments=bundle
            Log.d("Song",song.title+song.artist)

            (context as MainActivity).supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView ,fragAlbum,"album").commitAllowingStateLoss()
            //(context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,fragAlbum).commitAllowingStateLoss()
        }

        val bannerAdapter=BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFrag(R.drawable.banner))
        bannerAdapter.addFragment(BannerFrag(R.drawable.banner))
        binding.homeBanner.adapter=bannerAdapter
        binding.homeBanner.orientation=ViewPager2.ORIENTATION_HORIZONTAL


        val panelAdapter=PanelVPAdapter(this)
        binding.homePannelVp.adapter=panelAdapter



        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(binding.homePannelVp)


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}