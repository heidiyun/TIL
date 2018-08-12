package com.example.user.rxsamples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInModel {
    fun login(email: String, password: String) {

    }
}

class SignInViewModel {
    var email: String = ""
    var password: String = ""
    var isSavePassword: Boolean = false

    val model = SignInModel()

    fun login() {

    }
}


//view model은 화면에 보이는 모든 것을 순수 코드로 작성
// view model은 view 와 model의 data binding을 위한 중간 다리 역할을 한다.


class SignInActivity : AppCompatActivity() {
    val viewModel = SignInViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailEditText
                .textChanges()
                .subscribe {

                }

    }
}
