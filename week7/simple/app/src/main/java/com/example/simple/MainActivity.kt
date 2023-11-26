package com.example.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var list=ArrayList<Profile>()
    lateinit var customAdapter:CustomAdapter
    lateinit var db:ProfileDatabase

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //만들어 놓은 것들을 바탕으로 DB 사용해보기
        db=ProfileDatabase.getInstance(this)!!
        Thread{
            val savedContacts=db.profileDao().getAll()
            if(savedContacts.isNotEmpty()){
                list.addAll(savedContacts)
            }
        }.start()

        binding.button.setOnClickListener{
            Thread{
                list.add(Profile("승연","22","01058830056"))
                db.profileDao().insert(Profile("승연","22","01058830056"))
                val list=db.profileDao().getAll()
                Log.d("Inserted Primary key",list[list.size-1].id.toString())
            }.start()
            customAdapter.notifyDataSetChanged()
        }
        //

        customAdapter=CustomAdapter(list,this)
        binding.mainProfileLv.adapter=customAdapter

    }
}