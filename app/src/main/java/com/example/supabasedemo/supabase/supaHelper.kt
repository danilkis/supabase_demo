package com.example.supabasedemo.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine

class supaHelper {
    companion object {
        var key: String =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyAgCiAgICAicm9sZSI6ICJzZXJ2aWNlX3JvbGUiLAogICAgImlzcyI6ICJzdXBhYmFzZS1kZW1vIiwKICAgICJpYXQiOiAxNjQxNzY5MjAwLAogICAgImV4cCI6IDE3OTk1MzU2MDAKfQ.DaYlNEoUrrEn2Ig7tqibS-PHK5vgusbcbo7X36XVt4Q"
        var url: String = ""
        val client = createSupabaseClient(
            supabaseUrl = "https://supabase.pavlovskhome.ru/",
            supabaseKey = key
        ) {
            install(Postgrest)
            install(GoTrue)
            install(Storage)
            {
                jwtToken = key
            }
            httpEngine = OkHttpEngine(OkHttpConfig())
        }

        fun getAsyncClient(): SupabaseClient {
            return client
        }
    }
}