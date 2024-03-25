package com.example.supabasedemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.supabasedemo.qrcode.Scanner.ReaderScreen
import com.example.supabasedemo.screens.Booting.Auth
import com.example.supabasedemo.screens.Booting.Hello
import com.example.supabasedemo.screens.Booting.MainScreen
import com.example.supabasedemo.screens.Persons.PersonInfoScreen
import com.example.supabasedemo.screens.Search.SearchResultScreen
import com.example.supabasedemo.screens.Shelf.ShelfInfoScreen
import com.example.supabasedemo.screens.Things.BoxInfoScreen
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel

//TODO: Настроить навигацию

@Composable
fun GeneralNavigation(
    personVm: PersonsViewmodel = PersonsViewmodel(),
    thingVm: ThingsViewmodel = ThingsViewmodel(),
    shelfVm: ShelfViewmodel = ShelfViewmodel()
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            Auth(navController)
            //Ввод адреса сервера
        }
        composable("helloScreen") {
            Hello(navController)
            //Авторизация
        }
        composable("Scanner") {
            ReaderScreen(navController, thingVm)
        }
        composable("mainScreen") {
            MainScreen(navController)
            //Переход на главный экран
        }
        composable(
            "person/{personInfoId}",
            arguments = listOf(navArgument("personInfoId") { type = NavType.IntType })
        ) {
            val persons by personVm.newPersons.collectAsStateWithLifecycle(initialValue = listOf())
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
            val boxes by thingVm.boxes.collectAsStateWithLifecycle(initialValue = listOf())
            val personInfoId: Int = it.arguments?.getInt("boxId") ?: 0
            boxes.forEach { it ->
                if (it.id == personInfoId) {
                    BoxInfoScreen(it, navController)
                }
            }
        }
        composable(
            "shelf/{shelfId}",
            arguments = listOf(navArgument("shelfId") { type = NavType.IntType })
        ) {
            val shelfID: Int = it.arguments?.getInt("shelfId") ?: 0
            val shelves by shelfVm.shelves.collectAsStateWithLifecycle(initialValue = listOf())
            shelves.forEach { it ->
                if (it.id == shelfID) {
                    ShelfInfoScreen(it, navController)
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