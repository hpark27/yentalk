package com.themarks.yentalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.themarks.yentalk.databinding.ActivityNewcomerviewBinding
import com.themarks.yentalk.databinding.VisitorviewBinding

class NewComerViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewcomerviewBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewcomerviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore = FirebaseFirestore.getInstance()

        binding.newbieRe.adapter = ViewAdapter()
        binding.newbieRe.layoutManager = LinearLayoutManager(this)

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
        private val contents: ArrayList<NewComer> = ArrayList()

        init{
            fireStore?.collection("Visitor")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = NewComer(document["name"] as String?, document["birth"] as String?,
                                document["gender"] as String?, document["image"] as String?)

                            contents.add(0,item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, "Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: VisitorviewBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(VisitorviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // url
            val url = contents[position].image
            // title
            holder.binding.name.text = contents[position].name
            // content
            holder.binding.birth.text = contents[position].birth

            val gender = contents[position].gender

            if(gender=="Man"){
                holder.binding.male.visibility = View.VISIBLE
            }

            if(gender=="Woman"){
                holder.binding.female.visibility = View.VISIBLE
            }

            Glide.with(holder.itemView.context).load(url).into(holder.binding.profile)
        }

        override fun getItemCount(): Int {
            return contents.size
        }
    }
}