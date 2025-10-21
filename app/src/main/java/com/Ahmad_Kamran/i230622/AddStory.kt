package com.Ahmad_Kamran.i230622

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddStoryActivity : AppCompatActivity() {

    private lateinit var selectedImage: Uri
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        imageView = findViewById(R.id.selectedStoryImage)
        val uploadBtn = findViewById<Button>(R.id.uploadStoryButton)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        uploadBtn.setOnClickListener {
            uploadStoryToFirebase()
        }
    }

    private fun uploadStoryToFirebase() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val ref = FirebaseStorage.getInstance().reference.child("stories/${user.uid}.jpg")

        ref.putFile(selectedImage).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->
                val story = Story(
                    storyId = user.uid,
                    username = user.displayName ?: "User",
                    imageUrl = uri.toString(),
                    timestamp = System.currentTimeMillis()
                )
                FirebaseDatabase.getInstance().getReference("stories")
                    .child(user.uid)
                    .setValue(story)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data!!
            imageView.setImageURI(selectedImage)
        }
    }
}