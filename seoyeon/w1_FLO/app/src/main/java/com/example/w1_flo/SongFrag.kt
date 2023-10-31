package com.example.w1_flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.w1_flo.databinding.FragmentDetailBinding
import com.example.w1_flo.databinding.FragmentSongBinding


class SongFrag : Fragment() {
    private lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSongBinding.inflate(layoutInflater,container,false,)

        binding.songMixoffTg.setOnClickListener {
            setMixStatus(false)
        }
        binding.songMixonTg.setOnClickListener {
            setMixStatus(true)
        }

        return binding.root
    }

    fun setMixStatus(isOn:Boolean){
        if(!isOn){
            binding.songMixoffTg.visibility=View.GONE
            binding.songMixonTg.visibility=View.VISIBLE
        }
        if(isOn){
            binding.songMixoffTg.visibility=View.VISIBLE
            binding.songMixonTg.visibility=View.GONE
        }
    }
}