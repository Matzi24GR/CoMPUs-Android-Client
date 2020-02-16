package com.example.uom.Fragment


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.uom.R
import com.example.uom.utils.login
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences  = context!!.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE)

        val usrText = view.findViewById<TextInputLayout>(R.id.username_textView)
        val passwdText = view.findViewById<TextInputLayout>(R.id.password_textView)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        fun loginFunc(context: Context?) {
            usrText.editText!!.isEnabled = false
            passwdText.editText!!.isEnabled = false
            progressBar.visibility = View.VISIBLE

            val username = usrText.editText!!.text.toString()
            val password = passwdText.editText!!.text.toString()

            if (username == "") {
                usrText.error = "Cant be blank"
                usrText.isErrorEnabled = true
            }
            if (password == "") {
                passwdText.error = "Cant be blank"
                passwdText.isErrorEnabled = true
            }

            if (username.contains("001")) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/AG8VFyW61e0")))
                Toast.makeText(context,"RIP Stefan Karl",Toast.LENGTH_LONG).show()
            }

            if (username != "" && password != "") {

                sharedPreferences.edit().putString("username", username).apply()
                sharedPreferences.edit().putString("password", password).apply()

                val loginJob = GlobalScope.launch (Dispatchers.IO) { login(context!!) }

                var Message: String? = null
                loginJob.invokeOnCompletion() {
                    if (Looper.myLooper() == null) Looper.prepare()
                    when (sharedPreferences.getString("status",null)) {

                        "wrong" -> Message = "Wrong Username/Password"

                        "error" -> Message = "Check your internet connection"

                        "success" -> {
                            findNavController().navigate(R.id.action_toStart)
                        }

                        else -> Message = "This shouldn't ever happen"
                    }
                    activity!!.runOnUiThread {
                        progressBar.visibility = View.INVISIBLE
                        if(Message != null ) Toast.makeText(context,Message,Toast.LENGTH_LONG).show()
                        usrText.editText!!.isEnabled = true
                        passwdText.editText!!.isEnabled = true
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            loginFunc(context)
        }

        passwdText.editText!!.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_GO){
                Log.i("LoginEnterKey","PRESSED!")
                loginFunc(context)
                true
            } else {
                false
            }
        }


    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

}
