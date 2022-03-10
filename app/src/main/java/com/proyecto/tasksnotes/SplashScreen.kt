package com.proyecto.tasksnotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.tasksnotes.recovery.EmailVerificationActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        val time = 3800

        Handler(Looper.getMainLooper()).postDelayed({
            checkUser()
        }, time.toLong())
    }

    // Funcion para comprobar si hay algun usuario logueado y enviar a la activity correspondiente
    private fun checkUser() {
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if (firebaseUser != null && !firebaseUser.isEmailVerified) {
            sendEmailVerification()
            startActivity(Intent(this, EmailVerificationActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Se envio un correo de verificación",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}