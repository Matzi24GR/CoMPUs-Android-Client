package com.example.uom.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uom.R
import com.example.uom.Utils.login
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val sharedPreferences  = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)

        val usrText = findViewById<TextInputLayout>(R.id.username_textview)
        val passwdText = findViewById<TextInputLayout>(R.id.password_textview)
        val loginButton = findViewById<FloatingActionButton>(R.id.login_button)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        loginButton.setOnClickListener() {
            progressBar.visibility = View.VISIBLE

            val username = usrText.editText!!.text.toString()
            val password = passwdText.editText!!.text.toString()

            if (username == "") {
                usrText.error = "Cant be blank"
                usrText.isErrorEnabled = true
            } else usrText.isErrorEnabled = false
            if (password == "") {
                passwdText.error = "Cant be blank"
                passwdText.isErrorEnabled = true
            } else passwdText.isErrorEnabled = false

            if (username.contains("001"))
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/AG8VFyW61e0")))

            if (username != "" && password != "") {

                val cookie = login(username, password)

                if (cookie != null) {
                    sharedPreferences.edit().putString("username",username).apply()
                    sharedPreferences.edit().putString("password",password).apply()
                    sharedPreferences.edit().putString("cookie",cookie).apply()
                    finish()
                } else Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
        }
    }
}
