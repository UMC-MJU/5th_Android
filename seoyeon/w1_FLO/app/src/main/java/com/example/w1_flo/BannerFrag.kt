package com.example.w1_flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.w1_flo.databinding.FragmentBannerBinding


class BannerFrag(val img:Int) : Fragment() {

    private lateinit var binding:FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBannerBinding.inflate(layoutInflater,container,false)
        binding.bannerImg.setImageResource(img)

        return binding.root
    }

}