package com.themarks.yentalk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.themarks.yentalk.databinding.ActivityPrayviewBinding
import com.themarks.yentalk.databinding.PrayviewBinding

class PrayViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPrayviewBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrayviewBinding.inflate(layoutInflater)
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

    inner class ViewAdapter : RecyclerView.Adapter<ViewAdapter.ViewHolder>(){
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

        inner class ViewHolder(val binding: PrayviewBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(PrayviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // get date
            val date = contents[position].date

            // title
            holder.binding.title.text = contents[position].name
            // content
            holder.binding.content.text = contents[position].content
            // date
            holder.binding.date.text = date

            // open notices in other activity
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val notice = Intent(context, NoticeContentActivity::class.java)
                // deliver the posting info on intent
                notice.putExtra("title",contents[position].name)
                notice.putExtra("content",contents[position].content)
                startActivity(notice)
            }
        }

        override fun getItemCount(): Int {
            return contents.size
        }
    }
}