package com.example.androiod_exam


import com.example.androiod_exam.ui.theme.AndroiodExamTheme


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.androiod_exam.DataClass.AppContainer

class MainActivity : ComponentActivity() {
    private val appContainer: AppContainer by lazy { AppContainer(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroiodExamTheme {
                val navController = rememberNavController()

                Navigation(
                    navController = navController,
                    productViewModel = appContainer.productViewModel,
                    shoppingCartViewModel = appContainer.shoppingCartViewModel,
                    appContainer = appContainer,
                    orderViewModel = appContainer.orderViewModel // Legg til denne linjen
                )
            }
        }
    }
}