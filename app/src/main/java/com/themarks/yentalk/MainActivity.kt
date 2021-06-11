package com.themarks.yentalk

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.themarks.yentalk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mediaPlayer: MediaPlayer
    private val rcSignIn = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize authentication
        auth = FirebaseAuth.getInstance()

        // get media player
        mediaPlayer = MediaPlayer.create(this,R.raw.bgm)
        mediaPlayer.isLooping
        // start bgm
        mediaPlayer.start()

        // animation for title
        val animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        binding.main.startAnimation(animation)
        binding.submain.startAnimation(animation)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Google login
        binding.google.setOnClickListener {
            googleLogIn()
        }

    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // back button pressed
    override fun onBackPressed() {
        super.onBackPressed()

        // stop BGM
        mediaPlayer.stop()
    }

    override fun onPause() {
        super.onPause()

        // pause BGM
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()

        // restart BGM
        mediaPlayer.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == rcSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(applicationContext,"Login failed\nPlease try again", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext,"Sign into Google account first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * When succeed to login, go to next activity, image upload activity
     */
    private fun updateUI(currentuser: FirebaseUser?){
        if(currentuser != null){
            val goMain = Intent(this, SubMainActivity::class.java)

            startActivity(goMain)
            // turn off bgm
            mediaPlayer.stop()
            finish()
        }
    }

    /**
     *  Google Login
     */
    private fun googleLogIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        updateUI(null)
                    }
                }
    }
}