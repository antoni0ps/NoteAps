package com.proyecto.tasksnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.database.*
import com.proyecto.tasksnotes.model.User
import com.proyecto.tasksnotes.databinding.ActivityRegisterBinding
import com.proyecto.tasksnotes.recovery.EmailVerification
import java.util.regex.Pattern

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var name: String
    private lateinit var surname: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("users")


        binding.registerButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {

        name = binding.editTextName.text.toString().trim()
        surname = binding.editTextSurname.text.toString().trim()
        email = binding.editTextEmail.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()



        val passwordRegex = Pattern.compile(
            "^" + ".{6,}" + "$"
        )


        if (name.isEmpty() || surname.isEmpty()) {
            Toast.makeText(this, "Ingrese nombre y apellidos", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty() || !passwordRegex.matcher(password).matches()) {
            Toast.makeText(this, "Contraseña mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Confirme contraseña", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
        } else {
            createAccount(email, password)
        }
    }


    private fun createAccount(email: String, password: String) {

        var queryEmail = ""
        val query = databaseReference.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    queryEmail = ds.child("email").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    sendEmailVerification()
                    addTempUser()
                } else if (email == queryEmail) {
                    Toast.makeText(this, "Este email ya está registrado," +
                            " por favor pruebe con otro diferente.",
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this, "Error de autenticación",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Se ha enviado un correo de verificación",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTempUser() {
        val uid = auth.uid
        val user = User(uid, name, surname, email)
        val emailPath = auth.currentUser?.email!!.replace(".", ",")
        val databaseReference = FirebaseDatabase.getInstance().getReference("usersTemp")
        databaseReference.child(emailPath).setValue(user)
            .addOnSuccessListener {
                startActivity(Intent(this,EmailVerification::class.java))
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }

    }

//    private fun sendData(){
//        val intent = Intent(this, EmailVerificationActivity::class.java)
//        name = binding.editTextName.text.toString().trim()
//        surname = binding.editTextSurname.text.toString().trim()
//        intent.putExtra("name", name)
//        intent.putExtra("surname", surname)
//        startActivity(intent)
//        finish()
//    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}