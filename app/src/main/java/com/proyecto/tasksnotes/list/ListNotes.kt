package com.proyecto.tasksnotes.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyecto.tasksnotes.R
import com.proyecto.tasksnotes.add.AddNote
import com.proyecto.tasksnotes.databinding.ActivityListNotesBinding
import com.proyecto.tasksnotes.detail.DetailNote
import com.proyecto.tasksnotes.model.Note
import com.proyecto.tasksnotes.viewholder.ViewHolderNote

class ListNotes : AppCompatActivity() {

    private lateinit var binding: ActivityListNotesBinding
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var adapter: FirebaseRecyclerAdapter<Note, ViewHolderNote>
    private lateinit var options: FirebaseRecyclerOptions<Note>
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var layoutMan: StaggeredGridLayoutManager
    private lateinit var emailPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        recyclerViewNotes = binding.recyclerViewNotes
        recyclerViewNotes.setHasFixedSize(true)
        db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("users")
        emailPath = auth.currentUser?.email!!.replace(".", ",")

        binding.addNoteButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }

        listNotes()

    }

    private fun listNotes() {

        val query = databaseReference.child(emailPath).child("user_notes")

        options = FirebaseRecyclerOptions.Builder<Note>().setQuery(query, Note::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Note, ViewHolderNote>(options) {

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onBindViewHolder(holder: ViewHolderNote, position: Int, note: Note) {

                holder.setData(applicationContext, note.title, note.content)
                val colorCode = note.colorCode
                val mCardView: CardView = holder.mView.findViewById(R.id.noteCard)
                mCardView.setCardBackgroundColor(holder.mView.resources.getColor(colorCode!!, null))

                holder.setOnClickListener(object : ViewHolderNote.ClickListener {
                    override fun onItemClick(view: View?, position: Int) {

                        //obtener los datos de la nota
                        val title = getItem(holder.bindingAdapterPosition).title
                        val content = getItem(holder.bindingAdapterPosition).content
                        val colorCode = getItem(holder.bindingAdapterPosition).colorCode
                        val noteId = getItem(holder.bindingAdapterPosition).noteId

                        //Enviar datos de la nota a la actividad de detalle
                        val intent = Intent(applicationContext, DetailNote::class.java)
                        intent.putExtra("title_detail", title)
                        intent.putExtra("content_detail", content)
                        intent.putExtra("colorCode", colorCode)
                        intent.putExtra("noteId", noteId)
                        startActivity(intent)
                    }
                })

                val menuIcon: ImageView = holder.mView.findViewById(R.id.item_note_menuIcon)
                menuIcon.setOnClickListener {

                    val noteId = getItem(position).noteId
                    val popupMenu = PopupMenu(applicationContext, menuIcon)
                    popupMenu.gravity = Gravity.END
                    popupMenu.menu.add("Eliminar").setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {

                        deleteNote(noteId!!, position)
                        false
                    })
                    popupMenu.show()

                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNote {

                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
                return ViewHolderNote(view)
            }
        }

        layoutMan = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewNotes.layoutManager = layoutMan
        recyclerViewNotes.adapter = adapter
    }

    private fun deleteNote(noteId: String, position: Int) {
        val builder = AlertDialog.Builder(this@ListNotes)
        builder.setTitle("Eliminar nota")
        builder.setMessage("¿Desea eliminar la nota?")
        builder.setPositiveButton("SI") { dialogInterface, i ->

            Toast.makeText(applicationContext, "Eliminando nota...", Toast.LENGTH_SHORT).show()

            val query = databaseReference.child(emailPath).child("user_notes").orderByChild("noteId").equalTo(noteId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (ds in snapshot.children) {
                        ds.ref.removeValue()
                            .addOnSuccessListener {
                                adapter.notifyItemRemoved(position)
                                Toast.makeText(applicationContext, "Nota eliminada", Toast.LENGTH_SHORT).show()

                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext, "No se pudo eliminar la nota", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        builder.setNegativeButton("NO") { dialogInterface, i ->
            Toast.makeText(applicationContext, "La nota no ha sido eliminada", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onStart() {
        adapter.startListening()
        super.onStart()

    }
}