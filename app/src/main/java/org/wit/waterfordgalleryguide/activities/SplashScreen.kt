package org.wit.waterfordgalleryguide.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.views.login.LoginView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreeen)

        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this@SplashScreen, LoginView::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}