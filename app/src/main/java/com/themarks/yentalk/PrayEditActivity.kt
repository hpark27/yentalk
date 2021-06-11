package com.themarks.yentalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.themarks.yentalk.databinding.ActivityPrayeditBinding
import com.themarks.yentalk.databinding.PrayeditBinding

class PrayEditActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPrayeditBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrayeditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore = FirebaseFirestore.getInstance()

        binding.prayviewRe.adapter = ViewAdapter()
        binding.prayviewRe.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        snapshot?.remove()
    }

    inner class ViewAdapter : RecyclerView.Adapter<PrayEditActivity.ViewAdapter.ViewHolder>(){
        // array lists for content informations
        private val contents: ArrayList<Pray> = ArrayList()

        init{
            fireStore?.collection("Prayer")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = Pray(document["name"] as String?,
                                    document["content"] as String?, document["date"] as String?)

                            contents.add(item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, "Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: PrayeditBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(PrayeditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // name
            val name = contents[position].name.toString()
            // date
            val date = contents[position].date.toString()
            // get fileName
            val fileName = "$name $date"
            // name
            holder.binding.title.text = name
            // content
            holder.binding.content.text = contents[position].content

            // delete file
            holder.binding.trashcan.setOnClickListener {
                fireStore?.collection("Prayer")?.document(fileName)?.delete()?.addOnSuccessListener {
                    Toast.makeText(applicationContext,"File deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }?.addOnFailureListener {
                    Toast.makeText(applicationContext, "Something went wrong.\nPlease try again later", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int {
            return contents.size
        }
    }
}