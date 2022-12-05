package ru.desh.partfinder.features.start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import ru.desh.partfinder.core.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            // Do some preparation work
            // - Get on boarding flag
            // - Try to auth from cache, refresh token
            // - Get data for main page
            delay(2000)
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        }
    }
}