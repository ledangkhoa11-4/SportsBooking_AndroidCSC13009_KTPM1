package com.example.sportbooking_owner

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportbooking_owner.DTO.Courts
import com.example.sportbooking_owner.DTO.Message
import com.example.sportbooking_owner.DTO.User
import com.example.sportbooking_owner.DTO.User_Owner
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
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private lateinit var  oneTapClient:SignInClient
    private lateinit var signInRequest:BeginSignInRequest
    private lateinit var login_btn:LoginButton
    lateinit var callbackManager : CallbackManager
    lateinit var database:FirebaseDatabase
    lateinit var userRef:DatabaseReference
    companion object{
        var listCourt: ArrayList<Courts> = ArrayList()
        var user=Firebase.auth.currentUser
        var userList=ArrayList<User>()
        var lastCourtList: ArrayList<Courts> = ArrayList()
        var owner= User_Owner()
        fun loadCourtList() {

            var courtsRef = MainActivity.database.getReference("Courts")
            var query=courtsRef.orderByChild("ownerID").equalTo(user!!.uid)
            var valueEventListener:ValueEventListener=object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listCourt.clear()
                    for (child_snapshot in snapshot.children) {

                        val court: Courts? = child_snapshot.getValue(Courts::class.java)
                        for (imageName in court!!.Images) {
                            var imageRef = MainActivity.storageRef.child(imageName)
                            val ONE_MEGABYTE: Long = 1024 * 1024 * 5
                            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                court.bitmapArrayList.add(bitmap)
                                Log.i("AAAAAAAAAAAAAA","Changeeeeeeeeeee")
                                if (CourtListActivity.adapter != null) {
                                    CourtListActivity.adapter!!.notifyDataSetChanged()
                                }
                            }
                        }
                        listCourt.add(court)

                    }
                    lastCourtList.addAll(listCourt)
                    if (CourtListActivity.adapter != null) {
                        CourtListActivity.adapter!!.notifyDataSetChanged()
                    }
                    if (CourtListActivity.adapterSearchview != null) {
                        CourtListActivity.adapterSearchview!!.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            }

            query.addValueEventListener(valueEventListener)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        database = Firebase.database

        signInBtn=findViewById<Button>(R.id.SignInBtn)
        signInGGBtn=findViewById<Button>(R.id.SignInGGBtn)
        signInFbBtn=findViewById<Button>(R.id.SignInFbBtn)
        signUp=findViewById<Button>(R.id.SignUpBtn)
        emailEdt=findViewById(R.id.LayoutEmail)
        passwordEdt=findViewById(R.id.LayoutPassword)
        auth = Firebase.auth
        login_btn=findViewById(R.id.login_button)


        getAllUser()
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
                            user = auth.currentUser

                                if(!user?.isEmailVerified!!){
                                    getOwner(user!!.uid)
                                    startActivity(Intent(this, VerifyEmailActivity::class.java))
                                }
                            else{
                                    loadCourtList()
                                    startActivity(Intent(this, CourtListActivity::class.java))
                                    finish()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Email or password is not correct",
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
                     user = auth.currentUser
                    if(user!=null){
                        IsSignUp(user!!.uid)
                        getOwner(user!!.uid)
                        loadCourtList()
                        startActivity(Intent(this, CourtListActivity::class.java))
                        finish()
                    }

                } else {
    // If sign in fails, display a message to the user.
                    Log.i("tag","\nsignInWithCredential:failure")
                    Log.i("tag","\n${task.exception}")
                    //updateUI(null)
                }
            }
    }


    fun getAllUser(){
        MainActivity.database.getReference("User").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(ds  in snapshot.children){
                    val u=ds.getValue(User::class.java)
                    val imageRef=MainActivity.storageRef.child("user"+u!!.id)
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val bitmap=BitmapFactory.decodeByteArray(it,0,it.size)
                        u.Image=bitmap
                        // Data for "images/island.jpg" is returned, use this as needed
                    }.addOnFailureListener {
                        // Handle any errors
                    }

                    if (u != null) {
                        userList.add(u)
                    }
                }

            }
        }
        )

    }
    public override fun onStart() {
        super.onStart()
       // auth.signOut()
         //Check if user is signed in (non-null) and update UI accordingly.
//         user = auth.currentUser
//
//        if(user != null){
//            loadCourtList()
//            getOwner(user!!.uid)
//           startActivity(Intent(this, CourtListActivity::class.java))
//            finish()
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
                                                getOwner(user!!.uid)
                                                loadCourtList()
                                                startActivity(Intent(this, CourtListActivity::class.java))
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
                            Log.d(TAG, "No ID token!")
                        }
                    }
                }catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                }
            }
        }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)


    }
    fun getOwner(uid:String){
        val ownerRef=MainActivity.database.reference.child("Owner")
        ownerRef.orderByChild("id").equalTo(uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(child  in snapshot.children){
                   val owner_db=child.getValue(User_Owner::class.java)
                   if (owner_db != null) {
                       var imageRef = MainActivity.storageRef.child("owner${owner_db.id}")
                       val ONE_MEGABYTE: Long = 1024 * 1024 * 5
                       imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                           val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                           owner_db.Image = bitmap

                   }.addOnFailureListener { Log.w("Dowload","Failed") }
                       owner=owner_db
               }
            }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    fun updateUserProfile(newUser: User_Owner){
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
    fun IsSignUp(id:String){
        val userOwnerQuery=database.reference.child("Owner").orderByChild("id").equalTo(id)
        userOwnerQuery.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val numOwner=snapshot.childrenCount
                if(numOwner<1){
                    val owner= User_Owner(user!!.uid, user!!.displayName.toString(), user!!.email!!)
                    val ownerRef=database.reference.child("Owner")
                    ownerRef.push().setValue(owner)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    }




