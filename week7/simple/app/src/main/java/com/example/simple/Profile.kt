package com.example.simple

import androidx.room.*

@Entity
class Profile(
    var name:String,
    var age:String,
    var phone:String
) {
    @PrimaryKey(autoGenerate=true)var id:Int=0
}