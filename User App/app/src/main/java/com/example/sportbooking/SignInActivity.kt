package com.example.sportbooking

import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.sportbooking.DTO.User
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    var signInBtn: Button?=null
    var signInGGBtn: Button?=null
    var signInFbBtn: Button?=null
    var signUp: Button?=null
    private val REQ_ONE_TAP = 2
    private lateinit var  oneTapClient: SignInClient
    private lateinit var signInRequest:BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private var showOneTapUI=true
    lateinit var emailEdt: TextInputLayout
    lateinit var passwordEdt: TextInputLayout
    lateinit var callbackManager:CallbackManager
    private lateinit var login_btn: LoginButton
    var currentUser: User?=null
    private lateinit var database:FirebaseDatabase
    companion object{
        var user=Firebase.auth.currentUser
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        signInBtn=findViewById<Button>(R.id.SignInBtn)
        signInGGBtn=findViewById<Button>(R.id.SignInGGBtn)
        signInFbBtn=findViewById<Button>(R.id.SignInFbBtn)
        signUp=findViewById<Button>(R.id.SignUpBtn)
        emailEdt=findViewById(R.id.LayoutEmail)
        passwordEdt=findViewById(R.id.LayoutPassword)
        login_btn=findViewById(R.id.login_button)
        auth = Firebase.auth
        database=Firebase.database
        //Sign Up
        signUp!!.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        //Sign In

        signInBtn!!.setOnClickListener {
            val loadingStatus = findViewById<CircularProgressIndicator>(R.id.loading)

            val email= emailEdt.editText?.text.toString()
            val password=passwordEdt.editText?.text.toString()
            if (email!="" && password!="" ){
                loadingStatus.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            CreateToast.createToast(this, "Sign in successfully","Welcome to Sport Booking", true)

                            user = auth.currentUser
                            if(user!=null){
                                startActivity(Intent(this, HomeActivity::class.java))
                                getUser(user!!.uid)
                                loadingStatus.visibility = View.INVISIBLE
                                finish()
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            CreateToast.createToast(this, "Error","Authentication Failed", false)

                        }
                        loadingStatus.visibility = View.INVISIBLE
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
            if(showOneTapUI){
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

        }
        //Sign In with facebook
        callbackManager = CallbackManager.Factory.create();

        login_btn.setReadPermissions("email", "public_profile")
        login_btn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
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
                    user = auth.currentUser
                    if(user!=null){
                        IsSignUp(user!!.uid)

                        startActivity(Intent(this, HomeActivity::class.java))



                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("tag","\nsignInWithCredential:failure")
                    Log.i("tag","\n${task.exception}")
                    //updateUI(null)
                }
            }
    }
    fun getUser(uid:String){

        val bookingRef = MainActivity.database.getReference("User");
        val queryRef = bookingRef.orderByChild("id").equalTo(uid)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val user_db = ds.getValue(User::class.java)
                    MainActivity.user = user_db!!
                    var imageRef = MainActivity.storageRef.child("user${user_db.id}")
                    val ONE_MEGABYTE: Long = 1024 * 1024 * 5
                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        user_db.Image = bitmap
                        Log.i("AAAAAAAAAAAAA","AAAAAAAAAAAAAAA")
                    }.addOnFailureListener {
                        // Handle any errors
                        Log.i("AAAAAAAAAAAAA",it.toString())
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun updateUI(user: User?) {
        startActivity(Intent(this, HomeActivity::class.java))
    }


    public override fun onStart() {
        super.onStart()
        auth.signOut()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val user = auth.currentUser
//        currentUser=User(user?.uid,user?.displayName.toString(),user?.email)
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

                                        user = auth.currentUser

                                        //Log.i("VerifyEmail", user!!.isEmailVerified.toString())
                                        if(user!=null){
                                            IsSignUp(user!!.uid)
                                            getUser(user!!.uid)
                                            startActivity(Intent(this, HomeActivity::class.java))
                                            finish()
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(ContentValues.TAG, "No ID token!")
                        }
                    }
                }catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(ContentValues.TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(ContentValues.TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(
                                ContentValues.TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                }
            }
        }
    }

    fun IsSignUp(id:String){
        val userOwnerQuery=database.reference.child("User").orderByChild("id").equalTo(id)
        userOwnerQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val numOwner=snapshot.childrenCount
                if(numOwner<1){
                    val owner= User(user!!.uid, user!!.displayName!!, user!!.email!!)
                    val ownerRef=database.reference.child("User")
                    ownerRef.push().setValue(owner)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}