package com.proyecto.tasksnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyecto.tasksnotes.add.Add_Note_Activity
import com.proyecto.tasksnotes.add.Add_Task_Activity
import com.proyecto.tasksnotes.list.List_Notes
import com.proyecto.tasksnotes.list.List_Tasks
import com.proyecto.tasksnotes.about.About_Activity
import com.proyecto.tasksnotes.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var usuarios: DatabaseReference
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarios = FirebaseDatabase.getInstance().getReference("users")
        auth = FirebaseAuth.getInstance()


        val actionBar = supportActionBar
        actionBar!!.title = ""


        /* Llamamos a la funcion loadData al iniciar para comprobar si existe un usuario logueado,
        si existe el usuario se le envia al menu principal, en caso contrario se envía a la
        actividad de login */

        loadData()

        binding.addTaskButton.setOnClickListener {

            val intent = Intent(this, Add_Task_Activity::class.java)
            name = binding.tvName.text.toString()
            email = binding.tvEmail.text.toString()
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            startActivity(intent)
        }
        binding.myTasksButton.setOnClickListener {
            startActivity(Intent(this, List_Tasks::class.java))
        }
        binding.addNoteButton.setOnClickListener {
            startActivity(Intent(this, Add_Note_Activity::class.java))
        }
        binding.myNotesButton.setOnClickListener {
            startActivity(Intent(this, List_Notes::class.java))
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

        usuarios.child(user.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    val name = "" + snapshot.child("name").value
                    val surname = " " + snapshot.child("surname").value
                    val email = "" + snapshot.child("email").value

                    // En caso de que exista un usuario logueado seteamos el texto con su nombre y su email
                    binding.tvName.setText("$name $surname")
                    binding.tvEmail.setText(email)

                    //Si existe usuario logueado habilitamos los botones del menu
                    binding.addTaskButton.isEnabled = true
                    binding.myNotesButton.isEnabled = true
                    binding.myTasksButton.isEnabled = true
                    binding.addNoteButton.isEnabled = true
                    binding.aboutButton.isEnabled = true
                    binding.logoutButton.isEnabled = true
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun logOutApp() {
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
    }
}