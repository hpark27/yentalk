package com.themarks.yentalk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainIntent = Intent(this, MainActivity::class.java)
        // run intent
        startActivity(mainIntent)
        // finish splash activity so that it does not appear after moving to main activity
        finish()
    }
}