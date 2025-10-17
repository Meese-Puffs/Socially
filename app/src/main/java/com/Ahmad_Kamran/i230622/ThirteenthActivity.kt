package com.Ahmad_Kamran.i230622

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class ThirteenthActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thirteenth_activity)

        auth = FirebaseAuth.getInstance()

        val editButton: Button = findViewById(R.id.editButton)

        editButton.setOnClickListener {
            val intent = Intent(this, FourteenthActivity::class.java)
            startActivity(intent)
        }

        val highlightsButton: Button = findViewById(R.id.highlightButton)

        highlightsButton.setOnClickListener {
            val intent = Intent(this, SixteenthActivity::class.java)
            startActivity(intent)
        }

        val friendsButton: Button = findViewById(R.id.friendsButton)

        friendsButton.setOnClickListener {
            val intent = Intent(this, FifteenthActivity::class.java)
            startActivity(intent)
        }

        val sportButton: Button = findViewById(R.id.sportsButton)

        sportButton.setOnClickListener {
            val intent = Intent(this, FifteenthActivity::class.java)
            startActivity(intent)
        }

        val designButton: Button = findViewById(R.id.DesignButton)

        designButton.setOnClickListener {
            val intent = Intent(this, FifteenthActivity::class.java)
            startActivity(intent)
        }

        val homeButton = findViewById<Button>(R.id.homebutton)
        homeButton.setOnClickListener {
            val intent = Intent(this, FifthActivity::class.java)
            startActivity(intent)
        }

        val searchButton: Button = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val intent = Intent(this, SixthActivity::class.java)
            startActivity(intent)
        }

        val likesButton: Button = findViewById(R.id.likesButton)

        likesButton.setOnClickListener {
            val intent = Intent(this, EleventhActivity::class.java)
            startActivity(intent)
        }

        val logOutButton: Button = findViewById(R.id.logOutButton)

        logOutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, FourthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val cachedUsername = prefs.getString("username", "Loading...")

        findViewById<TextView>(R.id.name).text = cachedUsername

        UserUtils.fetchUsername { username ->
            if (username != null) {
                findViewById<TextView>(R.id.name).text = username
                prefs.edit().putString("username", username).apply()
            }
        }

        findViewById<TextView>(R.id.bio1).text = cachedUsername

        UserUtils.fetchUsername { username ->
            if (username != null) {
                findViewById<TextView>(R.id.name).text = username
                prefs.edit().putString("username", username).apply()
            }
        }

    }
}
