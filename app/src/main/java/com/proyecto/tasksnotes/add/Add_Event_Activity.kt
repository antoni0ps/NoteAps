package com.proyecto.tasksnotes.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.databinding.ActivityAddEventBinding

class Add_Event_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var db :DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


    }
}