package com.example.qrcraft.core.data.model

import kotlinx.serialization.Serializable


@Serializable
sealed interface ScannedContent {
    @Serializable

    data class TextContent(val value: String) : ScannedContent

    @Serializable
    data class LinkContent(val url: String) : ScannedContent

    @Serializable
    data class ContactContent(val name: String, val phone: String?, val email: String?) : ScannedContent

    @Serializable
    data class EmailContent(val address: String) : ScannedContent

    @Serializable
    data class PhoneContent(val number: String) : ScannedContent

    @Serializable
    data class WifiContent(val ssid: String, val password: String?, val securityType: String) : ScannedContent

    @Serializable
    data class GeoContent(val latitude: String, val longitude: String): ScannedContent
}