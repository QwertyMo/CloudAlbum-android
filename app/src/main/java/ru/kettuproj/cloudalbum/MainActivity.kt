package ru.kettuproj.cloudalbum

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.album.AlbumScreen
import ru.kettuproj.cloudalbum.screen.albums.AlbumsScreen
import ru.kettuproj.cloudalbum.screen.createAlbum.CreateAlbumScreen
import ru.kettuproj.cloudalbum.screen.splash.SplashScreen
import ru.kettuproj.cloudalbum.screen.image.ImageScreen
import ru.kettuproj.cloudalbum.screen.login.LoginScreen
import ru.kettuproj.cloudalbum.screen.userImages.ImagesScreen
import ru.kettuproj.cloudalbum.ui.component.BottomNav
import ru.kettuproj.cloudalbum.ui.theme.CloudAlbumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        setContent {
            val navController =  rememberNavController()
            CloudAlbumTheme {
                // A surface container using the 'background' color from the theme
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val color1 = MaterialTheme.colors.background
                    window.statusBarColor = Color.rgb(
                        color1.red,
                        color1.green,
                        color1.blue
                    )
                }

                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    bottomBar = { BottomNav(navController = navController)}
                ) {
                    Navigation(navController, splashScreen)
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, splash: SplashScreen){
    NavHost(navController = navController, startDestination = Destination.SPLASH.dest) {
        composable(Destination.SPLASH.dest) { SplashScreen(navController, splash) }
        composable(Destination.USER_IMAGES.dest) { ImagesScreen(navController) }
        composable(Destination.LOGIN.dest) { LoginScreen(navController) }
        composable(Destination.ALBUMS.dest) { AlbumsScreen(navController) }
        composable(Destination.CREATE_ALBUM.dest) { CreateAlbumScreen(navController) }
        composable(
            route = "${Destination.IMAGE.dest}/{imageUUID}",
            arguments = listOf(navArgument("imageUUID"){type = NavType.StringType})) { backStackEntry ->
            ImageScreen(navController, backStackEntry.arguments?.getString("imageUUID"))
        }
        composable(
            route = "${Destination.ALBUM.dest}/{albumID}",
            arguments = listOf(navArgument("albumID"){type = NavType.StringType})) { backStackEntry ->
            AlbumScreen(navController, backStackEntry.arguments?.getString("albumID"))
        }
    }
}