package com.example.mqttcontroller.handlers.sayhello

import android.util.Log
import com.example.mqttcontroller.RemoteProcedureCall.Companion.logTopicInfo
import com.example.mqttcontroller.handlers.BaseHandler
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.MqttMessage

class SayHelloHandler: BaseHandler() {
    class MySayHelloRequest(val name: String)
    class SayHelloResponse(val greeting: String)

    override fun procedureName() = "${SayHelloHandler::class.java}"
    override fun getMessageProcessingProcedure() = messageProcessingProcedure
    override fun getResponseTopic(topicName: String): String
    = topicName.replace("request", "response")

    companion object {
        private val TAG: String = Companion::class.java.simpleName.replace("Companion", "")
        val messageProcessingProcedure = fun(topicFilter: String, data: MqttMessage): String {
            logTopicInfo(topicFilter)
            val request = Gson().fromJson("${data.payload}", MySayHelloRequest::class.java)
            val message = "Hello ${request.name}"
            Log.d(TAG, message)
            return Gson().toJson(SayHelloResponse(message))
        }
    }
}