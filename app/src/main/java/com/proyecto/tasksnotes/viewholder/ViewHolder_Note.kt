package com.proyecto.tasksnotes.viewholder


import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.tasksnotes.R

class ViewHolder_Note(var mView: View): RecyclerView.ViewHolder(mView){

    private lateinit var mClickListener: ClickListener


    interface ClickListener {
        fun onItemClick(view: View?, position: Int) //Se ejecuta al pulsar en el item
    }

    fun setOnClickListener(clickListener: ClickListener?) {
        mClickListener = clickListener!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setData(context: Context, title: String?, content : String?){

        //Declaramos vistas
        val item_title: TextView
        val item_content: TextView

        //Establecemos conexión con el item
        item_title = mView.findViewById(R.id.item_note_title)
        item_content = mView.findViewById(R.id.item_note_content)


        //Establecemos contenido del item
        item_title.text = title
        item_content.text = content


    }



    init {
        itemView.setOnClickListener { view -> mClickListener.onItemClick(view, bindingAdapterPosition) }
    }
}