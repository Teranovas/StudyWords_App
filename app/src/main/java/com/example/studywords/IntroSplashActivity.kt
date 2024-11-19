package com.example.studywords

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class IntroSplashActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_splash)

        val logoImage: ImageView = findViewById(R.id.logoImage)
        val loginBtn: SignInButton = findViewById(R.id.loginBtn);
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.clear(R.id.logoImage, ConstraintSet.BOTTOM)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            if (auth.getCurrentUser() != null) {
                val intent = Intent(application, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            else{
                constraintLayout.post {
                    constraintSet.applyTo(constraintLayout)  // 하단 제약 조건을 제거하고 변경 사항 적용

                    // 이미지 뷰를 중간에서 시작하여 위로 15dp 만큼 이동
                    val middleY = constraintLayout.height / 2f - logoImage.height / 2f
                    val toYDelta = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
                    val toY = -toYDelta
                    val moveAnimation = TranslateAnimation(0f, 0f, middleY, toY)


                    moveAnimation.duration = 500
                    moveAnimation.fillAfter = true  // 애니메이션이 끝난 위치에서 정지
                    logoImage.startAnimation(moveAnimation)

                    // 로그인 버튼을 보여주기 위한 애니메이션
                    loginBtn.visibility = Button.VISIBLE
                    val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                    loginBtn.startAnimation(fadeIn)
                }

            }
        },3000)


        loginBtn.setOnClickListener {
            signIn()
        }

        // 3초 뒤 애니메이션 시작
//        Handler().postDelayed({
//         constraintLayout.post {
//                constraintSet.applyTo(constraintLayout)  // 하단 제약 조건을 제거하고 변경 사항 적용
//
//                // 이미지 뷰를 중간에서 시작하여 위로 15dp 만큼 이동
//                val middleY = constraintLayout.height / 2f - logoImage.height / 2f
//                val toYDelta = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
//                val toY = -toYDelta
//                val moveAnimation = TranslateAnimation(0f, 0f, middleY, toY)
//
//
//                moveAnimation.duration = 500
//                moveAnimation.fillAfter = true  // 애니메이션이 끝난 위치에서 정지
//                logoImage.startAnimation(moveAnimation)
//
//                // 로그인 버튼을 보여주기 위한 애니메이션
//                loginBtn.visibility = Button.VISIBLE
//                val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
//                loginBtn.startAnimation(fadeIn)
//            }
//
//
//
//        },3000)


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {


                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account)

            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.getIdToken(), null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 메인 액티비티로 이동
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }


}