package com.Ahmad_Kamran.i230622

data class Message(
    val senderUid: String = "",
    val receiverUid: String = "",
    val text: String = "",
    val timestamp: Long = 0
)
