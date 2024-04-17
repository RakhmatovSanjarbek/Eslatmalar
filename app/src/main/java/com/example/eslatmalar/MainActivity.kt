package com.example.eslatmalar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eslatmalar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val toolbar by lazy { findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_page_toolbar) }
    private lateinit var rv: RecyclerView
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        setSupportActionBar(toolbar)

        val user = sharedPreferencesHelper.getUser()

        findViewById<TextView>(R.id.user_first_name).text = user?.userFirstName ?: "Nomalum"
        findViewById<TextView>(R.id.user_last_name).text = user?.userLastNAme ?: "Nomalum"
        user?.let {
            findViewById<ImageView>(R.id.user_profile_image).setImageResource(it.userProfileImage)
        }

        findViewById<CardView>(R.id.next_all_to_do_page).setOnClickListener {
            startActivity(Intent(this, AllListPageActivity::class.java))
        }

        toDoAdapter = ToDoAdapter(
            onClick = {},
            deleteToDoButton = {position ->
                showDeleteDialog(position)
            },
            isMainToDo = { position ->
                replace(position)
            }
        )
        toDoAdapter.setClickLissener()
        rv = findViewById(R.id.main_list_page_rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.adapter = toDoAdapter

        ItemTouchHelper(RvHelper(toDoAdapter)).attachToRecyclerView(rv)
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

    private fun replace(position: Int) {
        val dataList = sharedPreferencesHelper.getToDoList()?.toMutableList() ?: mutableListOf()
        dataList[position].isMainToDo = !dataList[position].isMainToDo
        if (dataList[position].isMainToDo) {
            val imageView = (rv.layoutManager as LinearLayoutManager).findViewByPosition(position)
                ?.findViewById<ImageView>(R.id.is_main_to_do)
            imageView?.setImageResource(mainToDo())
            Toast.makeText(
                this,
                "Asosiy eslatmalar ro'yxatiga qo'shildi",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val imageView = (rv.layoutManager as LinearLayoutManager).findViewByPosition(position)
                ?.findViewById<ImageView>(R.id.is_main_to_do)
            imageView?.setImageResource(noMainToDo())
            Toast.makeText(
                this,
                "Asosiy eslatmalar ro'yxatidan olindi",
                Toast.LENGTH_SHORT
            ).show()
        }
        toDoAdapter.notifyItemToDoDataChanged(position)
    }

    private fun mainToDo(): Int = R.drawable.confirmation_icon
    private fun noMainToDo(): Int = R.drawable.check

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_page_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort->{
                sortListAlphabetically()
                true
            }
            R.id.log_out -> {
                showLogOutDialog()
                true
            }

            R.id.profile_settings -> {
                nextEditProfilePage()
                true
            }
            R.id.navigation_phone->{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:+998944856603")
                startActivity(intent)
                true
            }
            R.id.navigation_github->{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rakhmatovsanjar/To-Do-App"))
                startActivity(intent)
                true
            }
            R.id.search->{
                showSearchDialog()
                true
            }
            R.id.info_page->{
                startActivity(
                    Intent(this,InfoPageActivity::class.java)
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun nextEditProfilePage() {
        startActivity(
            Intent(this, SignUpPageActivity::class.java)
        )
        finish()
    }

    private fun logOut(): Intent {
        sharedPreferencesHelper.clearUser()
        val intent = Intent(this, SignUpPageActivity::class.java)
        finish()
        return intent
    }

    private fun showLogOutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Diqqat!")
            .setMessage("Agar hisobingizdan chiqsangiz barcha eslatmalariz o'chib ketadi")
            .setPositiveButton("Chiqish") { _, _ ->
                startActivity(logOut())
            }
            .setNegativeButton("Bekor qilish", null)
            .show()
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
    private fun showSearchDialog() {
        val searchView = layoutInflater.inflate(R.layout.dialog_search, null)
        AlertDialog.Builder(this)
            .setTitle("Qidirish")
            .setView(searchView)
            .setPositiveButton("Qidirish") { dialog, _ ->
                val searchText = searchView.findViewById<TextView>(R.id.search_text).text.toString()
                performSearch(searchText)
                dialog.dismiss()
            }
            .setNegativeButton("Bekor qilish") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun performSearch(query: String) {
        val dataList = sharedPreferencesHelper.getToDoList()
        if (dataList != null) {
            val filteredList = dataList.filter { it.toDoTitle.contains(query, ignoreCase = true) }
            toDoAdapter.loadData(filteredList)
        }
    }
    private fun sortListAlphabetically() {
        val dataList = sharedPreferencesHelper.getToDoList()?.toMutableList() ?: mutableListOf()

        dataList.sortBy { it.toDoTitle }
        sharedPreferencesHelper.saveToDoList(dataList)
        toDoAdapter.loadData(dataList)
        Toast.makeText(this, "Eslatmalar alfabet bo'yicha tartiblandi", Toast.LENGTH_SHORT).show()
    }

}
