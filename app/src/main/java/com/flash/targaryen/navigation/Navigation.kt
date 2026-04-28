package com.flash.targaryen.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart

sealed class Screen(val route: String) {
    // Bottom nav screens
    object Posts   : Screen("posts")
    object Users   : Screen("users")
    object Photos  : Screen("photos")

    // Detail screens
    object PostDetail  : Screen("post_detail/{id}") {
        fun createRoute(id: Int) = "post_detail/$id"
    }
    object UserDetail  : Screen("user_detail/{id}") {
        fun createRoute(id: Int) = "user_detail/$id"
    }
    object PhotoDetail : Screen("photo_detail/{id}") {
        fun createRoute(id: Int) = "photo_detail/$id"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Posts,  "POSTS",  Icons.Default.AddCircle),
    BottomNavItem(Screen.Users,  "USERS",  Icons.Default.DateRange),
    BottomNavItem(Screen.Photos, "PHOTOS", Icons.Default.ShoppingCart),
)