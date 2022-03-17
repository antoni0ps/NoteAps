package com.proyecto.tasksnotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyecto.tasksnotes.about.About_Activity
import com.proyecto.tasksnotes.databinding.ActivityMenuBinding
import com.proyecto.tasksnotes.list.List_Events
import com.proyecto.tasksnotes.list.List_Notes
import com.proyecto.tasksnotes.list.List_Tasks

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var usuarios: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var dbStatus: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarios = FirebaseDatabase.getInstance().getReference("users")
        auth = FirebaseAuth.getInstance()
        dbStatus = FirebaseDatabase.getInstance().getReference(".info/connected")


        /* Llamamos a la funcion loadData al iniciar para comprobar si existe un usuario logueado,
        si existe el usuario se le envia al menu principal, en caso contrario se envía a la
        actividad de login */


        loadData()

        binding.myTasksButton.setOnClickListener {
            startActivity(Intent(this, List_Tasks::class.java))
        }

        binding.myNotesButton.setOnClickListener {
            startActivity(Intent(this, List_Notes::class.java))
        }

        binding.myEventsButton.setOnClickListener {
            startActivity(Intent(this, List_Events::class.java))
        }

        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, About_Activity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            logOutApp()
            finish()
        }
    }


    private fun loadData() {

        user = auth.currentUser!!

        dbStatus.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val connected = snapshot.getValue(Boolean::class.java)
                if (connected!!) {
                    val emailPath = auth.currentUser?.email!!.replace(".", ",")
                    usuarios.child(emailPath).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.exists()) {

                                val name = "" + snapshot.child("name").value
                                val surname = " " + snapshot.child("surname").value
                                val email = "" + snapshot.child("email").value

                                // En caso de que exista un usuario logueado seteamos el texto con su nombre y su email

                                with(binding) {
                                    tvName.text = "$name $surname"
                                    tvEmail.text = email
                                    tvWelcome.text = "Bienvenido(a)"
                                    myEventsButton.isClickable = true
                                    myNotesButton.isClickable = true
                                    myTasksButton.isClickable = true
                                    logoutButton.isClickable = true
                                }


                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                } else {
                    with(binding) {
                        tvName.text = ""
                        tvEmail.text = ""
                        tvWelcome.text = "Sin conexión."
                        myEventsButton.isClickable = false
                        myNotesButton.isClickable = false
                        myTasksButton.isClickable = false
                        logoutButton.isClickable = false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onResume() {
        loadData()
        super.onResume()
    }

    private fun logOutApp() {
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }

}