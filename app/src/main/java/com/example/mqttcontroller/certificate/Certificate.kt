package com.example.mqttcontroller.certificate

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.SecurityConstants
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.CertificateFactory

class Certificate {
    companion object{
        // Load root CA certificate
        val caCertPath = "/path/to/root_ca_certificate.pem"
        val caCertInputStream = FileInputStream(caCertPath)

        // Load client certificate
        val clientCertPath = "/path/to/client_certificate.pem"
        val clientCertInputStream = FileInputStream(clientCertPath)

        // Load client private key
        val privateKeyPath = "/path/to/client_private_key.pem"
        val privateKeyInputStream = FileInputStream(privateKeyPath)

        // Create a KeyStore instance
        val keyStore = KeyStore.getInstance("PKCS12")
    }
    init {
        // Load key store with password
        keyStore.load(null, null)

// Import root CA certificate
        keyStore.setCertificateEntry("caCert", CertificateFactory.getInstance("X.509").generateCertificate(caCertInputStream))

        keyStore.setKeyEntry("clientCert", object : Key {})
// Import client certificate and private key
        keyStore.setKeyEntry("clientCert",
            KeyStore.PrivateKeyEntry(
//                PrivateKeyFactory.getInstance("RSA").generatePrivate(privateKeyInputStream),
//                arrayOf(keyStore.getCertificate("caCert"))
            )
        )
        val options = MqttConnectOptions()
        options.isCleanSession = true

        val factory = org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory.getClientKeyStoreFactory(keyStore, null)
        options.setSSLSocketFactory(factory)

// Set TLS handshake timeout if needed
        options.setSSLHandshakeTimeout(5000)

// Set MQTT security level (optional)
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1) // for TLSv1.2
        options.setSecurityLevel(SecurityConstants.MQTT_SSL_V3) // for TLSv1.2

        val client = MqttClient(brokerUrl, clientId, options)
        client.connect()

    }

}

