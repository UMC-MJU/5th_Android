package com.example.w1_flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.w1_flo.databinding.FragmentDetailBinding
import com.example.w1_flo.databinding.FragmentVideoBinding


class VideoFrag : Fragment() {
    private lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVideoBinding.inflate(layoutInflater,container,false,)
        return binding.root
    }
}