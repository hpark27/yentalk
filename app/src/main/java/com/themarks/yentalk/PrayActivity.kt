package com.themarks.yentalk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.themarks.yentalk.databinding.ActivityPrayBinding
import java.text.SimpleDateFormat
import java.util.*

class PrayActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPrayBinding
    // firebase firestore
    private lateinit var fireStore: FirebaseFirestore
    // posting title
    private lateinit var name: String
    // posting content
    private lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrayBinding.inflate(layoutInflater)
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
            name = binding.prayTitle.text.toString()
            content = binding.prayContent.text.toString()
            when {
                title.isEmpty() -> {
                    Toast.makeText(applicationContext, "Enter title info", Toast.LENGTH_SHORT).show()
                    binding.prayTitle.requestFocus()
                }
                content.isEmpty() -> { // when content is empty
                    Toast.makeText(applicationContext, "Enter content info", Toast.LENGTH_SHORT).show()

                    binding.prayContent.requestFocus()
                }
                else -> {
                    uploadPrayer(name, content)
                }
            }
        }
    }

    /**
     * Upload announce on firebasestore
     */
    private fun uploadPrayer(name: String, content: String){
        // current date
        val currentDate = Calendar.getInstance().time
        // convert date into string
        val date = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault()).format(currentDate)
        // firestore document name
        val fileName = "$name $date"

        val prayerMap = hashMapOf(
                "name" to name,
                "content" to content,
                "date" to date)

        fireStore.collection("Prayer").document(fileName).set(prayerMap)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                    // clear edit text and request focus
                    binding.prayTitle.text = null
                    binding.prayContent.text = null
                    binding.prayTitle.requestFocus()
                }
    }
}