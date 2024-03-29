package com.proyecto.tasksnotes.recovery

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.proyecto.tasksnotes.Login
import com.proyecto.tasksnotes.Menu
import com.proyecto.tasksnotes.databinding.ActivityEmailVerificationBinding
import com.proyecto.tasksnotes.model.User

class EmailVerification : AppCompatActivity() {

    private lateinit var binding: ActivityEmailVerificationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var referenceTemp: DatabaseReference
    private lateinit var emailTemp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        referenceTemp = FirebaseDatabase.getInstance().getReference("usersTemp")

        binding.veficateEmailButton.setOnClickListener {

            currentUser = auth.currentUser!!
            val profileUpdates = userProfileChangeRequest { }

            currentUser.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentUser.isEmailVerified) {
                        addUserIfContinue()
                    } else {
                        Toast.makeText(
                            this,
                            "Por favor, verifique su email para continuar.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.tvButtonResend.setOnClickListener {
            sendEmailVerification()
        }

        binding.verificationLogOutButton.setOnClickListener {
            currentUser = auth.currentUser!!
            val profileUpdates = userProfileChangeRequest { }

            currentUser.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentUser.isEmailVerified) {
                        addUserIfLogOut()
                    } else {
                        logOutApp()
                    }

                }
            }

        }
    }

    //Recoger datos de usertemp y añadirlos a la base de datos si el usuario continua después de verificar //Despues borrar el user en userTEmp para limpiar
    private fun addUserIfContinue() {

        emailTemp = auth.currentUser!!.email.toString()
        var queryName = ""
        var querySurname = ""

        val query = referenceTemp.orderByChild("email").equalTo(emailTemp)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    queryName = ds.child("name").value.toString()
                    querySurname = ds.child("surname").value.toString()
                }

                val uid = auth.uid
                val email = auth.currentUser?.email
                val user = User(uid, queryName, querySurname, email)
                val emailPath = auth.currentUser?.email!!.replace(".", ",")

                databaseReference.child(emailPath).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Cuenta creada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        referenceTemp.child(emailPath).removeValue()
                        startActivity(Intent(applicationContext, Menu::class.java))
                        finish()
                    }.addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "" + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    //Recoger datos de usertemp y añadirlos a la base de datos si el usuario sale después de verificar //Despues borrar el user en userTEmp para limpiar
    private fun addUserIfLogOut() {

        emailTemp = auth.currentUser!!.email.toString()
        var queryName = ""
        var querySurname = ""

        val query = referenceTemp.orderByChild("email").equalTo(emailTemp)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    queryName = ds.child("name").value.toString()
                    querySurname = ds.child("surname").value.toString()
                }

                val uid = auth.uid
                val email = auth.currentUser?.email
                val user = User(uid, queryName, querySurname, email)
                val emailPath = auth.currentUser?.email!!.replace(".", ",")

                databaseReference.child(emailPath).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Gracias por verificar su cuenta, inicie sesión cuando quiera",
                            Toast.LENGTH_SHORT
                        ).show()
                        referenceTemp.child(emailPath).removeValue()
                        auth.signOut()
                        startActivity(Intent(applicationContext, Login::class.java))
                        finish()
                    }.addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "" + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    private fun sendEmailVerification() {
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "Se ha enviado un correo de verificación",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this, "Debes esperar un poco para que " +
                            "podamos enviarte otro correo de verificación.",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private fun logOutApp() {
        auth.signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
        Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
    }

}