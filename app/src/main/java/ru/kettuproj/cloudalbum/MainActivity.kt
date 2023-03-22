package ru.kettuproj.cloudalbum

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.DelicateCoroutinesApi
import ru.kettuproj.cloudalbum.common.getStatusBarSize
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.album.AlbumScreen
import ru.kettuproj.cloudalbum.screen.albums.AlbumsScreen
import ru.kettuproj.cloudalbum.screen.createAlbum.CreateAlbumScreen
import ru.kettuproj.cloudalbum.screen.image.ImageScreen
import ru.kettuproj.cloudalbum.screen.login.LoginScreen
import ru.kettuproj.cloudalbum.screen.myProfile.MyProfileScreen
import ru.kettuproj.cloudalbum.screen.splash.SplashScreen
import ru.kettuproj.cloudalbum.ui.component.BottomNav
import ru.kettuproj.cloudalbum.ui.theme.CloudAlbumTheme


@OptIn(
    ExperimentalMaterial3Api::class,
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            val navController =  rememberNavController()
            CloudAlbumTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                DisposableEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    onDispose {}
                }


                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = { BottomNav(navController = navController)}
                ) { paddings ->
                    Navigation(navController, splashScreen, paddings)
                }
            }
        }
    }
}



@Composable
@OptIn(DelicateCoroutinesApi::class, ExperimentalPagerApi::class)
fun Navigation(navController: NavHostController, splash: SplashScreen, paddings: PaddingValues){
    NavHost(navController = navController, startDestination = Destination.SPLASH.dest) {
        composable(Destination.SPLASH.dest) { SplashScreen(navController, splash) }
        composable(Destination.MY_PROFILE.dest) { MyProfileScreen(navController, paddings) }
        composable(Destination.LOGIN.dest) { LoginScreen(navController) }
        composable(Destination.ALBUMS.dest) { AlbumsScreen(navController) }
        composable(Destination.CREATE_ALBUM.dest) { CreateAlbumScreen(navController) }
        composable(
            route = "${Destination.IMAGE.dest}?pos={pos}&album={album}",
            arguments = listOf(
                navArgument("pos") {defaultValue = "0" },
                navArgument("album") {nullable = true}
            )) { backStackEntry ->
            ImageScreen(navController, backStackEntry.arguments?.getString("pos"), backStackEntry.arguments?.getString("album"))
        }
        composable(
            route = "${Destination.ALBUM.dest}/{albumID}",
            arguments = listOf(navArgument("albumID"){type = NavType.StringType})) { backStackEntry ->
            AlbumScreen(navController, backStackEntry.arguments?.getString("albumID"))
        }
    }
}