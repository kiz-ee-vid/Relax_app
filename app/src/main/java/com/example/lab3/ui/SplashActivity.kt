package com.example.lab3.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab3.MainActivity
import com.example.lab3.R

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity_screen)
        supportActionBar?.hide()

        val backgroundImage: ImageView = findViewById(R.id.imageView)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.splash)
        backgroundImage.startAnimation(slideAnimation)

        Handler().postDelayed({

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        },2000)
    }
}