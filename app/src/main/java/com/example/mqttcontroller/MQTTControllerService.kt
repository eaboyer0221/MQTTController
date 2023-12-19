package com.example.mqttcontroller

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.mqttcontroller.handlers.saygoodbye.SayGoodbyeHandler
import com.example.mqttcontroller.handlers.sayhello.SayHelloHandler
import org.eclipse.paho.client.mqttv3.*

class MQTTControllerService : Service() {
    val brokerUrl = "url" // Replace with your broker URL //
    val clientId = "client_id" // Replace with your unique client ID
    val username = "un" // Replace with your username (optional)
    val password = "pw" // Replace with your password (optional)

    // Define a temporary map to store pending responses (optional)
    val client = MqttAsyncClient(brokerUrl, clientId)

    // Define a map of handler to function that should be run for each remote procedure call
    private val procedures: Map<String, (String, MqttMessage) -> String> =  listOf(
        SayHelloHandler(),
        SayGoodbyeHandler()
    ).map {
        // Subscribe to the topic filter associated with each handler
        client.subscribe(it.getTopicFilter(), 0) { topicFilter, message ->
            // Use the handler to run the procedure & get the response any time a message comes in
            val responseMessage = it.getMessageProcessingProcedure()(topicFilter, message)
            val responseTopic = it.getResponseTopic(it.procedureName())
            client.publish(responseTopic, responseMessage.toByteArray(), 0, false)
        }
        // make a map with class as key and procedure to run as value
        it.getMessageProcessingProcedure()
    }.associateBy { "${it.javaClass}" }

    override fun onBind(intent: Intent?): IBinder? = null

    init {
        val options = MqttConnectOptions()
        options.isCleanSession = true

        if (username.isNotBlank() && password.isNotBlank()) {
            // Todo which of these are not needed?
            options.userName = username
            options.password = password.toCharArray()
            options.isAutomaticReconnect = true
            options.connectionTimeout = Int.MAX_VALUE
            options.keepAliveInterval = 20
            options.maxReconnectDelay = 25
        }
        client.connect()
    }
}
