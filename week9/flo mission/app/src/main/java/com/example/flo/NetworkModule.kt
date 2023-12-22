package com.example.flo

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//interface 부분에서 슬래시 안 쳤으면 여기서 칠 것
const val Base_URL="https://edu-api-test.softsquared.com"
fun getRetrofit():Retrofit{
    val retrofit=Retrofit.Builder().baseUrl(Base_URL)
        .addConverterFactory(GsonConverterFactory.create())//gson 넣어줌
        .build()//build 완료
    return retrofit
}