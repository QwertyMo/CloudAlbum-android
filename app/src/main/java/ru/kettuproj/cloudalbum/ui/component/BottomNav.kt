package ru.kettuproj.cloudalbum.ui.component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.common.getBottomBarSize
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
    var selectedItem by remember { mutableStateOf(Destination.MY_PROFILE.dest) }
    val route = (currentDestination?.route?.split("/")?.get(0) ?: "").split("?")[0]

    var hided by remember { mutableStateOf(true) }
    hided = !hideOn.map { it.dest }.contains(route)

    AnimatedVisibility(
        modifier = Modifier.height(getBottomBarSize(LocalContext.current.resources) + 64.dp),
        visible = hided,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        NavigationBar{
            if(items.map { it.route.dest }.contains(route))
                selectedItem = route

            items.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                    label = { Text(
                        text = stringResource(screen.resourceId),
                        modifier = Modifier.padding(PaddingValues(bottom = getBottomBarSize(LocalContext.current.resources)-12.dp))
                        ) },
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

    object Profile : BottomNavItem(Destination.MY_PROFILE, R.drawable.baseline_person_24, R.string.profile)
    object Albums  : BottomNavItem(Destination.ALBUMS, R.drawable.baseline_image_24, R.string.albums)
}
