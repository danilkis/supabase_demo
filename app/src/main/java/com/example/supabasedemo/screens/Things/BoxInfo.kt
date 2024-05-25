package com.example.supabasedemo.screens.Things

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.BoxInfoHeader
import com.example.supabasedemo.model.States
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxInfoScreen( //TODO: Если пусто показать надпись что пусто
    box: Box, navController: NavController, viewModel: ThingsViewmodel = viewModel()
) {
    Column(modifier = Modifier.fillMaxSize())
    {
        BoxInfoHeader(box = box)
        ThingColumn(navController = navController, paddingValues = 5.dp)
        //BoxInfoThingColumn(box = box, navController = navController)
         var currentState by remember { mutableStateOf(States.Empty) }
//        val things by viewModel.things.collectAsState(initial = mutableListOf())
//        Crossfade(targetState = currentState, animationSpec = tween(300, 100)) { state ->
//            when (state) {
//                States.Info -> Info()
//                States.Loading -> PersonLoading(person = person)
//                States.Loaded -> PersonLoaded(person = person, contact = contact)
//                States.Empty -> Box {}
//            }
//        }
//        if (things.size == 0) {
//            currentState = States.Info
//        } else {
//            if (contact.id == 0 && contact.phone == "0") {
//                currentState = States.Loading
//            } else {
//                currentState = States.Loaded
//            }
//        }
    }

}

