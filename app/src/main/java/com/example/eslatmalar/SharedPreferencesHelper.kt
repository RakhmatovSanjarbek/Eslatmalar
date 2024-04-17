package com.example.eslatmalar

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper(private val context: Context) {
    private val gson by lazy { Gson() }
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("to_do_list", Context.MODE_PRIVATE)
    }

    fun saveToDoList(todoList: List<ToDoModel>): Boolean {
        return sharedPreferences.edit()?.putString("list", gson.toJson(todoList))
            ?.commit() ?: false
    }

    fun getToDoList(): List<ToDoModel>? {
        return if (!sharedPreferences.getString("list", "").isNullOrEmpty()) {
            gson.fromJson(
                sharedPreferences.getString("list", ""),
                object : TypeToken<List<ToDoModel>>() {}.type
            )
        } else {
            emptyList()
        }
    }

    fun removeToDoList(position: Int) {
        val existingList = getToDoList()?.toMutableList() ?: mutableListOf()
        if (position in existingList.indices) {
            existingList.removeAt(position)
            val json = gson.toJson(existingList)
            sharedPreferences.edit().putString("list", json).apply()
        }
    }

    fun saveUser(user: UserModel) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(user)
        editor.putString("user", json)
        editor.apply()
    }

    fun getUser(): UserModel? {
        val json = sharedPreferences.getString("user", null)
        return gson.fromJson(json, UserModel::class.java)
    }

    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    fun getContext(): Context {
        return context
    }

    fun editUser(userModel: UserModel) {
        val existingUserModel = getUser()
        if (existingUserModel != null) {
            val editor = sharedPreferences.edit()
            val json = gson.toJson(userModel)
            editor.putString("user", json)
            editor.apply()
        }
    }
}
