package com.example.flo

class Album(
    var title:String?="",
    var singer:String?="",
    var coverImg:Int?=null,
    var songs: Song? =null,//수록곡 같은 경우
    var switchOn: Boolean?=false
        )