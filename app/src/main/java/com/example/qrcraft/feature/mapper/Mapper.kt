package com.example.qrcraft.feature.mapper

import com.example.qrcraft.core.data.model.BarModel
import com.example.qrcraft.core.data.model.ScannedContent
import com.google.mlkit.vision.barcode.common.Barcode

private fun Barcode.toScannedContent(): ScannedContent {
    return when (valueType) {
        Barcode.TYPE_URL -> ScannedContent.LinkContent(
            url = url?.url.orEmpty()
        )

        Barcode.TYPE_TEXT -> ScannedContent.TextContent(
            value = rawValue.orEmpty()
        )

        Barcode.TYPE_CONTACT_INFO -> ScannedContent.ContactContent(
            name = contactInfo?.name?.formattedName.orEmpty(),
            phone = contactInfo?.phones?.firstOrNull()?.number,
            email = contactInfo?.emails?.firstOrNull()?.address
        )

        Barcode.TYPE_EMAIL -> ScannedContent.EmailContent(
            address = email?.address.orEmpty()
        )

        Barcode.TYPE_PHONE -> ScannedContent.PhoneContent(
            number = phone?.number.orEmpty()
        )

        Barcode.TYPE_WIFI -> ScannedContent.WifiContent(
            ssid = wifi?.ssid.orEmpty(),
            password = wifi?.password,
            securityType = wifi?.encryptionType.toString()
        )

        Barcode.TYPE_GEO -> ScannedContent.GeoContent(
            latitude = geoPoint?.lat.toString(),
            longitude = geoPoint?.lng.toString()
        )
        ////for unknown types
        else -> ScannedContent.TextContent(
            value = rawValue.orEmpty()
        )
    }
}

fun ScannedContent.displayType(): String{
    return when(this){
        is ScannedContent.ContactContent -> "Contact"
        is ScannedContent.EmailContent -> "Email"
        is ScannedContent.LinkContent -> "Link"
        is ScannedContent.PhoneContent -> "Phone Number"
        is ScannedContent.TextContent -> "Text"
        is ScannedContent.WifiContent -> "Wifi"
        is ScannedContent.GeoContent -> "Geolocation"
    }
}
fun Barcode.toBarModel() = BarModel(
    content = toScannedContent(),
    rawValue = rawValue.orEmpty(),
    displayValue = displayValue,
    format = format
)