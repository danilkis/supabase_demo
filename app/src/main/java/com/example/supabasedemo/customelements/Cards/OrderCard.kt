package com.example.supabasedemo.customelements.Cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Things.Orders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderCard(Order: Orders, navController: NavController) {  //
    val coroutineScope = rememberCoroutineScope()
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
            .clickable {
                coroutineScope.launch(Dispatchers.Main) {
                    navController.navigate("order/${Order.id}")
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
            UserHead(id = "a", firstName = Order.name, lastName = "", size = 40.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = Order.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row()
                {
                    val dateFormat_yyyyMMddHHmmss = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH
                    )
                    val date = dateFormat_yyyyMMddHHmmss.parse(Order.deadline.replace("T", " "))
                    Text(
                        text = "Срок: ${date}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}