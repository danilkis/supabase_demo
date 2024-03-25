package com.example.supabasedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.supabasedemo.navigation.GeneralNavigation
import com.example.supabasedemo.ui.theme.SupabaseDemoTheme
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
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
                    val ShelfVM = ShelfViewmodel() //TODO: Кривая реализация. ПОЧИНИТЬ
                    val ThingVM = ThingsViewmodel()
                    val PersonVM = PersonsViewmodel()
                    GeneralNavigation(PersonVM, ThingVM, ShelfVM)
                }
            }
        }
    }
}

