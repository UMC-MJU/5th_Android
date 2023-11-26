package com.example.flo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignupBinding

class SignUpActivity:AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
            finish()
        }
    }

    //입력한 유저 정보 가져오기
    private fun getUser():User{
        val email:String=binding.signUpIdEt.text.toString()+"@"+binding.signUpDirectInputEt.text.toString()
        val pwd:String=binding.signUpPasswordEt.text.toString()
        //꼭 string으로 변환해줄것
        return User(email,pwd)
    }
    private fun signUp(){
        //validation 처리-간단하게
        if(binding.signUpIdEt.text.toString().isEmpty()
            ||binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return //밑의 코드는 진행하지 않고 종료
        }
        if(binding.signUpPasswordEt.text.toString()!=binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }
        //정보 저장 작업
        val userDB=SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user=userDB.userDao().getUsers()
        Log.d("SIGNUPACT",user.toString())

    }
}