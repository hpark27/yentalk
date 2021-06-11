package com.themarks.yentalk

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.themarks.yentalk.databinding.ActivitySubmainBinding

class SubMainActivity:AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivitySubmainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var pdfUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize authentication
        auth = FirebaseAuth.getInstance()
        // initialize firebase storage
        firebaseStorage = FirebaseStorage.getInstance()

        binding.mainNavigation.setNavigationItemSelectedListener(this)

        // open drawer
        binding.menu.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }

        // community
        binding.community.setOnClickListener {
            val community = Intent(this,CommunityActivity::class.java)
            startActivity(community)
        }

        // meeting
        binding.map.setOnClickListener {
            val meeting = Intent(this,MeetingActivity::class.java)
            startActivity(meeting)
        }

        // download pdf
        binding.bulletin.setOnClickListener {
            readPdf()
        }

        // notice
        binding.notice.setOnClickListener {
            val notice = Intent(this, NoticeViewActivity::class.java)
            startActivity(notice)
        }

        // prayer
        binding.pray.setOnClickListener{
            val pray = Intent(this, PrayViewActivity::class.java)
            startActivity(pray)
        }

        // new comer
        binding.newbie.setOnClickListener {
            val newbie = Intent(this,NewComerViewActivity::class.java)
            startActivity(newbie)
        }

        // Attribute click characterlistic on bottom navigation menu items
        binding.botNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                // open youtube channel
                R.id.youtube -> openYoutube()
                // open instagram channel
                R.id.instagram -> openInstagram()
            }
            true
        }
    }

    /**
     * Navigation menu item is selected
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> loginEdit()

            R.id.upload -> loginUpload()

            R.id.logout -> logOut()
        }

        // close navigation bar
        binding.mainDrawer.closeDrawers()
        
        return true
    }

    /**
     *  Get Pdf Url
     */
    private fun readPdf(){
        // reference to firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Admin").child("Pdfurl")

        ref.get().addOnSuccessListener {
            pdfUri = it.value.toString()

            if(pdfUri.isEmpty()){
                Toast.makeText(applicationContext,"Bulletin unavailable now\nPlease try again later", Toast.LENGTH_SHORT).show()
            }else{
                val read = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUri))
                startActivity(read)
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,"Bulletin unavailable now\nPlease try again later", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *  Go to login activity
     */
    private fun loginEdit(){
        val login = Intent(this,LoginActivity::class.java)
        login.putExtra("Login",1)
        startActivity(login)
    }

    /**
     *  Go to login activity
     */
    private fun loginUpload(){
        val login = Intent(this,LoginActivity::class.java)
        login.putExtra("Login",2)
        startActivity(login)
    }

    /**
     * Logout intent
     */
    private fun logOut(){
        // get current user
        var user = auth.currentUser

        if(user!=null){
            auth.signOut()

            // get current user
            user = auth.currentUser

            if(user == null){
                Toast.makeText(applicationContext,"Logout successful",Toast.LENGTH_SHORT).show()
            }else{
                Toast.
                makeText(applicationContext,"Something went wrong\nPlease try again",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(applicationContext,"Please sign in first",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Open Youtube App
     */
    private fun openYoutube(){
        // get string from string resource
        val themarks = getString(R.string.lolmc_youtube)
        // get uri from string
        val themarksUri: Uri = Uri.parse(themarks)
        // intent
        val themarksIntent = Intent(Intent.ACTION_VIEW,themarksUri)
        // app package name
        val youtubePackage = getString(R.string.youtube_package)
        // app chooser title
        val appOpenTitle: String = getString(R.string.app_open)
        themarksIntent.setPackage(youtubePackage)
        // try catch with youtube package

        try {
            // when youtube app is installed
            startActivity(themarksIntent)
        } catch (e: ActivityNotFoundException) { // when youtube app does not exist
            // webview intent
            val themarksWebIntent = Intent(Intent.ACTION_VIEW, themarksUri)
            // create app chooser
            val appChooserYoutube: Intent =
                Intent.createChooser(themarksWebIntent, appOpenTitle)
            startActivity(appChooserYoutube)
        }
    }

    /**
     * Open Instagram App
     */
    private fun openInstagram(){
        // get string from string resource
        val instagram = getString(R.string.lolmc_instagram)
        // get uri from string
        val instagramUri: Uri = Uri.parse(instagram)
        // intent
        val instagramIntent = Intent(Intent.ACTION_VIEW, instagramUri)
        // instagram package name
        val instagramPackage = getString(R.string.instagram_package)
        // app chooser title
        val appOpenTitle: String = getString(R.string.app_open)
        instagramIntent.setPackage(instagramPackage)
        // try catch with youtube package
        try {
            // when youtube app is installed
            startActivity(instagramIntent)
        } catch (e: ActivityNotFoundException) { // when youtube app does not exist
            // webview intent
            val instagramWebIntent = Intent(Intent.ACTION_VIEW, instagramUri)
            // create app chooser
            val appChooserInstagram: Intent =
                Intent.createChooser(instagramWebIntent, appOpenTitle)
            startActivity(appChooserInstagram)
        }
    }
}