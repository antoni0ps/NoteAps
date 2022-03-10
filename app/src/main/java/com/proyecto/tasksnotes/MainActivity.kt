package com.proyecto.tasksnotes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.proyecto.tasksnotes.databinding.ActivityMainBinding
import com.proyecto.tasksnotes.recovery.AccountRecoveryActivity
import com.proyecto.tasksnotes.recovery.EmailVerificationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(
                        this, "Aún faltan campos por rellenar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    SignIn(email, password)
                }
            }
        }

        binding.buttonRegister.setOnClickListener {
            goToRegister()
        }

        binding.tvRecoveryAccount.setOnClickListener {
            startActivity(Intent(this, AccountRecoveryActivity::class.java))
        }
    }

    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkVerification()
                } else {
                    Toast.makeText(
                        this, "Correo o contraseña icorrectos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkVerification() {
        val firebaseUser = auth.currentUser

        if (firebaseUser!!.isEmailVerified) {
            goToMenu()
            finish()
        } else {
            sendEmailVerification()
            startActivity(Intent(this, EmailVerificationActivity::class.java))
            finish()
        }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "Se envio un correo de verificación",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun goToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        this.startActivity(intent)
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}