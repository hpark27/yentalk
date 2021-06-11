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
import com.themarks.yentalk.databinding.ActivityNewcomereditBinding
import com.themarks.yentalk.databinding.VisitoreditBinding

class NewComerEditActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewcomereditBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewcomereditBinding.inflate(layoutInflater)
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

    inner class ViewAdapter : RecyclerView.Adapter<NewComerEditActivity.ViewAdapter.ViewHolder>(){
        // array lists for content informations
        private val contents: ArrayList<NewComerEdit> = ArrayList()

        init{
            fireStore?.collection("Visitor")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = NewComerEdit(document["name"] as String?)

                            contents.add(0,item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, "Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: VisitoreditBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(VisitoreditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // get fileName
            val fileName = contents[position].name.toString()
            // title
            holder.binding.name.text = contents[position].name

            // delete file
            holder.binding.trashcan.setOnClickListener {
                fireStore?.collection("Visitor")?.document(fileName)?.delete()?.addOnSuccessListener {
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