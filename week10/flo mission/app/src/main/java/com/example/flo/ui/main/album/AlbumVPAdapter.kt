package com.example.flo.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter (fragment:Fragment) :FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int=3

    //상속 후 꼭 필요한 멤버함수
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> SongFragment()
            1-> DetailFragment()
            else-> VideoFragment()
        }
    }//getItemCount의 값이 4면 0, 1, 2, 3 실행
}