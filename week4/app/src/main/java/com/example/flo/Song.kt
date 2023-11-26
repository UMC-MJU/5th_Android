package com.example.flo

data class Song(
    val title:String="",
    val singer:String="",
    var second:Int=0,//노래가 얼마나 재생되었나
    var playTime:Int=0,//총 재생시간
    var isPlaying:Boolean=false //플레이여부
)
