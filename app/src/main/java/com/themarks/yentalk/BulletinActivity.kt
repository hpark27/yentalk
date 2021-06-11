package com.themarks.yentalk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.themarks.yentalk.databinding.ActivityBulletinBinding

class BulletinActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBulletinBinding
    private lateinit var dataBase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBulletinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataBase = FirebaseDatabase.getInstance()

        // back button
        binding.back.setOnClickListener {
            finish()
        }

        // upload URL
        binding.upload.setOnClickListener {
            val url = binding.url.text.toString()

            uploadUrl(url)
        }
    }

    /**
     * Upload URL to firebase
     */
    private fun uploadUrl(url: String){
        if(url.isEmpty()){
            Toast.makeText(applicationContext,"Enter URL", Toast.LENGTH_SHORT).show()
            binding.url.requestFocus()
        }else{
            // reference to firebase database. Specific user database (*uid)
            dataBase.reference.child("Admin").child("Pdfurl").setValue(url).
            addOnCompleteListener {
                Toast.makeText(applicationContext,"Bulletin Url uploaded", Toast.LENGTH_SHORT).show()
                binding.url.text=null
                binding.url.requestFocus()
            }
        }
    }
}