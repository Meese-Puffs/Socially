package com.Ahmad_Kamran.i230622

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class UserProfile(
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val dateOfBirth: String? = null
)

object UserUtils {

    fun fetchUserProfile(callback: (UserProfile?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            callback(null)
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserProfile::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun fetchUsername(callback: (String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            callback(null)
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.child("username").get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.value as? String)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
