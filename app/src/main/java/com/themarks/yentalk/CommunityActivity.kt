package com.themarks.yentalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.yentalk.databinding.ActivityCommunityBinding

class CommunityActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // back
        binding.back.setOnClickListener{
            finish()
        }
    }
}