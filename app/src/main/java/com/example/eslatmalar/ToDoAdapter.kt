package com.example.eslatmalar


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.util.Calendar
import java.util.Collections

class ToDoAdapter(
    private val onClick: (position: Int) -> Unit,
    private val deleteToDoButton: (position: Int) -> Unit,
    private val isMainToDo: (position: Int) -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private val dataList = mutableListOf<ToDoModel>()
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var clickLessener: ClickLessener? = null

    fun setClickLissener(clickLessener: ClickLessener? = null) {
        this.clickLessener = clickLessener
    }

    fun loadData(toDoList: List<ToDoModel>) {
        dataList.clear()
        dataList.addAll(toDoList)
        notifyDataSetChanged()
    }
    fun swapOnMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(dataList, fromPosition, toPosition)
        sharedPreferencesHelper.saveToDoList(dataList)
    }

    fun deleteItemFromList(position: Int) {
        dataList.removeAt(position)
        sharedPreferencesHelper.saveToDoList(dataList)
    }

    private fun showDeleteDialog(position: Int) {
            AlertDialog.Builder(sharedPreferencesHelper.getContext())
                .setTitle("Eslatmani o'chirish")
                .setMessage("Eslatma xaqiqatdan ham o'chirilsinmi?")
                .setPositiveButton("Xa") { _, _ ->
                    sharedPreferencesHelper.removeToDoList(position)
                    dataList.removeAt(position)
                }
                .setNegativeButton("Yo'q", null)
                .show()
    }

    private fun showEditDialog(position: Int) {
        val toDoList = (sharedPreferencesHelper.getToDoList()?.toMutableList()?: emptyList()).toMutableList()
        if (position < toDoList.size) {
            val dialogView = LayoutInflater.from(sharedPreferencesHelper.getContext()).inflate(R.layout.dialog_edit_todo, null)
            val dialogBuilder = AlertDialog.Builder(sharedPreferencesHelper.getContext())
                .setView(dialogView)
                .setTitle("Eslatmani tahrirlash")

            val etTitle = dialogView.findViewById<EditText>(R.id.et_title)
            val etDescription = dialogView.findViewById<EditText>(R.id.et_description)
            val etThisDay = dialogView.findViewById<EditText>(R.id.et_this_day)

            val toDoModel = toDoList[position]
            etTitle.setText(toDoModel.toDoTitle)
            etDescription.setText(toDoModel.toDoDescription)
            etThisDay.setText(toDoModel.toDoThisDay)

            dialogBuilder.setPositiveButton("Saqlash") { dialog, _ ->
                val newTitle = etTitle.text.toString()
                val newDescription = etDescription.text.toString()
                val newThisDay = etThisDay.text.toString()

                val updatedToDo = ToDoModel(newTitle, newDescription, newThisDay, getTime(), toDoModel.isMainToDo)
                toDoList[position] = updatedToDo

                sharedPreferencesHelper.saveToDoList(toDoList)
                loadData(toDoList)

                dialog.dismiss()
            }

            dialogBuilder.setNegativeButton("Bekor qilish") { dialog, _ ->
                dialog.dismiss()
            }

            dialogBuilder.show()
        }
    }

    private fun getTime(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return "$day/$month/$year $hour:$minute"
    }


    fun notifyItemToDoDataChanged(position: Int) {
        dataList[position].isMainToDo = !dataList[position].isMainToDo
        sharedPreferencesHelper.saveToDoList(dataList)
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        sharedPreferencesHelper = SharedPreferencesHelper(parent.context) // Initialize sharedPreferencesHelper
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
        holder.isMainToDo.setOnClickListener {
            isMainToDo(position)
        }
        holder.editButton.setOnClickListener {
            showEditDialog(position)
        }

        if (toDoModel.isMainToDo) {
            holder.isMainToDo.setImageResource(R.drawable.confirmation_icon)
        } else {
            holder.isMainToDo.setImageResource(R.drawable.check)
        }
    }

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var toDoTitle: TextView = itemView.findViewById(R.id.to_do_title)
        var toDoDescription: TextView = itemView.findViewById(R.id.to_do_description)
        var toDoThisDay: TextView = itemView.findViewById(R.id.to_do_this_day)
        var toDoPostedTime: TextView = itemView.findViewById(R.id.to_do_posted_time)
        var materialCardViewRoot: MaterialCardView = itemView.findViewById(R.id.item_root)
        var deleteToDoButton: ImageView = itemView.findViewById(R.id.delete_to_do_list)
        val isMainToDo: ImageView = itemView.findViewById(R.id.is_main_to_do)
        var editButton:ImageView=itemView.findViewById(R.id.edit_to_do_list)
    }
}
