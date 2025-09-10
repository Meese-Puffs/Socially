package com.Ahmad_Kamran.i230622

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.text.InputType
import android.widget.TextView
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        val passwordBox = findViewById<EditText>(R.id.Password_box)
        val eyeIcon = findViewById<ImageView>(R.id.visibility)

        eyeIcon.setOnClickListener {
            if (passwordBox.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                passwordBox.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                //eyeIcon.setImageResource(R.drawable.baseline_visibility_off_24)
            } else {
                passwordBox.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.baseline_visibility_24)
            }
            passwordBox.setSelection(passwordBox.text?.length ?: 0)

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second_menu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val create_Account = findViewById<TextView>(R.id.create_account)

        create_Account.setOnClickListener{
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

    }
}