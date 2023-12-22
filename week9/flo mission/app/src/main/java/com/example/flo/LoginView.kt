package com.example.flo

interface LoginView {
    fun onLoginSuccess(code:Int,result: com.example.flo.Result)
    fun onLoginFailure()
}