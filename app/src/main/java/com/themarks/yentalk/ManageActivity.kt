package com.themarks.yentalk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.yentalk.databinding.ActivityManageBinding

class ManageActivity:AppCompatActivity() {
    private lateinit var binding: ActivityManageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intentData = intent.getIntExtra("Manage",0)

        binding.back.setOnClickListener {
            finish()
        }

        binding.bulletin.setOnClickListener {
            val bulletin = Intent(this,BulletinActivity::class.java)
            startActivity(bulletin)
        }

        binding.notice.setOnClickListener {
            if(intentData==1){
                val noticeEdit = Intent(this,NoticeEditActivity::class.java)
                startActivity(noticeEdit)
            }

            if(intentData==2){
                val notice = Intent(this,NoticeActivity::class.java)
                startActivity(notice)
            }
        }

        binding.newbie.setOnClickListener {
            if(intentData==1){
                val newbie = Intent(this,NewComerEditActivity::class.java)
                startActivity(newbie)
            }

            if(intentData==2){
                val newbie = Intent(this,NewComerActivity::class.java)
                startActivity(newbie)
            }
        }

        binding.pray.setOnClickListener {
            if(intentData==1){
                val pray = Intent(this,PrayEditActivity::class.java)
                startActivity(pray)
            }

            if(intentData==2){
                val pray = Intent(this,PrayActivity::class.java)
                startActivity(pray)
            }
        }
    }
}