package com.example.mqttcontroller.handlers.saygoodbye

import com.example.mqttcontroller.RemoteProcedureCall
import com.example.mqttcontroller.handlers.BaseHandler
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.MqttMessage


class SayGoodbyeHandler : BaseHandler() {
    class SayGoodbyeRequest(val name: String)
    class SayGoodbyeResponse(val greeting: String)


    override fun procedureName() = "${SayGoodbyeHandler::class.java}"
    override fun getMessageProcessingProcedure() = processMessage

    override fun getProcedure(): (String, MqttMessage) -> String = processMessage
    companion object {
        val processMessage = fun(topicFilter: String, data: MqttMessage): String {
            RemoteProcedureCall.logTopicInfo(topicFilter)
            val request = Gson().fromJson("${data.payload}", SayGoodbyeRequest::class.java)
            // do we need this part?
            val message = "Hello ${request.name}"
            println(message)
            return Gson().toJson(SayGoodbyeResponse(message))
        }
    }
}