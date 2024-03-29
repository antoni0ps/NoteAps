package com.proyecto.tasksnotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyecto.tasksnotes.databinding.ActivityMenuBinding
import com.proyecto.tasksnotes.list.ListEvents
import com.proyecto.tasksnotes.list.ListNotes
import com.proyecto.tasksnotes.list.ListTasks

class Menu : AppCompatActivity() {

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
            startActivity(Intent(this, ListTasks::class.java))
        }

        binding.myNotesButton.setOnClickListener {
            startActivity(Intent(this, ListNotes::class.java))
        }

        binding.myEventsButton.setOnClickListener {
            startActivity(Intent(this, ListEvents::class.java))
        }

        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutUs::class.java))
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
        startActivity(Intent(this, Login::class.java))
    }

}