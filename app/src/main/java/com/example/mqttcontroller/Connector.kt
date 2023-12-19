package com.example.mqttcontroller

import android.content.Context
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
//import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
//import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
//import software.amazon.awssdk.regions.Region
//import software.amazon.awssdk.services.sts.StsClient
//import software.amazon.awssdk.services.sts.model.AssumeRoleRequest
//import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider.Builder


class Connector(val awsRegion: String) {
    fun createMqttClient(applicationContext: Context) {
        val stsClient = STSAssumeRoleSessionCredentialsProvider()
            .region(Region())
            .build()

        // Assuming you have an existing credentials provider (e.g., DefaultCredentialsProvider)
        val baseCredentialsProvider: AWSCredentialsProvider = DefaultCredentialsProvider.create()

        // Specify the role ARN and external ID
        val roleArn = "arn:aws:iam::account-id-with-role:role/role-name"
        val externalId = "your-external-id"

        // Create the AssumeRole request
        val assumeRoleRequest = AssumeRoleRequest.Builder()
            .roleArn(roleArn)
            .roleSessionName("your-session-name")
            .externalId(externalId)
            .build()

        // Create the STSAssumeRoleSessionCredentialsProvider
        val stsCredentialsProvider = STSAssumeRoleSessionCredentialsProvider.Builder()
            .stsClient(stsClient)
            .refreshRequest { baseCredentialsProvider.resolveCredentials() }
            .build()

        // Use the temporary credentials to create MqttAndroidClient
        val mqttClientId = "your-client-id"
        val mqttServerUri = "ssl://your-iot-endpoint:8883"

        val mqttClient = MqttAndroidClient(
            applicationContext,
            mqttServerUri,
            mqttClientId,
            MemoryPersistence()
        )

        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.userName = "use-token-auth"
        mqttConnectOptions.password = stsCredentialsProvider.resolveCredentials().sessionToken().toCharArray()

        mqttClient.connect(mqttConnectOptions)
    }
}