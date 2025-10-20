package com.Ahmad_Kamran.i230622

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.messaging.FirebaseMessaging
class SecondActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        auth = FirebaseAuth.getInstance()

        val db = FirebaseDatabase.getInstance().reference


        val firstNameField = findViewById<EditText>(R.id.YourName_box)
        val lastNameField = findViewById<EditText>(R.id.YourLastName_box)
        val usernameField = findViewById<EditText>(R.id.username_box)
        val dobField = findViewById<EditText>(R.id.DateOfBirth_box)
        val emailField = findViewById<EditText>(R.id.Email_box)
        val passwordField = findViewById<EditText>(R.id.Password_box)
        val createAccount = findViewById<TextView>(R.id.create_account)
        val back = findViewById<ImageView>(R.id.arrow)

        createAccount.setOnClickListener {
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val username = usernameField.text.toString().trim()
            val dob = dobField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                dob.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 1: Create user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Step 2: Store only profile info in Firestore
                        val userMap = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "username" to username,
                            "DateOfBirth" to dob
                        )

                        db.child("users").child(userId).setValue(userMap).addOnSuccessListener {
                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ThirdActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            prefs.edit()
                .putString("username", username)
                .putString("firstName", firstName)
                .putString("lastName", lastName)
                .putString("dob", dob)
                .apply()

        }

        back.setOnClickListener {
            val intent = Intent(this, FourthActivity::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(uid)
                        .child("fcmToken")
                        .setValue(token)
                }
            }
        }


    }
}
