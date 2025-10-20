package com.Ahmad_Kamran.i230622

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EighthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var userListContainer: LinearLayout
    private lateinit var chevron: ImageView
    private lateinit var plus: ImageView
    private lateinit var nameText: TextView

    private var senderUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure this matches your activity layout filename
        setContentView(R.layout.eighth_activity)

        // init firebase
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        senderUid = auth.currentUser?.uid
        if (senderUid == null) {
            Toast.makeText(this, "No logged in user. Return to login.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // make sure these IDs exist in activity_eighth.xml
        userListContainer = findViewById(R.id.userListContainer)
        chevron = findViewById(R.id.chevron)
        plus = findViewById(R.id.Plus)
        nameText = findViewById(R.id.name)

        loadCurrentUserName()
        loadUsersFromFirebase()

        chevron.setOnClickListener { finish() }
        plus.setOnClickListener { Toast.makeText(this, "Add new chat coming soon!", Toast.LENGTH_SHORT).show() }
    }

    private fun loadCurrentUserName() {
        senderUid?.let { uid ->
            dbRef.child(uid).child("username").get()
                .addOnSuccessListener { snapshot ->
                    nameText.text = snapshot.value?.toString() ?: "Unknown"
                }
                .addOnFailureListener {
                    nameText.text = "Unknown"
                }
        }
    }

    private fun loadUsersFromFirebase() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userListContainer.removeAllViews()

                if (!snapshot.exists()) {
                    Toast.makeText(this@EighthActivity, "No users found in database", Toast.LENGTH_SHORT).show()
                    return
                }

                val inflater = LayoutInflater.from(this@EighthActivity)

                for (userSnap in snapshot.children) {
                    val uid = userSnap.key ?: continue
                    if (uid == senderUid) continue // skip yourself

                    val username = userSnap.child("username").getValue(String::class.java) ?: "Unknown"
                    val firstName = userSnap.child("firstName").getValue(String::class.java) ?: ""
                    val lastName = userSnap.child("lastName").getValue(String::class.java) ?: ""

                    val displayName = if (firstName.isNotBlank() || lastName.isNotBlank())
                        "${firstName.trim()} ${lastName.trim()}".trim()
                    else
                        username

                    // inflate the item layout (you MUST create user_channel_item.xml)
                    val userView = inflater.inflate(R.layout.user_channel, userListContainer, false)

                    val usernameText = userView.findViewById<TextView>(R.id.usernameText)
                    val lastMessageText = userView.findViewById<TextView>(R.id.lastMessageText)
                    val timestampText = userView.findViewById<TextView>(R.id.timestampText)
                    val userImage = userView.findViewById<ImageView>(R.id.userImage)

                    usernameText.text = if (displayName.isNotBlank()) displayName else username
                    lastMessageText.text = "Tap to start chatting..."
                    timestampText.text = "now"
                    userImage.setImageResource(R.drawable.face1) // default avatar; change if you store URLs

                    userListContainer.addView(userView)

                    userView.setOnClickListener {
                        openChatWith(uid)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EighthActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openChatWith(receiverUid: String) {
        val intent = Intent(this, NinthActivity::class.java)
        intent.putExtra("senderUid", senderUid)
        intent.putExtra("receiverUid", receiverUid)
        startActivity(intent)
    }
}
