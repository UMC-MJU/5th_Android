package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity:AppCompatActivity(),LoginView {
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

//    private fun login(){
//        if(binding.loginIdEt.text.toString().isEmpty()
//            ||binding.loginDirectInputEt.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if(binding.loginPasswordEt.text.toString().isEmpty()){
//            Toast.makeText(this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        val email:String=binding.loginIdEt.text.toString()+"@"+binding.loginDirectInputEt.text.toString()
//        val pwd:String=binding.loginPasswordEt.text.toString()
//
//        val songDB=SongDatabase.getInstance(this)!!
//        val user=songDB.userDao().getUser(email,pwd)
//
//        user?.let {
//            Log.d("LOGIN_ACT/GET_USER","userId: ${user.id}, $user")
//            saveJwt(user.id)
//            startMainActivity()
//        }
//        Toast.makeText(this,"회원 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
//    }

    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty()
            ||binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(getUser())
        //getUser 안 쓰고 User(email,password,"")해서 정보 넣고 로그인해도 됨
    }
    private fun getUser(): User {
        val email = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        return User(email = email, password = password, name = "")
    }

//    private fun saveJwt(jwt:Int){
//        val spf=getSharedPreferences("auth", MODE_PRIVATE)
//        val editor=spf.edit()
//        editor.putInt("jwt",jwt)
//        editor.apply()
//    }
    private fun saveJwt(jwt:String){
        //api 명세서에 따른 인증(강의용이며 실제론 더 복잡?한 듯)
        val spf=getSharedPreferences("auth2", MODE_PRIVATE)
        val editor=spf.edit()
        editor.putString("jwt",jwt)
        editor.apply()
    }

    //로그인 종료 후 main activity로 나가기
    private fun startMainActivity(){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code:Int,result:Result) {
        when(code){
            1000->{
                saveJwt(result.jwt)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        //실패시
    }
}