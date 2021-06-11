package com.themarks.yentalk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.yentalk.databinding.ActivityMeetingBinding

class MeetingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // back
        binding.back.setOnClickListener{
            finish()
        }

        // church map
        binding.faclity.setOnClickListener {
            val facility = Intent(this, FacilityActivity::class.java)

            startActivity(facility)
        }
    }
}