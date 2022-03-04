package com.proyecto.tasksnotes.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.model.Task
import com.proyecto.tasksnotes.R
import com.proyecto.tasksnotes.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.*


class Add_Task_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        createActionBar()
        getAndSetData()
        getDateAndTime()


        //Creamos un datepickerdialog al pulsar el botón calendario y establecemos la fecha en el textview correspondiente
        binding.calendarButton.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { view, mYear, mMonth, mDay ->

                val formatedDay: String
                val formatedMonth: String
                val month = mMonth + 1

                formatedDay = if (mDay < 10) {
                    "0$mDay"
                } else {
                    mDay.toString()
                }
                formatedMonth = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }

                binding.tvCalendarDate.text = "$formatedDay/$formatedMonth/$mYear"
            }, year, month, day)

            datePickerDialog.show()
        }


    }

    private fun getTaskData() {

        val currentUser = auth.currentUser
        val email = currentUser?.email.toString()
        val userName = binding.tvUserName.text.toString()
        val actualDate = binding.tvActualDate.text.toString()
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val taskDate = binding.tvCalendarDate.text.toString()
        val status = binding.tvStatus.text.toString()
        val taskId = db.push().key
        val userUid = currentUser!!.uid



        if (userName.isEmpty() || email.isEmpty() || actualDate.isEmpty() || title.isEmpty() || description.isEmpty() || taskDate.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            addTaskToDatabase(userUid, taskId!!, userName, email, actualDate, title, description, taskDate, status)
            Toast.makeText(this, "Agregando tarea a la Base de Datos...", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }


    }

    private fun addTaskToDatabase(
        userUid: String, taskId: String, userName: String, email: String, actualDate: String,
        title: String, description: String, taskDate: String, status: String
    ) {


        val task = Task(userUid, taskId, userName, email, actualDate, title, description, taskDate, status)
        db.child("users").child(auth.currentUser!!.uid).child("tasks").child(taskId).setValue(task)
            .addOnSuccessListener {

            }
    }

    private fun createActionBar() {
        val actionBar = supportActionBar
        with(actionBar) {
            this!!.title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun getAndSetData() {

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        binding.tvUserName.setText(name)
        binding.tvEmail.setText(email)
    }

    private fun getDateAndTime() {
        val registerDateAndTime = SimpleDateFormat(
            "dd-MM-yyyy/HH:mm",
            Locale.getDefault()
        ).format(System.currentTimeMillis())

        binding.tvActualDate.setText(registerDateAndTime)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.addToDatabase -> getTaskData()

        }
        return super.onOptionsItemSelected(item)
    }

}