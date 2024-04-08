package com.example.eslatmalar

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class AddToDoPageActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var toDoTitle: EditText
    private lateinit var toDoDescreption: EditText
    private lateinit var toDoThisDay: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_to_do_page)
        findViewById<Toolbar>(R.id.add_to_do_page_toolbar).setNavigationOnClickListener {
            finish()
        }
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        toDoAdapter = ToDoAdapter(
            onClick = {},
            deleteToDoButton = {},
            isMainToDo = {}
        )
        toDoTitle = findViewById(R.id.edit_to_do_title)
        toDoDescreption = findViewById(R.id.edit_to_do_descreption)
        toDoThisDay = findViewById(R.id.edit_to_do_this_day)
        findViewById<MaterialCardView>(R.id.card_this_day).setOnClickListener {
            showDatePicker()
        }
        val saveToDoListButton = findViewById<MaterialButton>(R.id.save_to_do_list_button)

        saveToDoListButton.setOnClickListener {
            saveToDoList()
        }
        loadData()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                toDoThisDay.text = selectedDate
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun saveToDoList() {
        val toDoTitle = toDoTitle.text.toString()
        val toDoDescription = toDoDescreption.text.toString()
        val toDoThisDay = toDoThisDay.text.toString()

        if (toDoTitle.isEmpty() || toDoDescription.isEmpty() || toDoThisDay.isEmpty()) {
            Toast.makeText(this, "Maydonlarni toldiring", Toast.LENGTH_LONG).show()
            return
        }

        val toDoModel = ToDoModel(toDoTitle, toDoDescription, toDoThisDay, getTime())

        sharedPreferencesHelper.saveToDoList(toDoModel)
        Toast.makeText(this, "Muvaffaqqiyatli qo'shildi", Toast.LENGTH_LONG).show()
        finish()
        loadData()
    }

    private fun loadData() {
        val dataList = sharedPreferencesHelper.getToDoList()
        if (dataList != null) {
            toDoAdapter.loadData(dataList)
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
}