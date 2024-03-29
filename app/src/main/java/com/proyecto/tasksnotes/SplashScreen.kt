package com.proyecto.tasksnotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.tasksnotes.recovery.EmailVerification

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        val time = 3800
        Handler(Looper.getMainLooper()).postDelayed({ checkUser() },time.toLong())
    }

    // Funcion para comprobar si hay algun usuario logueado y enviar a la activity correspondiente
    private fun checkUser() {
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
        } else if (firebaseUser != null && !firebaseUser.isEmailVerified) {
            startActivity(Intent(this, EmailVerification::class.java))
            finish()
        } else {
            startActivity(Intent(this, Menu::class.java))
            finish()
        }
    }

}