package com.themarks.yentalk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.themarks.yentalk.databinding.ActvityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActvityLoginBinding
    private lateinit var data: FirebaseDatabase
    private var intentData: Int = 0
    // check if permissions are allowed
    private var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActvityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Permission checking activity
        setPermission()

        // initialize database
        data = FirebaseDatabase.getInstance()

        intentData = intent.getIntExtra("Login",0)

        // go back
        binding.back.setOnClickListener{
            finish()
        }

        // go to manage activity
        binding.login.setOnClickListener {
            if(num==1){
                logIn()
            }else{
                Toast.makeText(applicationContext,"Please allow permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Set up tedpermission
     */
    private fun setPermission() {
        val permission = object: PermissionListener {
            // permission is granted
            override fun onPermissionGranted() {
                num = 1
                // toast message
                Toast.makeText(this@LoginActivity, "Permission granted",Toast.LENGTH_SHORT).show()
            }
            //permission is denied
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                // toast message
                Toast.makeText(this@LoginActivity, "Please allow permission", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setDeniedMessage("Please allow permission")
            .setGotoSettingButtonText("Go to permission setting")
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    /**
     *  Login
     */
    private fun logIn(){
        // get password
        val text = binding.password.text.toString()

        // reference to firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Admin").child("Password")

        ref.get().addOnSuccessListener {
            val password = it.value.toString()

            if(text.isEmpty()){
                Toast.makeText(applicationContext, "Enter Password", Toast.LENGTH_SHORT).show()
                binding.password.requestFocus()
            }else {
                if(text == password){
                    val manage = Intent(this,ManageActivity::class.java)

                    if(intentData==1){
                        manage.putExtra("Manage",1)
                    }

                    if(intentData==2){
                        manage.putExtra("Manage",2)
                    }

                    startActivity(manage)
                    finish()
                }else{
                    Toast.makeText(applicationContext,"Please try again",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}