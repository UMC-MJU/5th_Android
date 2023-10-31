package com.example.w1_flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.w1_flo.databinding.FragmentStoreBinding
import com.google.android.material.tabs.TabLayoutMediator



class storeFrag : Fragment() {
    private var information= arrayListOf("저장한 곡","음악파일")

    private lateinit var binding:FragmentStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentStoreBinding.inflate(layoutInflater,container,false)

        val storeAdapter=StoreVPAdapter(this)
        binding.storeVp.adapter=storeAdapter

        TabLayoutMediator(binding.storeTb,binding.storeVp){
            tab, position->
            tab.text=information[position]
        }.attach()

        return binding.root
    }


}