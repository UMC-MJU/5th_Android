package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment: Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSongBinding.inflate(inflater,container,false)

        binding.songMixoffTg.setOnClickListener{
            binding.songMixoffTg.setImageResource(R.drawable.btn_toggle_on)
        }

        return binding.root
    }
}