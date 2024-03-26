package com.example.supabasedemo.customelements.Cards

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.supabasedemo.customelements.UserHead

@Composable
fun ContactCard(contactName: String, value: String) {
    val ctx = LocalContext.current
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, top = 5.dp, end = 0.dp, bottom = 0.dp)
            .clickable {
                if (contactName == "Phone") {
                    val intent =
                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$value"))
                    ContextCompat.startActivity(ctx, intent, null)
                } else if (contactName == "Telegram") {
                    val username = value.replace("@", "")
                    val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$username"))
                    telegram.setPackage("org.telegram.messenger")
                    ContextCompat.startActivity(ctx, telegram, null)
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
            UserHead(id = "a", firstName = contactName, lastName = "", size = 40.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}