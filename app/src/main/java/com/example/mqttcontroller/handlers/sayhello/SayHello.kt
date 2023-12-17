package com.example.mqttcontroller.handlers.sayhello

import com.example.mqttcontroller.RemoteProcedureCall.Companion.logTopicInfo
import com.example.mqttcontroller.handlers.BaseHandler
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.MqttMessage

class SayHelloHandler: BaseHandler() {
    class MySayHelloRequest(val name: String)
    class SayHelloResponse(val greeting: String)


    override fun procedureName() = "${SayHelloHandler::class.java}"
    override fun getProcedure(): (String, MqttMessage) -> String = processMessage
    companion object {
        val processMessage = fun(topicFilter: String, data: MqttMessage): String {
            logTopicInfo(topicFilter)
            val request = Gson().fromJson("${data.payload}", MySayHelloRequest::class.java)
            // do we need this part?
            val message = "Hello ${request.name}"
            println(message)
            return Gson().toJson(SayHelloResponse(message))
        }
    }
}