package com.proyecto.tasksnotes.list

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyecto.tasksnotes.detail.Detail_Task_Activity
import com.proyecto.tasksnotes.model.Task
import com.proyecto.tasksnotes.R
import com.proyecto.tasksnotes.add.Add_Task_Activity
import com.proyecto.tasksnotes.viewholder.ViewHolder_Task
import com.proyecto.tasksnotes.databinding.ActivityListTasksBinding
import com.proyecto.tasksnotes.databinding.OptionsDialogBinding


class List_Tasks : AppCompatActivity() {

    private lateinit var binding: ActivityListTasksBinding
    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var db: FirebaseDatabase
    private lateinit var dataBaseReference: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: FirebaseRecyclerAdapter<Task, ViewHolder_Task>
    private lateinit var options: FirebaseRecyclerOptions<Task>
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var emailPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        recyclerViewTasks = binding.recyclerViewTasks
        recyclerViewTasks.setHasFixedSize(true) //el recyclerview se adaptará al tamaño de la lista
        db = FirebaseDatabase.getInstance()
        dataBaseReference = db.getReference("users")
        emailPath = auth.currentUser?.email!!.replace(".",",")

        listTasks()

        binding.addTaskButton.setOnClickListener {
            startActivity(Intent(this, Add_Task_Activity::class.java))
        }
    }

    private fun listTasks() {

        val query = dataBaseReference.child(emailPath).child("user_tasks").orderByChild("status")

        options = FirebaseRecyclerOptions.Builder<Task>().setQuery(query, Task::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Task, ViewHolder_Task>(options) {

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onBindViewHolder(viewHolder_task: ViewHolder_Task, position: Int, task: Task) {

                viewHolder_task.setData(
                    applicationContext,
                    task.taskId,
                    task.userName,
                    task.email,
                    task.registerDate,
                    task.title,
                    task.description,
                    task.taskDate,
                    task.status
                )

                viewHolder_task.setOnClickListener(object : ViewHolder_Task.ClickListener {

                    override fun onItemClick(view: View?, position: Int) {

                        //Obtener datos de la tarea
                        val userName = getItem(position).userName
                        val email = getItem(position).email
                        val registerDate = getItem(position).registerDate
                        val title = getItem(position).title
                        val description = getItem(position).description
                        val taskDate = getItem(position).taskDate
                        val status = getItem(position).status

                        //Enviar datos de la tarea a la actividad de detalle
                        val intent = Intent(applicationContext, Detail_Task_Activity::class.java)
                        intent.putExtra("userName_detail", userName)
                        intent.putExtra("email_detail", email)
                        intent.putExtra("registryDate_detail", registerDate)
                        intent.putExtra("title_detail", title)
                        intent.putExtra("description_detail", description)
                        intent.putExtra("finishDate_detail", taskDate)
                        intent.putExtra("status_detail", status)
                        startActivity(intent)
                    }
                })

                val menuIcon = viewHolder_task.mView.findViewById<ImageView>(R.id.item_menuButton)
                menuIcon.setOnClickListener {

                    val taskStatus = getItem(position).status
                    val taskId = getItem(position).taskId
                    val popupMenu = PopupMenu(applicationContext, menuIcon)
                    popupMenu.gravity = Gravity.END
                    popupMenu.menu.add("Marcar Finalizada").setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {

                        updateStatus(taskId!!, taskStatus!!)

                        false
                    })

                    popupMenu.menu.add("Eliminar tarea").setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {

                        deleteTask(taskId!!)
                        false
                    })
                    popupMenu.show()
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_Task {

                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
                val taskViewholder = ViewHolder_Task(view)
                return taskViewholder
            }

        }
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        recyclerViewTasks.layoutManager = linearLayoutManager
        recyclerViewTasks.adapter = adapter
    }

    private fun deleteTask(taskId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar tarea")
        builder.setMessage("¿Desea eliminar la tarea?")

        //Metodo que se ejecutará si el usuario confirma que quiere eliminar la nota
        builder.setPositiveButton("SI") { dialogInterface, i ->

            //Eliminamos nota de la Base de datos
            val query = dataBaseReference.child(emailPath).child("user_tasks").orderByChild("taskId").equalTo(taskId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        ds.ref.removeValue()
                            .addOnCompleteListener {
                                Toast.makeText(applicationContext, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        builder.setNegativeButton("NO") { dialogInterface, i ->
            Toast.makeText(applicationContext, "La Tarea no ha sido eliminada", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    private fun updateStatus(taskId: String, taskStatus: String) {

        val builder = AlertDialog.Builder(this)

        if (taskStatus == "No finalizada") {
            builder.setTitle("Finalizar")
            builder.setMessage("¿Desea finalizar la tarea?")
        } else {
            builder.setTitle("Tarea ya finalizada")
            builder.setMessage("¿Desea volver a activarla?")
        }


        //Metodo que se ejecutará si el usuario confirma que quiere actualizar el estado de la nota
        builder.setPositiveButton("SI") { dialogInterface, i ->

            val query = dataBaseReference.child(emailPath).child("user_tasks").orderByChild("taskId").equalTo(taskId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (ds in snapshot.children) {

                        val status = ds.child("status").getValue(String::class.java)

                        if (status.equals("No finalizada")) {
                            ds.ref.child("status").setValue("Finalizada")
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Tarea finalizada", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            ds.ref.child("status").setValue("No finalizada")
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Tarea no finalizada", Toast.LENGTH_SHORT).show()
                                }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        builder.setNegativeButton("NO") { dialogInterface, i ->
            Toast.makeText(applicationContext, "No se han realizado cambios", Toast.LENGTH_SHORT).show()
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