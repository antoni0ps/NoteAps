package com.proyecto.tasksnotes.add

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyecto.tasksnotes.databinding.ActivityAddEventBinding
import com.proyecto.tasksnotes.model.Event
import com.proyecto.tasksnotes.model.Task
import com.vivekkaushik.datepicker.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class Add_Event_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var db :DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var newCalendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        newCalendar = Calendar.getInstance()

        createActionbar()
        getAndSetData()
        getDateAndTime()
        createDatePickerAndTimePicker()



    }

    private fun addEventToDatabase(
        userUid: String, eventId: String, userName: String, email: String,
        title: String, description: String, eventDate: String, eventHour: String
    ) {
        val event = Event(userUid, eventId, userName, email, title, description, eventDate, eventHour)
        db.child("users").child(auth.currentUser!!.uid).child("events").child(eventId).setValue(event)
            .addOnSuccessListener {
                Toast.makeText(this,"Evento agregado con éxito", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onCalendarButtonPressed(newCalendar: Calendar) {
        binding.calendarButton.setOnClickListener {

            if (binding.tvCalendarDate.text.isEmpty() || binding.tvTime.text.isEmpty()) {
                Toast.makeText(applicationContext, "Debes elegir fecha y hora", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                intent.action = Intent.ACTION_EDIT
                intent.type = "vnd.android.cursor.item/event"

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, newCalendar.timeInMillis)
                intent.putExtra(CalendarContract.Events.TITLE, binding.etTitle.text.toString())
                intent.putExtra(CalendarContract.Events.DESCRIPTION, binding.etDescription.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun createDatePickerAndTimePicker() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Creamos el datepicker
        val datePickerTimeline = binding.datePickerTimeline

        //Establecemos la fecha de inicio del calendario
        datePickerTimeline.setInitialDate(year, month, day)

        //Creamos el timepicker
        val timePicker = binding.customTimePicker
        timePicker.setIs24HourView(true)

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

                binding.tvCalendarDate.text = "$formatedDay/$formatedMonth/$year"
            }

            override fun onDisabledDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int, isDisabled: Boolean) {
            }
        })

        //Establecemos las acciones que se realizarán al seleccionar una hora
        timePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->

            val formatedHour = if (hourOfDay < 10) {
                "0$hourOfDay"
            } else {
                hourOfDay.toString()
            }

            val formatedMin = if (minute < 10) {
                "0$minute"
            } else {
                minute.toString()
            }

            newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            newCalendar.set(Calendar.MINUTE, minute)

            binding.tvTime.text = "$formatedHour : $formatedMin"


        })

        //funcion que se llama al pulsar el boton de añadir al calendario
        onCalendarButtonPressed(newCalendar)
    }

    private fun getAndSetData() {

        var name = ""
        var surname = ""

        db.child("users").child(auth.currentUser!!.uid).child("name").get().addOnSuccessListener {
            name = it.value.toString()
            db.child("users").child(auth.currentUser!!.uid).child("surname").get().addOnSuccessListener {
                surname = it.value.toString()
                binding.tvUserName.text = "$name $surname"
            }
        }
        binding.tvEmail.text = auth.currentUser!!.email.toString()
    }

    private fun getDateAndTime() {
        val registerDateAndTime = SimpleDateFormat(
            "dd-MM-yyyy/HH:mm",
            Locale.getDefault()
        ).format(System.currentTimeMillis())

        binding.tvActualDate.text = registerDateAndTime
    }

    private fun createActionbar() {
        val actionBar = supportActionBar
        with(actionBar) {
            this!!.title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}