package com.proyecto.tasksnotes.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyecto.tasksnotes.databinding.ActivityDetailEventBinding

class Detail_Event_Activity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createActionBar()
    }


    private fun createActionBar() {
        val actionBar = supportActionBar
        with(actionBar) {
            this!!.title = "Detalles de la tarea"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}