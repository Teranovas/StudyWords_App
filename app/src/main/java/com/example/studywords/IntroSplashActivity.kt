package com.example.studywords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible

class IntroSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_splash)

        val logoImage: ImageView = findViewById(R.id.logoImage)
        val loginBtn: Button = findViewById(R.id.loginBtn);

        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.clear(R.id.logoImage, ConstraintSet.BOTTOM)



        // 3초 뒤 애니메이션 시작
        Handler().postDelayed({
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



        },3000)


    }
}