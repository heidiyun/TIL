package com.example.user

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

// 사용자가 Application을 만들고싶으면 상속해서 만든다.
// AndroidManifest 에 등록해주어야 한다.

class ChatApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // 앱이 처음 구동할 때 생성되는 인스턴스
        JodaTimeAndroid.init(this)
    }
}