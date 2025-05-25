package com.example.becycle.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts

object JwtUtils {
    fun decodeUserId(jwtToken: String): String? {
        return try {
            // Split the JWT into parts
            val parts = jwtToken.split(".")
            if (parts.size != 3) return null

            // Decode the payload (second part)
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))

            // Parse as JSON and extract user_id
            val json = org.json.JSONObject(payload)
            json.getString("user_id")
        } catch (e: Exception) {
            null
        }
    }
}