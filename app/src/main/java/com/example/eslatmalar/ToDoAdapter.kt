package com.example.eslatmalar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ToDoAdapter(
    private val onClick: (position: Int) -> Unit,
    private val deleteToDoButton: (position: Int) -> Unit,
    private val isMainToDo: (position: Int) -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private val dataList = mutableListOf<ToDoModel>()

    private var clickLessener: ClickLessener? = null

    fun setClickLissener(clickLessener: ClickLessener? = null) {
        this.clickLessener = clickLessener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadData(toDoList: List<ToDoModel>) {
        dataList.clear()
        dataList.addAll(toDoList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        return ToDoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val toDoModel = dataList[position]
        holder.toDoTitle.text = toDoModel.toDoTitle
        holder.toDoDescription.text = toDoModel.toDoDescription
        holder.toDoThisDay.text = toDoModel.toDoThisDay
        holder.toDoPostedTime.text = toDoModel.toDoPostedTime

        holder.materialCardViewRoot.setOnClickListener {
            onClick(position)
        }
        holder.deleteToDoButton.setOnClickListener {
            deleteToDoButton(position)
        }
        holder.isMainToDo.setOnClickListener{
            isMainToDo(position)
        }
    }

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var toDoTitle: TextView = itemView.findViewById(R.id.to_do_title)
        var toDoDescription: TextView = itemView.findViewById(R.id.to_do_description)
        var toDoThisDay: TextView = itemView.findViewById(R.id.to_do_this_day)
        var toDoPostedTime: TextView = itemView.findViewById(R.id.to_do_posted_time)
        var materialCardViewRoot: MaterialCardView = itemView.findViewById(R.id.item_root)
        var deleteToDoButton: ImageView = itemView.findViewById(R.id.delete_to_do_list)
        val isMainToDo:ImageView=itemView.findViewById(R.id.is_main_to_do)
    }
}
