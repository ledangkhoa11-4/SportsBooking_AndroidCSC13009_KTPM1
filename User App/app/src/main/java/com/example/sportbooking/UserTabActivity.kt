package com.example.sportbooking

import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.sportbooking.Ultils.CreateToast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserTabActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    lateinit var editBtn: Button
    lateinit var usernameTv:TextView
    lateinit var useremailTv:TextView
    lateinit var avatarIv:ImageView
    lateinit var messageListBtn:Button
    lateinit var logoutBtn:Button
    lateinit var facebookHiddenBtn: LoginButton
    lateinit var linkToFacebook:Button
    lateinit var linkToGoogle:Button
    lateinit var callbackManager:CallbackManager
    private lateinit var  oneTapClient: SignInClient
    private lateinit var signInRequest:BeginSignInRequest
    private var showOneTapUI=true
    private val REQ_ONE_TAP = 2
    val currentUser = MainActivity.user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_tab)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)
        editBtn = findViewById(R.id.editBtn)
        editBtn.setOnClickListener {
            startActivityForResult(Intent(this,EditInformationActivity::class.java),200)
        }

        usernameTv = findViewById(R.id.user_nameTv)
        useremailTv = findViewById(R.id.user_emailTv)
        avatarIv = findViewById(R.id.avatarTv)
        usernameTv.setText(currentUser.username)
        useremailTv.setText(currentUser.email)
        if(currentUser.Image!=null){
            avatarIv.setImageBitmap(currentUser.Image)
        }
        findViewById<Button>(R.id.favoriteCourtBtn).setOnClickListener {
            val intent = Intent(this, MyFavoriteActivity::class.java)
            startActivity(intent)
        }
        messageListBtn=findViewById(R.id.messageListBtn)
        messageListBtn.setOnClickListener {
            startActivity(Intent(this,ListMessage::class.java))
        }
        logoutBtn = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()
            LoginManager.getInstance().logOut()
            finish()
            startActivity(Intent(this, SignInActivity::class.java))
        }
        findViewById<Button>(R.id.changePassButton).setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        //Link to facebook
        facebookHiddenBtn = findViewById(R.id.linkFacebook)
        linkToFacebook = findViewById(R.id.linkFacebookBtn)

        callbackManager = CallbackManager.Factory.create();

        facebookHiddenBtn.setReadPermissions("email", "public_profile")
        facebookHiddenBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
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
                    val request = GraphRequest.newMeRequest(accessToken){
                        jsonObject,_->
                        run {
                            val email = jsonObject!!.getString("email")
                            if(email == currentUser.email){
                                handleFacebookAccessToken(it)
                            }else{
                                CreateToast.createToast(this@UserTabActivity, "Error", "The account's email does not match the email used to login",false)
                            }
                        }
                    }
                    val params = Bundle()
                    params.putString("fields", "email")
                    request.parameters = params
                    request.executeAsync()

                }
                LoginManager.getInstance().logOut()
            }
        })
        linkToFacebook.setOnClickListener {
            facebookHiddenBtn.performClick()
        }

        //Link Google
        linkToGoogle = findViewById(R.id.linkWithGoogle)

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
        linkToGoogle.setOnClickListener {
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
    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_user
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home->{
                    startActivity(Intent(this,HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.item_user->{
                    true
                }
                R.id.item_schedule->{
                    startActivity(Intent(this,MyBookingActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        usernameTv = findViewById(R.id.user_nameTv)
        useremailTv = findViewById(R.id.user_emailTv)
        avatarIv = findViewById(R.id.avatarTv)
        usernameTv.setText(currentUser.username)
        useremailTv.setText(currentUser.email)
        if(currentUser.Image!=null){
            avatarIv.setImageBitmap(currentUser.Image)
        }
    }
    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        Firebase.auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful) {
                    CreateToast.createToast(this@UserTabActivity, "Success", "This account was linked", true);
                } else {
                    CreateToast.createToast(this@UserTabActivity, "Failure", "This account was already linked", false);
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ONE_TAP -> {
                try{
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                    var emailLink = googleCredential.id
                    if(currentUser.email == emailLink){
                        val idToken = googleCredential.googleIdToken
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        Firebase.auth.currentUser!!.linkWithCredential(firebaseCredential)
                            .addOnCompleteListener(this){task->
                                if (task.isSuccessful) {
                                    CreateToast.createToast(this@UserTabActivity, "Success", "This account was linked", true);
                                } else {
                                    CreateToast.createToast(this@UserTabActivity, "Failure", "This account was already linked", false);
                                }
                            }
                    }else{
                        CreateToast.createToast(this@UserTabActivity, "Error", "The account's email does not match the email used to login",false)
                    }
                }catch (e: ApiException) {
                    CreateToast.createToast(this@UserTabActivity, "Error", e.stackTraceToString(),false)
                }
            }
        }
    }
}