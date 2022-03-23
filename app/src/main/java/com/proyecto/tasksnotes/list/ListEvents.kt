package com.proyecto.tasksnotes.list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyecto.tasksnotes.R
import com.proyecto.tasksnotes.add.AddEvent
import com.proyecto.tasksnotes.databinding.ActivityListEventsBinding
import com.proyecto.tasksnotes.detail.DetailEvent
import com.proyecto.tasksnotes.model.Event
import com.proyecto.tasksnotes.viewholder.ViewHolderEvent

class ListEvents : AppCompatActivity() {

    private lateinit var binding: ActivityListEventsBinding
    private lateinit var recyclerViewEvents: RecyclerView
    private lateinit var db: FirebaseDatabase
    private lateinit var dataBaseReference: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: FirebaseRecyclerAdapter<Event, ViewHolderEvent>
    private lateinit var options: FirebaseRecyclerOptions<Event>
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var emailPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        recyclerViewEvents = binding.recyclerViewEvents
        recyclerViewEvents.setHasFixedSize(true)
        db = FirebaseDatabase.getInstance()
        dataBaseReference = db.getReference("users")
        emailPath = auth.currentUser?.email!!.replace(".", ",")

        listEvents()

        binding.addEventButton.setOnClickListener {
            startActivity(Intent(this, AddEvent::class.java))
        }
    }

    private fun listEvents() {

        val query = dataBaseReference.child(emailPath).child("user_events").orderByChild("event_date")

        options = FirebaseRecyclerOptions.Builder<Event>().setQuery(query, Event::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Event, ViewHolderEvent>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEvent {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
                return ViewHolderEvent(view)
            }

            override fun onBindViewHolder(eventViewHolder: ViewHolderEvent, position: Int, event: Event) {

                eventViewHolder.setData(
                    applicationContext,
                    event.eventId,
                    event.userName,
                    event.email,
                    event.title,
                    event.description,
                    event.event_date,
                    event.event_time
                )

                eventViewHolder.setOnClickListener(object : ViewHolderEvent.ClickListener {

                    override fun onItemClick(view: View?, position: Int) {

                        //obtener datos del evento
                        val userName = getItem(position).userName
                        val email = getItem(position).email
                        val title = getItem(position).title
                        val description = getItem(position).description
                        val eventDate = getItem(position).event_date
                        val eventTime = getItem(position).event_time

                        //enviar datos del evento a la actividad detalle
                        val intent = Intent(applicationContext, DetailEvent::class.java)
                        intent.putExtra("userName_detail", userName)
                        intent.putExtra("email_detail", email)
                        intent.putExtra("title_detail", title)
                        intent.putExtra("description_detail", description)
                        intent.putExtra("date_detail", eventDate)
                        intent.putExtra("time_detail", eventTime)
                        startActivity(intent)
                    }
                })

                val menuIcon = eventViewHolder.mView.findViewById<ImageView>(R.id.event_menuButton)
                menuIcon.setOnClickListener {

                    val eventId = getItem(position).eventId
                    val popupMenu = PopupMenu(applicationContext, menuIcon)
                    popupMenu.menu.add("Eliminar Evento").setOnMenuItemClickListener {


                        deleteEvent(eventId!!, position)
                        false
                    }

                    popupMenu.show()
                }
            }
        }
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        recyclerViewEvents.layoutManager = linearLayoutManager
        recyclerViewEvents.adapter = adapter

    }

    private fun deleteEvent(eventId: String, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Evento")
        builder.setMessage("¿Desea eliminar el evento?")

        builder.setPositiveButton("SI") { dialogInterface, i ->

            Toast.makeText(applicationContext, "Eliminando evento...", Toast.LENGTH_SHORT).show()

            val query = dataBaseReference.child(emailPath).child("user_events").orderByChild("eventId").equalTo(eventId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        ds.ref.removeValue()
                            .addOnSuccessListener {
                                adapter.notifyItemRemoved(position)
                                Toast.makeText(applicationContext, "Evento eliminado correctamente", Toast.LENGTH_SHORT).show()
                            }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error al eliminar el evento", Toast.LENGTH_SHORT).show()
                }
            })
        }
        builder.setNegativeButton("NO") { dialogInterface, i ->

            Toast.makeText(applicationContext, "El evento no ha sido eliminado", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    override fun onStart() {
        adapter.startListening()
        super.onStart()

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}