package com.themarks.yentalk

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.themarks.yentalk.databinding.ActivityNewcomerBinding

class NewComerActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewcomerBinding
    // firebase storage
    private lateinit var storage: FirebaseStorage
    // firebase firestore
    private lateinit var fireStore: FirebaseFirestore
    // name
    private var name: String = ""
    // birth
    private var birth: String = ""
    // address
    private var address: String = ""
    // contact
    private var contact: String = ""
    // gender
    private var gender: String = ""
    // baptism
    private var baptism: String = ""
    // new believer
    private var believer: String = ""
    // ride
    private var ride: String = ""
    // firebase download url
    private var fdownloadUrl: String = ""
    // image url
    private var urlString: String = ""
    // pick image file
    private val pickImage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewcomerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize Firebase storage
        storage = FirebaseStorage.getInstance()
        // initialize Firebase Database
        fireStore = FirebaseFirestore.getInstance()

        binding.back.setOnClickListener {
            finish()
        }

        // choose image
        binding.profile.setOnClickListener {
            pick()
        }
        
        // post on firestore
        binding.upload.setOnClickListener {
            when {
                binding.profileName.text.isEmpty() -> {
                    Toast.makeText(applicationContext,"Enter name",Toast.LENGTH_SHORT).show()

                    binding.profileName.requestFocus()
                }
                binding.profileBirth.text.isEmpty() -> {
                    Toast.makeText(applicationContext,"Enter date of birth",Toast.LENGTH_SHORT).show()

                    binding.profileBirth.requestFocus()
                }
                binding.profileAddress.text.isEmpty() -> {
                    Toast.makeText(applicationContext,"Enter address",Toast.LENGTH_SHORT).show()

                    binding.profileAddress.requestFocus()
                }
                binding.profileContact.text.isEmpty() -> {
                    Toast.makeText(applicationContext,"Enter contact",Toast.LENGTH_SHORT).show()

                    binding.profileContact.requestFocus()
                }
                else -> {
                    name = binding.profileName.text.toString()
                    birth = binding.profileBirth.text.toString()
                    address = binding.profileAddress.text.toString()
                    contact = binding.profileContact.text.toString()

                    if(binding.male.isChecked){
                        gender = "Man"
                    }

                    if(binding.female.isChecked){
                        gender = "Woman"
                    }

                    believer = if(binding.beginner.isChecked){
                        "Yes"
                    }else{
                        "No"
                    }

                    baptism = if(binding.baptism.isChecked){
                        "Yes"
                    }else{
                        "No"
                    }

                    ride = if(binding.ride.isChecked){
                        "Yes"
                    }else{
                        "No"
                    }

                    if((binding.male.isChecked)&&(binding.female.isChecked)){
                        Toast.makeText(applicationContext,"Select right gender",Toast.LENGTH_SHORT).show()
                    }else if((!binding.male.isChecked)&&(!binding.female.isChecked)){
                        Toast.makeText(applicationContext,"Select gender",Toast.LENGTH_SHORT).show()
                    } else if((binding.baptism.isChecked)&&(binding.beginner.isChecked)){
                        Toast.makeText(applicationContext,"Select right one",Toast.LENGTH_SHORT).show()
                    }else{
                        if(urlString.isEmpty()){
                            Toast.makeText(applicationContext,"Choose image", Toast.LENGTH_SHORT).show()
                        }else{
                            uploadContact()
                        }
                    }
                }
            }
        }
    }

    /**
     * Pick images from gallery
     */
    private fun pick(){
        val pick = Intent()
        // set intent type
        pick.type = "image/*"
        // get content
        pick.action = Intent.ACTION_PICK
        // start intent
        startActivityForResult(pick, pickImage)
    }

    /**
     *  Upload new comer info on firebase store
     */
    private fun uploadContact(){
        // file name
        val fileName = name
        // get firebase storage reference
        val ref = FirebaseStorage.getInstance().getReference("Visitor/$fileName")
        // upload task
        val upload: StorageTask<*>

        if(urlString.isNotEmpty()){
            val url = Uri.parse(urlString)
            upload = ref.putFile(url)

            upload.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // get image download url from firebase
                    fdownloadUrl = task.result.toString()

                    val announceMap = hashMapOf(
                        "name" to name,
                        "birth" to birth,
                        "address" to address,
                        "contact" to contact,
                        "gender" to gender,
                        "baptism" to baptism,
                        "believer" to believer,
                        "ride" to ride,
                        "image" to fdownloadUrl)

                    fireStore.collection("Visitor").document(fileName).set(announceMap)
                        .addOnCompleteListener {
                            Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                            // clear edit text
                            binding.profileName.text = null
                            binding.profileBirth.text = null
                            binding.profileAddress.text = null
                            binding.profileContact.text = null

                            // uncheck check boxes
                            binding.male.isChecked = false
                            binding.female.isChecked = false
                            binding.beginner.isChecked = false
                            binding.baptism.isChecked = false
                            binding.ride.isChecked = false

                            binding.profile.setImageResource(android.R.color.transparent)
                            binding.profileName.requestFocus()
                        }
                } else {
                    Toast.makeText(applicationContext, "Something went wrong\nPleasetry again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // photo result from start activity for result intent and default camera app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        // pick image from gallery
        if(requestCode == pickImage){
            if(resultCode == Activity.RESULT_OK){
                if(data!=null){
                    val url = data.data
                    urlString = url.toString()
                    // show image on imageview
                    Glide.with(this).load(urlString).into(binding.profile)
                }else{
                    Toast.makeText(applicationContext,"Something went wrong\nPlease try again",Toast.LENGTH_SHORT).show()
                }
            }else{
                // finish
                finish()
            }
        }
    }
}