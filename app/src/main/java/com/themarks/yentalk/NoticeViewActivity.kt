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
import com.themarks.yentalk.databinding.ActivityNoticeviewBinding
import com.themarks.yentalk.databinding.NoticeviewBinding

class NoticeViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNoticeviewBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore = FirebaseFirestore.getInstance()

        binding.noticeviewRe.adapter = ViewAdapter()
        binding.noticeviewRe.layoutManager = LinearLayoutManager(this)

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
        private val contents: ArrayList<Notice> = ArrayList()

        init{
            fireStore?.collection("Announcement")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = Notice(document["title"] as String?,
                                    document["content"] as String?, document["date"] as String?)

                            contents.add(0,item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, "Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: NoticeviewBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(NoticeviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // title
            holder.binding.title.text = contents[position].title
            // content
            holder.binding.content.text = contents[position].content
            // date
            holder.binding.date.text = contents[position].date

            // open notices in other activity
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val notice = Intent(context, NoticeContentActivity::class.java)
                // deliver the posting info on intent
                notice.putExtra("title",contents[position].title)
                notice.putExtra("content",contents[position].content)
                startActivity(notice)
            }
        }

        override fun getItemCount(): Int {
            return contents.size
        }
    }
}