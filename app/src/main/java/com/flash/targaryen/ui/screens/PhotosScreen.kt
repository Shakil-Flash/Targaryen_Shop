package com.flash.targaryen.ui.screens


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.flash.targaryen.navigation.Screen
import com.flash.targaryen.ui.components.DetailRow
import com.flash.targaryen.ui.components.NexusBadge
import com.flash.targaryen.ui.components.NexusError
import com.flash.targaryen.ui.components.NexusLoader
import com.flash.targaryen.ui.theme.NeonCyan
import com.flash.targaryen.ui.theme.PlasmaGlow


// ── Photos Grid ───────────────────────────────────────────────────────────────
@Composable
fun PhotosScreen(
    navController: NavController,
    vm: PhotosViewModel = viewModel()
) {
    val state by vm.photos.collectAsStateWithLifecycle()

    when {
        state.isLoading -> NexusLoader()
        state.error != null -> NexusError(state.error!!) { vm.loadPhotos() }
        state.data != null -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(state.data!!) { photo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NeonCyan.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        onClick = { navController.navigate(Screen.PhotoDetail.createRoute(photo.id)) }
                    ) {
                        AsyncImage(
                            model = photo.thumbnailUrl,
                            contentDescription = photo.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            NexusBadge("#${photo.albumId}", PlasmaGlow)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = photo.title,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Photo Detail ──────────────────────────────────────────────────────────────
@Composable
fun PhotoDetailScreen(
    photoId: Int,
    vm: PhotosViewModel = viewModel()
) {
    LaunchedEffect(photoId) { vm.loadPhoto(photoId) }
    val state by vm.photo.collectAsStateWithLifecycle()

    when {
        state.isLoading -> NexusLoader()
        state.error != null -> NexusError(state.error!!) { vm.loadPhoto(photoId) }
        state.data != null -> {
            val photo = state.data!!
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AsyncImage(
                        model = photo.url,
                        contentDescription = photo.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusBadge("PHOTO", NeonCyan)
                        NexusBadge("ALBUM #${photo.albumId}", PlasmaGlow)
                    }
                }
                item {
                    Text(
                        text = photo.title.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                item {
                    DetailRow("Photo ID", "${photo.id}", Icons.Default.Info)
                    DetailRow("Album ID", "${photo.albumId}", Icons.Default.Face)
                    DetailRow("Full URL", photo.url, Icons.Default.Share)
                }
            }
        }
    }
}