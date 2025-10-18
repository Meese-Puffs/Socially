package com.Ahmad_Kamran.i230622

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FifthActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fifth_activity)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(listOf())
        recyclerView.adapter = adapter

        // 🔥 connect to your "posts" node in Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("posts")

        // 👇 read safely
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()
                for (postSnap in snapshot.children) {
                    try {
                        val post = postSnap.getValue(Post::class.java)
                        if (post != null) postsList.add(post)
                    } catch (e: Exception) {
                        Log.e("FirebaseError", "Error parsing post: ${e.message}")
                    }
                }
                adapter = PostAdapter(postsList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }
}