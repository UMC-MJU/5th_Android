package com.example.simple

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [Profile::class], version = 1)
abstract class ProfileDatabase:RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object{
        private var instance:ProfileDatabase?=null
        @Synchronized
        fun getInstance(context:Context):ProfileDatabase?{
            if(instance==null){
                synchronized(ProfileDatabase::class){
                    instance=Room.databaseBuilder(
                        context.applicationContext,
                        ProfileDatabase::class.java,
                        "user-database"//다른 데이터베이스랑 이름 겹치지 않도록
                    ).build()
                }
            }
            return instance
        }
    }
}