package com.example.flo.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import javax.xml.transform.OutputKeys

@Entity(tableName = "SongTable")
data class Song(
    var title:String="",
    var singer:String="",
    var second:Int=0,//노래가 얼마나 재생되었나
    var playTime:Int=0,//총 재생시간
    var isPlaying:Boolean=false, //플레이여부
    var music: String ="",//음악 파일
    var coverImg: Int?=null,
    var isLike: Boolean=false
){
    @PrimaryKey(autoGenerate = true)var id:Int=0
}
