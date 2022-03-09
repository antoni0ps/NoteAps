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

        getAndSetData()
    }

    private fun getAndSetData(){

        val username = intent.getStringExtra("userName_detail")
        val email = intent.getStringExtra("email_detail")
        val title = intent.getStringExtra("title_detail")
        val description = intent.getStringExtra("description_detail")
        val date = intent.getStringExtra("date_detail")
        val time = intent.getStringExtra("time_detail")

        binding.userNameDetail.text = username
        binding.emailDetail.text = email
        binding.titleDetail.text = title
        binding.descriptionDetail.text = description
        binding.dateAndTimeDetail.text = "$date     $time H"

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}