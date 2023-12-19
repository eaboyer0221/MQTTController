package com.example.mqttcontroller.handlers

import com.example.mqttcontroller.RemoteProcedureCall
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

abstract class BaseHandler : RemoteProcedureCall {
    override fun handle(message: MqttMessage, client: IMqttClient) {
        // Extract the identifier from the request message
        val identifier = message.payload.toString().split("-").first()

        // Process the request data based on the topic (e.g., sayHello)
        val response = getMessageProcessingProcedure()(identifier, message)

        // If temporary mapping is used, store the pending response
        val pendingResponses = mutableMapOf<String, String>()
        pendingResponses[identifier] = response

        // Generate the response topic
        val responseTopic = "${procedureName()}/response/$identifier"

        // Publish the response to the generated topic

        client.publish(responseTopic, response.toByteArray(), 0, false)

        // Remove the response from the map after sending (optional)
        pendingResponses.remove(identifier)
    }

    abstract fun procedureName(): String
    fun getTopicFilter(): String = "${procedureName()}/request"
    abstract fun getMessageProcessingProcedure():  (String, MqttMessage) -> String

    override fun getResponseTopic(topicName: String): String {
        TODO("Not yet implemented")
    }
}

