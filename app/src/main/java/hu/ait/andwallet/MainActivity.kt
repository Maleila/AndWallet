package hu.ait.andwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.ait.andwallet.screen.WalletScreen
import hu.ait.andwallet.screen.WalletSummaryScreen
import hu.ait.andwallet.ui.theme.AndWalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AndWalletNavHost()
                }
            }
        }
    }
}

@Composable
fun AndWalletNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "walletscreen"
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("walletscreen") { WalletScreen(
            onNavigateToSummary = {expense, income ->
            navController.navigate("walletsummary/$expense/$income")
        }
        )}
        composable("walletsummary/{numexpense}/{numincome}",
            arguments = listOf(
                    navArgument("numexpense"){type = NavType.IntType},
                    navArgument("numincome"){type = NavType.IntType})
            ) {
            val numexpense = it.arguments?.getInt("numexpense")
            val numincome = it.arguments?.getInt("numincome")
            if (numexpense != null && numincome != null) {
                WalletSummaryScreen(
                    numexpense = numexpense,
                    numincome = numincome
                )
            }
        }
    }
}