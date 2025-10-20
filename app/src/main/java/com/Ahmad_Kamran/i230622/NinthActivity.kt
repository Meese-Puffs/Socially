package com.Ahmad_Kamran.i230622

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class NinthActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var chatContainer: LinearLayout
    private lateinit var chatBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var chatId: String

    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var nameText: TextView  // Username at top

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ninth_activity)

        dbRef = FirebaseDatabase.getInstance().reference

        senderUid = intent.getStringExtra("senderUid") ?: return
        receiverUid = intent.getStringExtra("receiverUid") ?: return

        // Create unique chatId based on both UIDs
        chatId = if (senderUid < receiverUid)
            "${senderUid}_${receiverUid}"
        else
            "${receiverUid}_${senderUid}"

        chatContainer = findViewById(R.id.chatContainer)
        chatBox = findViewById(R.id.chatbox)
        sendButton = findViewById(R.id.share)
        nameText = findViewById(R.id.name)

        val callButton: Button = findViewById(R.id.callButton)
        val backButton: Button = findViewById(R.id.backButton)

        loadReceiverUsername()

        callButton.setOnClickListener {
            val intent = Intent(this, TenthActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, EighthActivity::class.java)
            startActivity(intent)
        }

        listenForMessages()

        sendButton.setOnClickListener {
            val messageText = chatBox.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                chatBox.text.clear()
            }
        }
    }

    // Fetch receiver's username
    private fun loadReceiverUsername() {
        val userRef = dbRef.child("users").child(receiverUid)
        userRef.child("username").get().addOnSuccessListener { snapshot ->
            val username = snapshot.value?.toString() ?: "Unknown"
            nameText.text = username
        }.addOnFailureListener {
            nameText.text = "Unknown"
        }
    }

    // Send message to Realtime Database
    private fun sendMessage(text: String) {
        val message = Message(senderUid, receiverUid, text, System.currentTimeMillis())
        dbRef.child("Messages").child(chatId).push().setValue(message)
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
            }
    }

    // Listen for messages in real-time
    private fun listenForMessages() {
        dbRef.child("Messages").child(chatId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatContainer.removeAllViews()
                    for (msgSnap in snapshot.children) {
                        val message = msgSnap.getValue(Message::class.java)
                        message?.let { displayMessage(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Display message in chat container
    private fun displayMessage(message: Message) {
        val textView = TextView(this)
        textView.text = message.text
        textView.textSize = 16f
        textView.setPadding(25, 10, 25, 10)

        if (message.senderUid == senderUid) {
            textView.setBackgroundResource(R.drawable.curved_rectangle)
            textView.setTextColor(resources.getColor(android.R.color.white))
            textView.backgroundTintList = getColorStateList(R.color.light_magenta)
        } else {
            textView.setBackgroundResource(R.drawable.curved_rectangle)
            textView.backgroundTintList = getColorStateList(R.color.pink)
        }

        chatContainer.addView(textView)
    }
}
