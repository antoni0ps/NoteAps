package com.proyecto.tasksnotes.add

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.R
import com.proyecto.tasksnotes.databinding.ActivityAddNoteBinding
import com.proyecto.tasksnotes.model.Note
import java.util.*

class Add_Note_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var emailPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        emailPath = auth.currentUser?.email!!.replace(".", ",")

        binding.noteSaveButton.setOnClickListener {
            getAndValidateData()
        }

    }


    private fun addNoteToDatabase(noteId: String, email: String, title: String, content: String, colorCode: Int) {


        val note = Note(noteId, email, title, content, colorCode)

        db.child("users").child(emailPath).child("user_notes").child(noteId).setValue(note)
            .addOnCompleteListener {
                Toast.makeText(this, "Nota agregada con éxito.", Toast.LENGTH_SHORT).show()

            }
    }

    private fun getAndValidateData() {

        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val noteId = db.push().key
        val email = auth.currentUser!!.email.toString()
        val colorCode = getRandomColor()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(applicationContext, "Debes rellenar Título y Descripción", Toast.LENGTH_SHORT).show()
        } else {
            addNoteToDatabase(noteId!!, email, title, content, colorCode)
            onBackPressed()
        }
    }

    private fun getRandomColor(): Int {
        val colorCode: MutableList<Int> = ArrayList()
        colorCode.add(R.color.blue)
        colorCode.add(R.color.yellow)
        colorCode.add(R.color.lightBlue)
        colorCode.add(R.color.lightOrange)
        colorCode.add(R.color.lightGreen)
        colorCode.add(R.color.gray)
        colorCode.add(R.color.pink)
        colorCode.add(R.color.red)
        colorCode.add(R.color.lightPurple)
        val randomColor = Random()
        val number = randomColor.nextInt(colorCode.size)
        return colorCode[number]
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}