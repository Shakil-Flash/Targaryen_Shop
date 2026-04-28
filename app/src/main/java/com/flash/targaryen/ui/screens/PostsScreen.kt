package com.flash.targaryen.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

// ── Posts List ────────────────────────────────────────────────────────────────
@Composable
fun PostsScreen(
    navController: NavController,
    vm: PostsViewModel = viewModel()
) {
    val state by vm.posts.collectAsStateWithLifecycle()

    when {
        state.isLoading -> NexusLoader()
        state.error != null -> NexusError(state.error!!) { vm.loadPosts() }
        state.data != null -> {
            val posts = state.data!! // ✅ Capture in local variable first
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                itemsIndexed(posts) { index, post ->  // ✅ Use captured variable
                    NexusCard(onClick = {
                        navController.navigate(Screen.PostDetail.createRoute(post.id))
                    }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            NexusBadge("POST", NeonCyan)
                            NexusBadge("#${post.id}", NeonPurple)
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = post.title.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = post.body,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Post Detail ───────────────────────────────────────────────────────────────
@Composable
fun PostDetailScreen(
    postId: Int,
    vm: PostsViewModel = viewModel()
) {
    LaunchedEffect(postId) { vm.loadPost(postId) }
    val state by vm.post.collectAsStateWithLifecycle()

    when {
        state.isLoading -> NexusLoader()
        state.error != null -> NexusError(state.error!!) { vm.loadPost(postId) }
        state.data != null -> {
            val post = state.data!!
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusBadge("POST", NeonCyan)
                        NexusBadge("USER #${post.userId}", NeonPurple)
                    }
                }
                item {
                    Text(
                        text = post.title.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "CONTENT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 2.sp
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 26.sp
                            )
                        }
                    }
                }
                item {
                    DetailRow("Post ID", "${post.id}", Icons.Default.Star)
                    DetailRow("Author ID", "User #${post.userId}", Icons.Default.Person)
                    DetailRow("Word Count", "${post.body.split(" ").size} words", Icons.Default.Edit)
                }
            }
        }
    }
}