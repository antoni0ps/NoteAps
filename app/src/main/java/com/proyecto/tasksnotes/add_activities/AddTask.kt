package com.proyecto.tasksnotes.add_activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.databinding.ActivityAddTaskBinding
import com.proyecto.tasksnotes.model.Task
import com.vivekkaushik.datepicker.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*


class AddTask : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var newCalendar: Calendar
    private lateinit var emailPath : String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        newCalendar = Calendar.getInstance()
        emailPath = auth.currentUser?.email!!.replace(".",",")

        getAndSetData()
        getDateAndTime()
        createDatePicker()


        binding.saveTaskButton.setOnClickListener {
            getTaskData()
        }
    }

    private fun createDatePicker() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Creamos el datepicker
        val datePickerTimeline = binding.datePickerTimeline

        //Establecemos la fecha de inicio del calendario
        datePickerTimeline.setInitialDate(year, month, day)

        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {

                val formatedDay: String
                val formatedMonth: String
                val mMonth = month + 1

                formatedDay = if (day < 10) {
                    "0$day"
                } else {
                    day.toString()
                }
                formatedMonth = if (month < 10) {
                    "0$mMonth"
                } else {
                    month.toString()
                }

                newCalendar.set(Calendar.YEAR, year)
                newCalendar.set(Calendar.MONTH, month)
                newCalendar.set(Calendar.DAY_OF_MONTH, day)

                binding.tvFinishDate.text = "$formatedDay/$formatedMonth/$year"
            }
            override fun onDisabledDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int, isDisabled: Boolean) {
            }
        })
    }

    private fun getTaskData() {

        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()
        val userName = binding.tvUserName.text.toString()
        val actualDate = binding.tvActualDate.text.toString()
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val taskDate = binding.tvFinishDate.text.toString()
        val status = binding.tvStatus.text.toString()
        val taskId = db.push().key

        if (userName.isEmpty() || email.isEmpty() || actualDate.isEmpty() || title.isEmpty() || description.isEmpty() || taskDate.isEmpty()) {
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Agregando tarea a la Base de Datos...", Toast.LENGTH_SHORT).show()
            addTaskToDatabase(taskId!!, userName, email, actualDate, title, description, taskDate, status)
            onBackPressed()
        }
    }

    private fun addTaskToDatabase(
        taskId: String, userName: String, email: String, actualDate: String,
        title: String, description: String, taskDate: String, status: String
    ) {
        val task = Task(taskId, userName, email, actualDate, title, description, taskDate, status)
        db.child("users").child(emailPath).child("user_tasks").child(taskId).setValue(task)
            .addOnSuccessListener {
                Toast.makeText(this,"Tarea agregada con éxito",Toast.LENGTH_SHORT).show()
            }
    }


    private fun getAndSetData() {

        var name = ""
        var surname = ""

        db.child("users").child(emailPath).child("name").get().addOnSuccessListener {
            name = it.value.toString()
            db.child("users").child(emailPath).child("surname").get().addOnSuccessListener {
                surname = it.value.toString()
                binding.tvUserName.text = "$name $surname"
            }
        }
        binding.tvEmail.text = auth.currentUser!!.email.toString()
    }

    private fun getDateAndTime() {
        val registerDateAndTime = SimpleDateFormat("dd-MM-yyyy/HH:mm", Locale.getDefault()).format(System.currentTimeMillis())
        binding.tvActualDate.text = registerDateAndTime }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}