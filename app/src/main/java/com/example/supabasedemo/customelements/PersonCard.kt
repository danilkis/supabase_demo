package com.example.supabasedemo.customelements

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.model.ExpandShapeState
import com.example.supabasedemo.model.Persons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PersonCard(Person: Persons, navController: NavController, currState: ExpandShapeState) {
    val coroutineScope = rememberCoroutineScope()
    val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
    var currentState = currState
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
            .clickable {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                coroutineScope.launch(Dispatchers.Main) {
                    navController.navigate("person/${Person.id}")
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserHead(id = "a", firstName = Person.Name, lastName = Person.Surname.toString(), size = 40.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Text(
                    text = Person.Name + " " + Person.Surname.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}