package com.example.memo

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.memo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var text:String=String()
    lateinit var edit:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        edit=binding.et

        binding.button.setOnClickListener{
            val intent=Intent(this,ResultActivity::class.java)
            intent.putExtra("text",edit.text.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(text!=null){
            edit.setText(text)
        }
    }

    override fun onPause() {
        super.onPause()
        this.text= edit.text.toString()
        Log.d("tag","onpause 호출")
    }

    override fun onRestart() {
        super.onRestart()
        var d:AlertDialog.Builder= AlertDialog.Builder(this)
        d.setMessage("다시 작성하시겠습니까?")
            .setPositiveButton("예",
            DialogInterface.OnClickListener{dialog,id->
            })
            .setNegativeButton("아니오",
            DialogInterface.OnClickListener{dialog,id->
                text= null.toString()
                binding.et.text=null
            })
        var alterDialog=d.create()
        alterDialog.show()
    }
}