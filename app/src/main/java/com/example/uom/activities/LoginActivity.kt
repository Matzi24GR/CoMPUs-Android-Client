package com.example.uom.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uom.R
import com.example.uom.utils.login
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val sharedPreferences  = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)

        val usrText = findViewById<TextInputLayout>(R.id.username_textView)
        val passwdText = findViewById<TextInputLayout>(R.id.password_textView)
        val loginButton = findViewById<FloatingActionButton>(R.id.login_button)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        fun loginFunc() {

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

                sharedPreferences.edit().putString("username", username).apply()
                sharedPreferences.edit().putString("password", password).apply()

                val loginJob = GlobalScope.launch (Dispatchers.IO) { login(this@LoginActivity)}

                loginJob.invokeOnCompletion {

                    when (sharedPreferences.getString("status",null)) {

                        "wrong" -> Toast.makeText(this@LoginActivity,"Wrong Username/Password",Toast.LENGTH_SHORT).show()

                        "error" -> Toast.makeText(this@LoginActivity,"Check your internet connection",Toast.LENGTH_SHORT).show()

                        "success" -> {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }

                        else -> Toast.makeText(this@LoginActivity,"This shouldn't ever happen",Toast.LENGTH_SHORT).show()

                    }
                }


            }
        }
        loginButton.setOnClickListener {
            loginFunc()
            }

        passwdText.editText!!.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_GO){
                Log.i("LoginEnterKey","PRESSED!")
                loginFunc()
                true
            } else {
                false
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
