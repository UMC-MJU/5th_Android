package com.example.w1_flo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import com.example.w1_flo.databinding.ActivityMainBinding
import com.example.w1_flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator


class albumFrag : Fragment() {

    private lateinit var binding:FragmentAlbumBinding

    private val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(layoutInflater, container, false)//추가

        val bundle=arguments
        val str1=bundle?.getString("title_album")
        val str2=bundle?.getString("artist_album")
        if(str1!=null && str2!=null){
            binding.albumMusicTitleTv.text=str1
            binding.albumSingerNameTv.text=str2
        }
        Log.d("test",str1+str2)


        binding.albumBackIv.setOnClickListener {

            val _frag=(context as MainActivity).supportFragmentManager.findFragmentByTag("album")
            if(_frag!=null){
                (context as MainActivity).supportFragmentManager.beginTransaction().remove(_frag).commitAllowingStateLoss()
            }
            //(context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,homeFrag()).commitAllowingStateLoss()
        }

        val albumAdapter=AlbumVPAdapter(this)
        binding.albumContentVp.adapter=albumAdapter

        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab, position->
            tab.text=information[position]
        }.attach()

        return binding.root
    }





}