package com.example.eslatmalar


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SignUpPageActivity : AppCompatActivity() {

    private var imageList = arrayOf(
        R.drawable.profile_image_1,
        R.drawable.profile_image_2,
        R.drawable.profile_image_3,
        R.drawable.profile_image_4,
        R.drawable.profile_image_5
    )

    private lateinit var sharedPreferencesHelper:SharedPreferencesHelper

    private lateinit var profileImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var editUserFirstName: EditText
    private lateinit var editUserLasttName: EditText
    private lateinit var signUpButton: MaterialButton

    private var imageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)

        profileImage = findViewById(R.id.sign_up_page_profile_emage)
        selectImageButton = findViewById(R.id.sign_up_page_select_profile_image_button)
        editUserFirstName=findViewById(R.id.edit_user_first_name)
        editUserLasttName=findViewById(R.id.edit_user_last_name)
        signUpButton=findViewById(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            if (isEmpty()){
                return@setOnClickListener
            }
            startActivity(saveAndSignUp())
        }


        sharedPreferencesHelper= SharedPreferencesHelper(this)


        profileImage.setImageResource(imageList[imageIndex])

        selectImageButton.setOnClickListener {
            imageIndex = (imageIndex + 1) % imageList.size
            profileImage.setImageResource(imageList[imageIndex])
        }
    }

    private fun saveAndSignUp(): Intent {
        val userFirstName = editUserFirstName.text.toString()
        val userLastName = editUserLasttName.text.toString()
        val userProfileImage = imageList[imageIndex]

        val userModel = UserModel(userFirstName,userLastName,userProfileImage,true)

        sharedPreferencesHelper.saveUser(userModel)

        val intent= Intent(this, MainActivity::class.java)
        finish()
        return intent

    }

    private fun isEmpty():Boolean{
        val userFirstName = editUserFirstName.text.toString()
        val userLastName = editUserLasttName.text.toString()
        if (userFirstName.isEmpty()&&userLastName.isEmpty()){
            Toast.makeText(this,"Ism va familya kiriting😡",Toast.LENGTH_LONG).show()
            return true
        }
        if (userFirstName.isEmpty()){
            Toast.makeText(this,"Ismni kim kiritadi 🧐",Toast.LENGTH_LONG).show()
            return true
        }
        if (userLastName.isEmpty()){
            Toast.makeText(this,"Familyani kim kiritadi 🧐",Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }
}