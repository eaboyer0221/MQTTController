package com.example.mqttcontroller

import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

interface RemoteProcedureCall {
    fun handle(message: MqttMessage, client: IMqttClient)

    fun getProcedure() : (topicFilter: String, message: MqttMessage) -> String

    companion object {
        fun logTopicInfo(topicFilter: String) {
            Log.d("${
                RemoteProcedureCall::class.java}",
                "incomming message on topic $topicFilter"
            )
        }
    }
}
