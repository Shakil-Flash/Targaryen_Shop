package com.flash.targaryen



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flash.targaryen.navigation.Screen
import com.flash.targaryen.navigation.bottomNavItems
import com.flash.targaryen.ui.components.NexusBottomNav
import com.flash.targaryen.ui.components.NexusTopBar
import com.flash.targaryen.ui.screens.PhotoDetailScreen
import com.flash.targaryen.ui.screens.PhotosScreen
import com.flash.targaryen.ui.screens.PhotosViewModel
import com.flash.targaryen.ui.screens.PostDetailScreen
import com.flash.targaryen.ui.screens.PostsScreen
import com.flash.targaryen.ui.screens.PostsViewModel
import com.flash.targaryen.ui.screens.UserDetailScreen
import com.flash.targaryen.ui.screens.UsersScreen
import com.flash.targaryen.ui.screens.UsersViewModel
import com.flash.targaryen.ui.theme.NexusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusTheme {
                NexusApp()
            }
        }
    }
}

@Composable
fun NexusApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    // Determine top bar title and whether to show back button
    val isDetailScreen = currentRoute?.contains("detail") == true
    val topBarTitle = when {
        currentRoute?.startsWith("post_detail")  == true -> "Post Detail"
        currentRoute?.startsWith("user_detail")  == true -> "User Detail"
        currentRoute?.startsWith("photo_detail") == true -> "Photo Detail"
        currentRoute == Screen.Posts.route  -> "Posts"
        currentRoute == Screen.Users.route  -> "Users"
        currentRoute == Screen.Photos.route -> "Photos"
        else -> "Nexus"
    }

    // Shared ViewModels (lifted to here so they survive navigation)
    val postsVm: PostsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val usersVm: UsersViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val photosVm: PhotosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val onRefresh: () -> Unit = {
        when (currentRoute) {
            Screen.Posts.route  -> postsVm.loadPosts()
            Screen.Users.route  -> usersVm.loadUsers()
            Screen.Photos.route -> photosVm.loadPhotos()
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NexusTopBar(
                title = topBarTitle,
                showBack = isDetailScreen,
                onBack = { navController.navigateUp() },
                onMenuClick = onRefresh
            )
        },
        bottomBar = {
            // Hide bottom nav on detail screens
            if (!isDetailScreen) {
                NexusBottomNav(navController, bottomNavItems)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Posts.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ── Bottom nav destinations ───────────────────────────────────────
            composable(Screen.Posts.route) {
                PostsScreen(navController = navController, vm = postsVm)
            }
            composable(Screen.Users.route) {
                UsersScreen(navController = navController, vm = usersVm)
            }
            composable(Screen.Photos.route) {
                PhotosScreen(navController = navController, vm = photosVm)
            }

            // ── Detail destinations ───────────────────────────────────────────
            composable(
                route = Screen.PostDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                PostDetailScreen(
                    postId = entry.arguments?.getInt("id") ?: 1,
                    vm = postsVm
                )
            }
            composable(
                route = Screen.UserDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                UserDetailScreen(
                    userId = entry.arguments?.getInt("id") ?: 1,
                    vm = usersVm
                )
            }
            composable(
                route = Screen.PhotoDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                PhotoDetailScreen(
                    photoId = entry.arguments?.getInt("id") ?: 1,
                    vm = photosVm
                )
            }
        }
    }
}