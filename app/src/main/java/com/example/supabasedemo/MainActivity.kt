package com.example.supabasedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.ui.theme.SupabaseDemoTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupabaseDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    LaunchedEffect(true){
                        get_info_from_supa()
                    }
                }
            }
        }
    }
}

var text: String = ""
val client = createSupabaseClient(
    supabaseUrl = "http://91.185.84.211/api/v1",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyAgCiAgICAicm9sZSI6ICJhbm9uIiwKICAgICJpc3MiOiAic3VwYWJhc2UtZGVtbyIsCiAgICAiaWF0IjogMTY0MTc2OTIwMCwKICAgICJleHAiOiAxNzk5NTM1NjAwCn0.dc_X5iR_VP_qT0zsiyj_I_OZ2T9FtRU2BBNWN8Bu4GE"
) {
    install(Postgrest)
    install(GoTrue) {
        scheme = "public"
        host = "91.185.84.211"
        autoSaveToStorage = true
        autoLoadFromStorage = true
    }
    httpEngine = OkHttpEngine(OkHttpConfig())
}

suspend fun get_info_from_supa() {
    text = client.postgrest["Persons"].select(Columns.ALL).decodeList<Persons>().toString()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = com.example.supabasedemo.text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SupabaseDemoTheme {
        Greeting("Android")
    }
}