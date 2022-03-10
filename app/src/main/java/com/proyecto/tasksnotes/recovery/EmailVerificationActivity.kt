package com.proyecto.tasksnotes.recovery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.MainActivity
import com.proyecto.tasksnotes.MenuActivity
import com.proyecto.tasksnotes.databinding.ActivityEmailVerificationBinding
import com.proyecto.tasksnotes.model.User

class EmailVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailVerificationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")


        binding.veficateEmailButton.setOnClickListener {

            currentUser = auth.currentUser!!
            val profileUpdates = userProfileChangeRequest { }

            currentUser.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentUser.isEmailVerified) {
                        addUser()
                        startActivity(Intent(this, MenuActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Por favor, verifique su email para continuar.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.verificationLogOutButton.setOnClickListener {
            logOutApp()
        }
    }

    private fun addUser() {
        val uid = auth.uid
        val email = auth.currentUser?.email
        val name = intent.getStringExtra("name")
        val surname = intent.getStringExtra("surname")
        val user = User(uid, name, surname, email)
        val emailPath = auth.currentUser?.email!!.replace(".", ",")

        databaseReference.child(emailPath).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }

    }

    private fun logOutApp() {
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
    }


}