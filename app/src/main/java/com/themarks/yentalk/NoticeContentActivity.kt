package com.themarks.yentalk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.yentalk.databinding.ActivityNoticecontentBinding

class NoticeContentActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNoticecontentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticecontentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // go back to announcement list
        binding.back.setOnClickListener {
            finish()
        }

        // get data
        binding.noticeTitle.text = intent.getStringExtra("title")
        binding.noticeContent.text = intent.getStringExtra("content")
    }
}