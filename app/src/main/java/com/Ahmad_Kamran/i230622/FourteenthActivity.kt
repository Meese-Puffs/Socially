package com.Ahmad_Kamran.i230622

import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FourteenthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fourteenth_activity)

        val cancelButton : Button = findViewById(R.id.cancelButton)

        cancelButton.setOnClickListener{
            val intent = Intent(this, ThirteenthActivity::class.java)
            startActivity(intent)
        }

        val doneButton : Button = findViewById(R.id.doneButton)

        doneButton.setOnClickListener{
            val intent = Intent(this, ThirteenthActivity::class.java)
            startActivity(intent)
        }

        UserUtils.fetchUserProfile { profile ->
            if (profile != null) {
                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email

                if (email != null) {
                    findViewById<TextView>(R.id.Email2).text = email
                }
                findViewById<TextView>(R.id.User2).text = profile.username ?: ""
                findViewById<TextView>(R.id.Name2).text = "${profile.firstName ?: ""} ${profile.lastName ?: ""}"

            }
            else {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }

        }



    }


}