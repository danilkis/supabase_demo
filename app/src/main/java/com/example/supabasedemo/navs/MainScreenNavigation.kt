package com.example.supabasedemo.navs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.supabasedemo.model.MainScreenDest
import com.example.supabasedemo.user
import kotlinx.serialization.json.jsonPrimitive


val destinations = listOf(
    MainScreenDest("People", Icons.Rounded.Person) {
        //Lessons(it)
    },
    MainScreenDest("Things", Icons.Rounded.Build) {
        Text(text = "Искуственный интелект")
    },
    MainScreenDest("Orders", Icons.Rounded.ShoppingCart) {
        //GameScreen()
    },
)

@Composable
fun MainScreenNavigation(navControllerGeneral: NavHostController) {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            destinations.forEach { dest ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == dest.name } == true,
                    onClick = {
                        navController.navigate(dest.name) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            dest.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(text = dest.name) })
            }
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val painter = rememberAsyncImagePainter(
                model = (user!!.userMetadata!!["avatar_url"]?.jsonPrimitive?.content
                    ?: "https://w7.pngwing.com/pngs/981/645/png-transparent-default-profile-united-states-computer-icons-desktop-free-high-quality-person-icon-miscellaneous-silhouette-symbol-thumbnail.png")
            )

            NavHost(
                navController = navController,
                startDestination = destinations.first().name,
                modifier = Modifier.padding(innerPadding)
            ) {
                for (dest in destinations) {
                    composable(dest.name) {
                        dest.content(navControllerGeneral)
                    }
                }
            }
            Image(
                painter,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(40.dp)
                    .clip(CircleShape)

            )
        }
    }
}