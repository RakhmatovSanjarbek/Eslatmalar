package com.example.eslatmalar

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eslatmalar.databinding.ActivityAllListPageBinding

class AllListPageActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var rv: RecyclerView
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var binding: ActivityAllListPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toDoAdapter = ToDoAdapter(
            onClick = { position ->
                Toast.makeText(this, "$position item bosildi", Toast.LENGTH_LONG).show()
            },
            deleteToDoButton = { position ->
                showDeleteDialog(position)
            },
            isMainToDo = { position ->
                replace(position)
            }
        )
        toDoAdapter.setClickLissener()
        sharedPreferencesHelper = SharedPreferencesHelper(this)



        rv = findViewById(R.id.all_list_page_rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.adapter = toDoAdapter

        findViewById<CardView>(R.id.next_add_to_do_page).setOnClickListener {
            startActivity(nextPage())
        }
    }

    private fun replace(position: Int) {
        val dataList = sharedPreferencesHelper.getToDoList()
        dataList?.get(position)?.isMainToDo = !dataList?.get(position)?.isMainToDo!!
        if (dataList[position].isMainToDo) {
            val imageView = (rv.layoutManager as LinearLayoutManager).findViewByPosition(position)
                ?.findViewById<ImageView>(R.id.is_main_to_do)
            imageView?.setImageResource(mainToDo())
            Toast.makeText(
                this,
                "$position Asosiy eslatmalar ro'yxatiga qo'shildi",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val imageView = (rv.layoutManager as LinearLayoutManager).findViewByPosition(position)
                ?.findViewById<ImageView>(R.id.is_main_to_do)
            imageView?.setImageResource(noMainToDo())
            Toast.makeText(
                this,
                "$position Asosiy eslatmalar ro'yxatidan olindi",
                Toast.LENGTH_SHORT
            ).show()
        }
        println(":::::: isMain  ${dataList[position].isMainToDo}")
        loadData()
    }


    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val dataList = sharedPreferencesHelper.getToDoList()
        if (dataList != null) {
            toDoAdapter.loadData(dataList)
        }
    }

    private fun nextPage(): Intent {
        return Intent(this, AddToDoPageActivity::class.java)
    }

    private fun showDeleteDialog(position: Int) {
        val toDoTitle = sharedPreferencesHelper.getToDoList()?.get(position)?.toDoTitle.toString()
        AlertDialog.Builder(this).setTitle("Eslatmani o'chirish")
            .setMessage("$toDoTitle nomli eslatma xaqiqatdan ham o'chirilsinmi?")
            .setPositiveButton("Xa") { _, _ ->
                sharedPreferencesHelper.removeToDoList(position)
                Toast.makeText(this, "$toDoTitle nomi eslatma o'chirildi", Toast.LENGTH_LONG).show()
                loadData()
            }.setNegativeButton("Yo'q", null).show()
    }

    private fun mainToDo(): Int = R.drawable.confirmation_icon
    private fun noMainToDo(): Int = R.drawable.check
}
