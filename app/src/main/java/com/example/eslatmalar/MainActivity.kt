package com.example.eslatmalar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val toolbar by lazy { findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_page_toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        setSupportActionBar(toolbar)

        val user = sharedPreferencesHelper.getUser()

        findViewById<TextView>(R.id.user_first_name).text = user?.userFirstName ?: "Nomalum"
        findViewById<TextView>(R.id.user_last_name).text = user?.userLastNAme ?: "Nomalum"
        user?.let {
            findViewById<ImageView>(R.id.user_profile_image).setImageResource(it.userProfileImage)
        }

        findViewById<CardView>(R.id.next_all_to_do_page).setOnClickListener {
            startActivity(Intent(this,AllListPageActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_page_toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.log_out->{
                showLogOutDialog()
                true
            }
            else-> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() : Intent{
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
}