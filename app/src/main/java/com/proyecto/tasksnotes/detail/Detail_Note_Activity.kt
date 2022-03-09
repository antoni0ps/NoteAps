package com.proyecto.tasksnotes.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.R
import com.proyecto.tasksnotes.databinding.ActivityDetailNoteBinding


class Detail_Note_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNoteBinding
    private lateinit var db: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference

        getAndSetData()
        binding.noteSaveButton.setOnClickListener {
            updateDatainDatabase()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getAndSetData() {

        val title = intent.getStringExtra("title_detail")
        val content = intent.getStringExtra("content_detail")

        binding.tvTitle.setText(title)
        binding.tvContent.setText(content)
        binding.tvContent.setBackgroundColor(resources.getColor(intent.getIntExtra("colorCode", 0), null))
    }

    private fun updateDatainDatabase() {

        val tableName = "notes"
        val noteId = intent.getStringExtra("noteId")
        db.child(tableName).child(noteId!!).child("title").setValue(binding.tvTitle.text.toString())
        db.child(tableName).child(noteId).child("content").setValue(binding.tvContent.text.toString())
            .addOnCompleteListener {
                Toast.makeText(this, "Nota Actualizada con éxito.", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}