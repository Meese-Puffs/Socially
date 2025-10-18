package com.Ahmad_Kamran.i230622

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.UUID

class UploadPostActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null

    private lateinit var imageView: ImageView
    private lateinit var captionInput: EditText
    private lateinit var uploadButton: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ correct layout name and closing parenthesis
        setContentView(R.layout.activity_upload_post)

        imageView = findViewById(R.id.selected_image)
        captionInput = findViewById(R.id.captionInput)
        uploadButton = findViewById(R.id.uploadButton)
        backButton = findViewById(R.id.backButton)

        // Get URI from FifthActivity
        val uriString = intent.getStringExtra("selectedImageUri")
        if (!uriString.isNullOrEmpty()) {
            selectedImageUri = Uri.parse(uriString)
            imageView.setImageURI(selectedImageUri)
        } else {
            Toast.makeText(this, "No image received", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Back to feed
        backButton.setOnClickListener {
            startActivity(Intent(this, FifthActivity::class.java))
            finish()
        }

        // Upload to Realtime DB (Base64)
        uploadButton.setOnClickListener {
            val caption = captionInput.text.toString()
            val uri = selectedImageUri
            if (uri == null) {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadPostToFirebase(uri, caption)
        }
    }

    private fun uploadPostToFirebase(imageUri: Uri, caption: String) {
        try {
            // Convert to Bitmap (simple, works with SAF)
            val bitmap = if (Build.VERSION.SDK_INT < 29) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } else {
                // For API 29+, getBitmap is still okay via SAF; this keeps it simple
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            }

            // Compress + encode
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
            val imageBytes = outputStream.toByteArray()
            val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            // Build the Post object
            val postId = UUID.randomUUID().toString()
            val post = Post(
                id = postId,
                username = "You",
                imageBase64 = base64Image,
                caption = caption,
                timestamp = System.currentTimeMillis().toString()
            )

            // Save under /posts/{postId}
            val dbRef = FirebaseDatabase.getInstance()
                .getReference("posts")
                .child(postId)

            dbRef.setValue(post)
                .addOnSuccessListener {
                    Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, FifthActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_LONG).show()
                }

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}