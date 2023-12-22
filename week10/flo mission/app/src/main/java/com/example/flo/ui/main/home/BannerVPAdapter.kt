package com.example.flo.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter (fragment:Fragment) :FragmentStateAdapter(fragment) {
    //    //fragment를 parameter로 받고 adapter를 상속
    //
    //    //list 만들어서 fragment들 담기
    private val fragmentlist:ArrayList<Fragment> = ArrayList()

    //상속 후 꼭 필요한 멤버함수
//    override fun getItemCount(): Int {
//        //class에서 연결된 view pager에게 데이터를 전달할 때, 데이터 몇 개를 전달할 것인지 적음
//        return fragmentlist.size
//    } ^ 본래 형태 / v 같은 걸 간결하게 쓴 것
    override fun getItemCount(): Int=fragmentlist.size

    //상속 후 꼭 필요한 멤버함수
    override fun createFragment(position: Int): Fragment = fragmentlist[position]
    //getItemCount의 값이 4면 0, 1, 2, 3 실행

    //fragment 추가 함수
    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        //list에 새 값이 추가됐음을 알림
        notifyItemInserted(fragmentlist.size-1)
    }
}