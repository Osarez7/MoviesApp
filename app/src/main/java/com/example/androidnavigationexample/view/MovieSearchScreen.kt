package com.example.androidnavigationexample.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.androidnavigationexample.network.Movie
import com.example.androidnavigationexample.view.viewmodels.MovieSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                RichBlack,
                                DeepBlack
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Title
                Text(
                    text = "CINEMA",
                    style = MaterialTheme.typography.displaySmall,
                    color = GoldAccent,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = {
                        Text(
                            "Search movies...",
                            color = MediumGray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GoldAccent
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                searchText = ""
                                viewModel.clearSearch()
                            }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = LightGray
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OffWhite,
                        unfocusedTextColor = OffWhite,
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = SlateGray,
                        cursorColor = GoldAccent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                
                Button(
                    onClick = { viewModel.searchMovies(searchText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = DeepBlack
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = searchText.isNotEmpty()
                ) {
                    Text(
                        "SEARCH",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
        },
        containerColor = DeepBlack
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = GoldAccent
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "An error occurred",
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                uiState.movies.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No movies found",
                            color = LightGray,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Try a different search",
                            color = MediumGray,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.movies) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.67f)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black,
                spotColor = GoldAccent.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CharcoalGray
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Poster Image
            movie.getPosterUrl()?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                ScrimLight,
                                ScrimDark
                            ),
                            startY = 200f
                        )
                    )
            )
            
            // Movie Info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                // Rating Badge
                Surface(
                    color = GoldAccent,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = DeepBlack,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", movie.voteAverage),
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepBlack,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
                
                // Title
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = OffWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                
                // Release Year
                if (movie.releaseDate.isNotEmpty()) {
                    Text(
                        text = movie.releaseDate.take(4),
                        style = MaterialTheme.typography.bodySmall,
                        color = LightGray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}