package com.proyecto.tasksnotes.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.proyecto.tasksnotes.R

class ViewHolder_Task(var mView: View) : RecyclerView.ViewHolder(mView) {

    private lateinit var mClickListener: ClickListener
    private lateinit var  item_userName: TextView
    private lateinit var item_email: TextView
    private lateinit var item_registerDate: TextView
    private lateinit var item_title: TextView
    private lateinit var item_description: TextView
    private lateinit var item_finishDate: TextView
    private lateinit var item_status: TextView
    private lateinit var finishedTask_item : ImageView
    private lateinit var unfinishedTask_item : ImageView



    interface ClickListener : ViewHolder_Event.ClickListener {
        override fun onItemClick(view: View?, position: Int) //Se ejecuta al pulsar en el item
//        fun onItemLongClick(view: View?, position: Int) //Se ejecuta al mantener presionado el item
    }



    fun setOnClickListener(clickListener: ClickListener?) {
        mClickListener = clickListener!!
    }

    fun setData(context: Context?, taskId: String?, userName: String?, email: String?, registerDate: String?,
                title: String?, description: String?, noteDate: String?, status: String?) {

        //Declaramos las vistas
        //val item_taskId: TextView
        val item_userName: TextView
        val item_email: TextView
        val item_registerDate: TextView
        val item_title: TextView
        val item_description: TextView
        val item_finishDate: TextView
        val item_status: TextView
        val finishedTask_item : ImageView
        val unfinishedTask_item : ImageView

        //Establecemos conexión con el item


            val item_taskId : TextView = mView.findViewById(R.id.item_task_Id)
            item_userName = mView.findViewById(R.id.item_userName)
            item_email = mView.findViewById(R.id.item_email)
            item_registerDate = mView.findViewById(R.id.item_registerDate)
            item_title = mView.findViewById(R.id.item_title)
            item_description = mView.findViewById(R.id.item_description)
            item_finishDate = mView.findViewById(R.id.item_finishDate)
            item_status = mView.findViewById(R.id.item_status)
            finishedTask_item = mView.findViewById(R.id.finishedTask_item)
            unfinishedTask_item = mView.findViewById(R.id.unFinishedTask_item)




        //Establecer contenido del item
        item_taskId.text = taskId
        item_userName.text = userName
        item_email.text = email
        item_registerDate.text = registerDate
        item_title.text = title
        item_description.text = description
        item_finishDate.text = noteDate
        item_status.text = status

        if(status.equals("Finalizada")){
            finishedTask_item.visibility = View.VISIBLE
            unfinishedTask_item.visibility = View.GONE
        }else{
            finishedTask_item.visibility = View.GONE
            unfinishedTask_item.visibility = View.VISIBLE
        }
    }

    init {
        itemView.setOnClickListener { view -> mClickListener.onItemClick(view, bindingAdapterPosition) }
        }
    }
