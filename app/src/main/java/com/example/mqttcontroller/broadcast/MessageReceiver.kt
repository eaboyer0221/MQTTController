package com.example.mqttcontroller.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageReceiver(intentName: String) : BroadcastReceiver() {

    // Define constants for your custom broadcast message actions
    private val MESSAGE_KEY = "message"
    private var responseIntent: String

    init {
        responseIntent = "$intentName.MESSAGE_RECEIVED"
    }

    // Optional listener interface for receiving messages
    interface MessageListener {
        fun onMessageReceived(message: String)
    }

    // Override the onReceive method to handle broadcast messages
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == responseIntent) {
            intent.getStringExtra(MESSAGE_KEY)?.fromJson()
            // Do any other processing with the message here
            receiverHandler.handle()
        }
    }
}

private fun String?.fromJson() {
    TODO("Not yet implemented")
}
