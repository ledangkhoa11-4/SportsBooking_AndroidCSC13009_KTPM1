package com.example.sportbooking_owner

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignIn : AppCompatActivity() {
    var signInBtn:Button?=null
    var signInGGBtn:Button?=null
    var signInFbBtn:Button?=null
    var signUp:Button?=null
    var mGoogleSignInClient:GoogleSignInClient?=null
    private lateinit var auth: FirebaseAuth
    lateinit var emailEdt:TextInputLayout
    lateinit var passwordEdt:TextInputLayout
    var currentUser:User_Owner?=null
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private lateinit var  oneTapClient:SignInClient
    private lateinit var signInRequest:BeginSignInRequest
    private lateinit var login_btn:LoginButton
    lateinit var callbackManager : CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val database = Firebase.database
        signInBtn=findViewById<Button>(R.id.SignInBtn)
        signInGGBtn=findViewById<Button>(R.id.SignInGGBtn)
        signInFbBtn=findViewById<Button>(R.id.SignInFbBtn)
        signUp=findViewById<Button>(R.id.SignUpBtn)
        emailEdt=findViewById(R.id.LayoutEmail)
        passwordEdt=findViewById(R.id.LayoutPassword)
        auth = Firebase.auth
        login_btn=findViewById(R.id.login_button)

        //Sign Up
        signUp!!.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }

        //Sign In

        signInBtn!!.setOnClickListener {
            val email= emailEdt.editText?.text.toString()
            val password=passwordEdt.editText?.text.toString()
            if (email!="" && password!="" ){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            val user = auth.currentUser
                            currentUser= User_Owner(user!!.uid,user.displayName,user.email)
                            startActivity(Intent(this, CourtListActivity::class.java))
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }

        }
        //Sign In with google
         signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        oneTapClient = Identity.getSignInClient(this)


        signInGGBtn!!.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, REQ_ONE_TAP,
                            null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("TAG", e.localizedMessage)
                    Log.e("TAG", GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE.toString())
                }

        }

        //Sign In with Facebook
        callbackManager = CallbackManager.Factory.create();

        login_btn.setReadPermissions("email", "public_profile")
        login_btn.registerCallback(callbackManager, object :FacebookCallback<LoginResult>{
            override fun onCancel() {
                Log.i("tag","facebook:onCancel")
            }
                override fun onError(error: FacebookException) {
                    Log.i("tag","\nfacebook:onError")
                }



                override fun onSuccess(result: LoginResult) {
                    Log.i("tag","\nfacebook:onSuccess:$result")
                    val accessToken = result.accessToken
                            accessToken?.let {
                                handleFacebookAccessToken(it)
                            }

                }
            })
        signInFbBtn!!.setOnClickListener {
            login_btn.performClick()
        }


    }
    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
    // Sign in success, update UI with the signed-in user's info
                    Log.i("tag","\nsignInWithCredential:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
    // If sign in fails, display a message to the user.
                    Log.i("tag","\nsignInWithCredential:failure")
                    Log.i("tag","\n${task.exception}")
                    //updateUI(null)
                }
            }
    }
    private fun updateUI(user: User_Owner?) {
        startActivity(Intent(this, CourtListActivity::class.java))
    }


    public override fun onStart() {
        super.onStart()
        //auth.signOut()
         //Check if user is signed in (non-null) and update UI accordingly.
//        val user = auth.currentUser
//        currentUser=User_Owner(user?.uid,user?.displayName.toString(),user?.email)
//        if(currentUser != null){
//           startActivity(Intent(this, CourtListActivity::class.java))
//        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ONE_TAP -> {
                try{
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = googleCredential.googleIdToken

                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithCredential:success")
                                        val user = auth.currentUser
                                        currentUser= User_Owner(user!!.uid,user.displayName,user.email)
                                        startActivity(Intent(this, CourtListActivity::class.java))
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithCredential:failure", task.exception)

                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                }catch (e: ApiException) {
                    Log.d("TAG", e.message.toString())
                }
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)


    }
    fun updateUserProfile(newUser:User_Owner){
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = newUser.username
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }
    }




