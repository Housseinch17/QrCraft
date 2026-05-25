package com.example.qrcraft.app.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.qrcraft.core.data.model.BarModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val BarModelNavType = object : NavType<BarModel>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): BarModel? {
            return bundle.getString(key)?.let { Json.decodeFromString(it) }
        }

        override fun parseValue(value: String): BarModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: BarModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: BarModel) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}