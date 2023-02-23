package ru.kettuproj.cloudalbum.ui.component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.screen.Destination
import ru.kettuproj.cloudalbum.screen.navigate

@Composable
fun BottomNav(
    navController: NavController,

){
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Albums,
    )

    val hideOn = listOf(
        Destination.LOGIN,
        Destination.SPLASH,
        Destination.IMAGE
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedItem by remember { mutableStateOf(Destination.USER_IMAGES.dest) }
    val route = currentDestination?.route?.split("/")?.get(0) ?: ""

    var hided by remember { mutableStateOf(true) }
    hided = !hideOn.map { it.dest }.contains(route)

    AnimatedVisibility(
        visible = hided,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BottomNavigation {
            if(items.map { it.route.dest }.contains(route))
                selectedItem = route

            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { screen.route.dest == selectedItem } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

sealed class BottomNavItem(
    val route:      Destination,
    val icon:       Int,
    @StringRes val resourceId: Int){

    object Profile : BottomNavItem(Destination.USER_IMAGES, R.drawable.ic_launcher_foreground, R.string.profile)
    object Albums  : BottomNavItem(Destination.ALBUMS, R.drawable.ic_launcher_foreground, R.string.albums)
}
