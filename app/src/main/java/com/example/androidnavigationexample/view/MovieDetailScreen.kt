package com.example.androidnavigationexample.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.androidnavigationexample.network.MovieDetail
import com.example.androidnavigationexample.view.viewmodels.MovieDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    onBackClick: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OffWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
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
                uiState.movie != null -> {
                    MovieDetailContent(movie = uiState.movie!!)
                }
            }
        }
    }
}

@Composable
fun MovieDetailContent(movie: MovieDetail) {
    val scrollState = rememberScrollState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Section with Backdrop
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // Backdrop Image
                movie.getBackdropUrl()?.let { url ->
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
                                    DeepBlack.copy(alpha = 0.7f),
                                    DeepBlack
                                )
                            )
                        )
                )
                
                // Poster and Title at Bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    // Poster
                    movie.getPosterUrl()?.let { url ->
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .height(180.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = movie.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Title and Basic Info
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.Bottom)
                    ) {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = OffWhite,
                            fontWeight = FontWeight.Bold
                        )
                        
                        movie.tagline?.takeIf { it.isNotEmpty() }?.let { tagline ->
                            Text(
                                text = "\"$tagline\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GoldAccent,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
            
            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Rating and Metadata Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Rating Badge
                    Surface(
                        color = GoldAccent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = DeepBlack,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.1f", movie.voteAverage),
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepBlack,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "/10",
                                style = MaterialTheme.typography.bodySmall,
                                color = DeepBlack.copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    // Release Date
                    MetadataChip(
                        icon = Icons.Default.Star,
                        text = movie.releaseDate
                    )
                    
                    // Runtime
                    movie.runtime?.let { runtime ->
                        MetadataChip(
                            icon = Icons.Default.ArrowDropDown,
                            text = "${runtime}m"
                        )
                    }
                }
                
                // Genres
                if (movie.genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        movie.genres.take(3).forEach { genre ->
                            Surface(
                                color = CharcoalGray,
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SlateGray)
                            ) {
                                Text(
                                    text = genre.name.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LightGray,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
                
                // Overview
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "SYNOPSIS",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LightGray,
                    lineHeight = 24.sp
                )
                
                // Production Companies
                if (movie.productionCompanies.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "PRODUCTION",
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.productionCompanies.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightGray
                    )
                }
                
                // Budget and Revenue
                if (movie.budget > 0 || movie.revenue > 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        if (movie.budget > 0) {
                            Column {
                                Text(
                                    text = "BUDGET",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MediumGray,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "$${String.format("%,d", movie.budget)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        if (movie.revenue > 0) {
                            Column {
                                Text(
                                    text = "REVENUE",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MediumGray,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "$${String.format("%,d", movie.revenue)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                // Status
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    color = when (movie.status) {
                        "Released" -> SuccessGreen.copy(alpha = 0.2f)
                        else -> CharcoalGray
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "STATUS: ${movie.status.uppercase()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = when (movie.status) {
                            "Released" -> SuccessGreen
                            else -> LightGray
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun MetadataChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        color = CharcoalGray,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = LightGray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = LightGray
            )
        }
    }
}
