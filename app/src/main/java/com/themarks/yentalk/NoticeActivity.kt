package com.themarks.yentalk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.themarks.yentalk.databinding.ActivityNoticeBinding
import java.text.SimpleDateFormat
import java.util.*

class NoticeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNoticeBinding
    // firebase firestore
    private lateinit var fireStore: FirebaseFirestore
    // posting title
    private lateinit var title: String
    // posting content
    private lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize firestore
        fireStore = FirebaseFirestore.getInstance()

        // go back to posting page
        binding.back.setOnClickListener {
            finish()
        }

        // upload bulletin
        binding.upload.setOnClickListener {
        // get title and content
            title = binding.noticeTitle.text.toString()
            content = binding.noticeContent.text.toString()
            when {
                title.isEmpty() -> {
                    Toast.makeText(applicationContext, "Enter title info", Toast.LENGTH_SHORT).show()
                    binding.noticeTitle.requestFocus()
                }
                content.isEmpty() -> { // when content is empty
                    Toast.makeText(applicationContext, "Enter content info", Toast.LENGTH_SHORT).show()

                    binding.noticeContent.requestFocus()
                }
                else -> {
                    uploadAnnounce(title, content)
                }
            }
        }
    }

    /**
     * Upload announce on firebasestore
     */
    private fun uploadAnnounce(title: String, content: String){
        // current date
        val currentDate = Calendar.getInstance().time
        // convert date into string
        val date = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault()).format(currentDate)
        // firestore document name
        val fileName = "$title $date"

        val announceMap = hashMapOf(
            "title" to title,
            "content" to content,
            "date" to date)

        fireStore.collection("Announcement").document(fileName).set(announceMap)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                // clear edit text and request focus
                binding.noticeTitle.text = null
                binding.noticeContent.text = null
                binding.noticeTitle.requestFocus()
            }
    }
}