package com.example.flo

import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView=signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView=loginView
    }

    fun signUp(user: User){
        val authService= getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            //service에서 call을 반환하면 enqueue로 받아올 수 있음
            //object로 callback을 받아야 함-응답에 대한 반응
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS",response.toString())
                val resp:AuthResponse=response.body()!! //응답의 body를 가져옴
                when(resp.code){//응답 코드에 따라 다르게 동작
                    //이메일 오류 처리, 회원가입버튼onclick리스너 finish 삭제해야 동작
                    1000->signUpView.onSignUpSuccess()
                    else->signUpView.onSignUpFailure()
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE",t.message.toString())
            }
        })
        Log.d("SIGNUP","HELLO")
    }

    fun login(user: User){
        val authService= getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object : Callback<AuthResponse> {
            //service에서 call을 반환하면 enqueue로 받아올 수 있음
            //object로 callback을 받아야 함-응답에 대한 반응
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS",response.toString())
                val resp:AuthResponse=response.body()!! //응답의 body를 가져옴
                when(val code=resp.code){
                    1000->loginView.onLoginSuccess(code,resp.result!!)
                    else->loginView.onLoginFailure()
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE",t.message.toString())
            }
        })
        Log.d("SIGNUP","HELLO")
    }
}