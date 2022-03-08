package com.proyecto.tasksnotes.viewholder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.tasksnotes.R

class ViewHolder_Event(var mView: View) : RecyclerView.ViewHolder(mView) {

    private lateinit var mClickListener: ClickListener
    private lateinit var item_eventId: TextView
    private lateinit var item_userName: TextView
    private lateinit var item_email: TextView
    private lateinit var item_title: TextView
    private lateinit var item_description: TextView
    private lateinit var item_date: TextView
    private lateinit var item_time: TextView


    interface ClickListener {
        fun onItemClick(view: View?, position: Int) //Se ejecuta al pulsar en el item
    }

    fun setOnClickListener(clickListener: ClickListener) {
        mClickListener = clickListener!!
    }

    fun setData(
        context: Context?, eventId: String?, userName: String?, email: String?,
        title: String?, description: String?, eventDate: String?, eventTime: String?
    ) {

        //Establecemos conexión con el item


        item_eventId = mView.findViewById(R.id.item_event_Id)
        item_userName = mView.findViewById(R.id.event_userName)
        item_email = mView.findViewById(R.id.event_email)
        item_title = mView.findViewById(R.id.event_title)
        item_description = mView.findViewById(R.id.event_description)
        item_date = mView.findViewById(R.id.event_Date)
        item_time = mView.findViewById(R.id.event_time)

        //Establecemos contenido del item
        item_eventId.text = eventId
        item_userName.text = userName
        item_email.text = email
        item_title.text = title
        item_description.text = description
        item_date.text = eventDate
        item_time.text = eventTime
    }

    init {
        itemView.setOnClickListener { view -> mClickListener.onItemClick(view, bindingAdapterPosition) }
    }
}