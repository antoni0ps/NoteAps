package com.proyecto.tasksnotes.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyecto.tasksnotes.databinding.ActivityTaskDetailBinding

class Detail_Task_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        createActionBar()
        getAndSetData()
    }

    private fun getAndSetData() {

        val username = intent.getStringExtra("userName_detail")
        val email = intent.getStringExtra("email_detail")
        val title = intent.getStringExtra("title_detail")
        val description = intent.getStringExtra("description_detail")
        val registryDate = intent.getStringExtra("registryDate_detail")
        val finishDate = intent.getStringExtra("finishDate_detail")
        val status = intent.getStringExtra("status_detail")

        binding.userNameDetail.text = username
        binding.emailDetail.text = email
        binding.titleDetail.text = title
        binding.descriptionDetail.text = description
        binding.registryDateDetail.text = registryDate
        binding.finishDateDetail.text = finishDate
        binding.statusDetail.text = status
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