package com.example.eslatmalar

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreferencesHelper(context: Context) {
    private val mySharedPreferences: SharedPreferences =
        context.getSharedPreferences("to_do_list", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveToDoList(toDoModel: ToDoModel) {
        val existingList = getToDoList()?.toMutableList() ?: mutableListOf()
        existingList.add(toDoModel)
        val json = gson.toJson(existingList)
        mySharedPreferences.edit().putString("list", json).apply()
    }

    fun getToDoList(): List<ToDoModel>? {
        val json = mySharedPreferences.getString("list", null)
        return gson.fromJson(json, Array<ToDoModel>::class.java)?.toList()
    }

    fun removeToDoList(position: Int) {
        val existingList = getToDoList()?.toMutableList() ?: mutableListOf()
        if (position in existingList.indices) {
            existingList.removeAt(position)
            val json = gson.toJson(existingList)
            mySharedPreferences.edit().putString("list", json).apply()
        }
    }

    fun saveUser(user: UserModel) {
        val editor = mySharedPreferences.edit()
        val json = gson.toJson(user)
        editor.putString("user", json)
        editor.apply()
    }

    fun getUser(): UserModel? {
        val json = mySharedPreferences.getString("user", null)
        return gson.fromJson(json, UserModel::class.java)
    }

    fun clearUser() {
        val editor = mySharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    fun editUser(toDoModel: ToDoModel) {
        val existingToDoModel = getToDoList()
        if (existingToDoModel != null) {
            val editor = mySharedPreferences.edit()
            val json = gson.toJson(toDoModel)
            editor.putString("user", json)
            editor.apply()
        }
    }

}