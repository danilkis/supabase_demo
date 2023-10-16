package com.example.supabasedemo.navs

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
//TODO: Настроить навигацию

@Composable
fun GeneralNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            //Auth(navController)
            //Ввод адреса сервера
        }
        composable("helloScreen") {
            //HelloScreen(navController)
            //Авторизация
        }
        composable("mainScreen") {
            //MainScreen(navController)
            //Переход на главный экран
        }
        composable(
            "person/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) {
            val personId: Int = it.arguments?.getInt("personId") ?: 0
            //LessonScreen(lessorn = lessonDtoId, navController)
        }
        composable("test"){
            //GameScreen()
        }
    }
}