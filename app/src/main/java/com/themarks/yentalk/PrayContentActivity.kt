package com.themarks.yentalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.yentalk.databinding.ActivityPraycontentBinding

class PrayContentActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPraycontentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPraycontentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // go back to announcement list
        binding.back.setOnClickListener {
            finish()
        }

        // get data
        binding.prayTitle.text = intent.getStringExtra("name")
        binding.prayContent.text = intent.getStringExtra("content")
    }
}