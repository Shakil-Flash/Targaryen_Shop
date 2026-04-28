package com.flash.targaryen.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.flash.targaryen.navigation.Screen
import com.flash.targaryen.ui.components.DetailRow
import com.flash.targaryen.ui.components.NexusBadge
import com.flash.targaryen.ui.components.NexusCard
import com.flash.targaryen.ui.components.NexusError
import com.flash.targaryen.ui.components.NexusLoader
import com.flash.targaryen.ui.theme.NeonCyan
import com.flash.targaryen.ui.theme.NeonPurple
import com.flash.targaryen.ui.theme.PlasmaGlow

// ── Users List ────────────────────────────────────────────────────────────────
@Composable
fun UsersScreen(
    navController: NavController,
    vm: UsersViewModel = viewModel()
) {
    val state by vm.users.collectAsStateWithLifecycle()

    when {
        state.isLoading -> NexusLoader()
        state.error != null -> NexusError(state.error!!) { vm.loadUsers() }
        state.data != null -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(state.data!!) { user ->
                    NexusCard(onClick = {
                        navController.navigate(Screen.UserDetail.createRoute(user.id))
                    }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(NeonPurple.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.first().uppercase(),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = NeonPurple,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "@${user.username}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NexusBadge(user.company.name, NeonCyan)
                            NexusBadge(user.address.city, PlasmaGlow)
                        }
                    }
                }
            }
        }
    }
}

// ── User Detail ───────────────────────────────────────────────────────────────
@Composable
fun UserDetailScreen(
    userId: Int,
    vm: UsersViewModel = viewModel()
) {
    LaunchedEffect(userId) { vm.loadUser(userId) }
    val state by vm.user.collectAsStateWithLifecycle()

    when {
        state.isLoading -> NexusLoader()
        state.error != null -> NexusError(state.error!!) { vm.loadUser(userId) }
        state.data != null -> {
            val user = state.data!!
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Hero avatar
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(NeonPurple.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.first().uppercase(),
                                style = MaterialTheme.typography.displayMedium.copy(
                                    color = NeonPurple,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Column {
                            NexusBadge("USER", NeonPurple)
                            Spacer(Modifier.height(4.dp))
                            Text(user.name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                            Text("@${user.username}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                item {
                    Text("CONTACT", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp, color = MaterialTheme.colorScheme.primary))
                    Spacer(Modifier.height(4.dp))
                    DetailRow("Email", user.email, Icons.Default.Email)
                    DetailRow("Phone", user.phone, Icons.Default.Phone)
                    DetailRow("Website", user.website, Icons.Default.MailOutline)
                }
                item {
                    Text("LOCATION", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp, color = MaterialTheme.colorScheme.primary))
                    Spacer(Modifier.height(4.dp))
                    DetailRow("Street", user.address.street, Icons.Default.Place)
                    DetailRow("City", user.address.city, Icons.Default.LocationOn)
                    DetailRow("Zip Code", user.address.zipcode, Icons.Default.ShoppingCart)
                }
                item {
                    Text("COMPANY", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp, color = MaterialTheme.colorScheme.primary))
                    Spacer(Modifier.height(4.dp))
                    DetailRow("Name", user.company.name, Icons.Default.Check)
                    DetailRow("Catch Phrase", user.company.catchPhrase, Icons.Default.Favorite)
                }
            }
        }
    }
}
