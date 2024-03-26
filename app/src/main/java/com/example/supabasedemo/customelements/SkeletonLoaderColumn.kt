package com.example.supabasedemo.customelements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.supabasedemo.customelements.Cards.LoadingCard

@Preview
@Composable
fun SkeletonLoaderColumn(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues), verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        items(5)
        {
            LoadingCard()
        }
    }
}