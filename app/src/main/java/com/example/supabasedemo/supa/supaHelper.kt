package com.example.supabasedemo.supa

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine

class supaHelper {
    companion object
    {
        var key: String = ""
        var url: String = ""
        val client = createSupabaseClient(
            supabaseUrl = "https://pavlovskhome.ru/",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyAgCiAgICAicm9sZSI6ICJhbm9uIiwKICAgICJpc3MiOiAic3VwYWJhc2UtZGVtbyIsCiAgICAiaWF0IjogMTY0MTc2OTIwMCwKICAgICJleHAiOiAxNzk5NTM1NjAwCn0.dc_X5iR_VP_qT0zsiyj_I_OZ2T9FtRU2BBNWN8Bu4GE"
        ) {
            install(Postgrest)
            install(GoTrue)
            httpEngine = OkHttpEngine(OkHttpConfig())
        }
        suspend fun getAsyncClient(): SupabaseClient {
            return client
        }
    }
}