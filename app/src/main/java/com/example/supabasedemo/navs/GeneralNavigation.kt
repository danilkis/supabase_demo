package com.example.supabasedemo.navs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.supabasedemo.screens.Auth
import com.example.supabasedemo.screens.BoxInfoScreen
import com.example.supabasedemo.screens.Hello
import com.example.supabasedemo.screens.MainScreen
import com.example.supabasedemo.screens.OrderInfoScreen
import com.example.supabasedemo.screens.PersonInfoScreen
import com.example.supabasedemo.screens.SearchResultScreen
import com.example.supabasedemo.viewmodel.OrderViewmodel
import com.example.supabasedemo.viewmodel.PersonsViewmodel
import com.example.supabasedemo.viewmodel.ThingsViewmodel

//TODO: Настроить навигацию

@Composable
fun GeneralNavigation() {
    val navController = rememberNavController()
    val personVm = PersonsViewmodel()
    val thingVm = ThingsViewmodel()
    val ordersVm = OrderViewmodel()
    val persons by personVm.newPersons.collectAsState(initial = listOf())
    val boxes by thingVm.boxes.collectAsState(initial = listOf())
    val orders by ordersVm.orders.collectAsState(initial = listOf())
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            Auth(navController)
            //Ввод адреса сервера
        }
        composable("helloScreen") {
            Hello(navController)
            //Авторизация
        }
        composable("mainScreen") {
            MainScreen(navController)
            //Переход на главный экран
        }
        composable(
            "person/{personInfoId}",
            arguments = listOf(navArgument("personInfoId") { type = NavType.IntType })
        ) {
            val personInfoId: Int = it.arguments?.getInt("personInfoId") ?: 0
            persons.forEach { it ->
                if (it.id == personInfoId) {
                    PersonInfoScreen(person = it, navController)
                }
            }
        }
        composable(
            "box/{boxId}",
            arguments = listOf(navArgument("boxId") { type = NavType.IntType })
        ) {
            val personInfoId: Int = it.arguments?.getInt("boxId") ?: 0
            boxes.forEach { it ->
                if (it.id == personInfoId) {
                    BoxInfoScreen(it, navController)
                }
            }
        }
        composable(
            "order/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) {
            val orderID: Int = it.arguments?.getInt("orderId") ?: 0
            orders.forEach { it ->
                if (it.id == orderID) {
                    OrderInfoScreen(it, navController)
                }
            }
        }
        composable(
            "searchResults/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query")
            SearchResultScreen(query = query!!, navController = navController)
        }
    }
}