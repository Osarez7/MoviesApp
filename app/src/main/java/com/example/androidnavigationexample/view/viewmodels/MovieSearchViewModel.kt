package com.example.androidnavigationexample.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidnavigationexample.data.MovieRepository
import com.example.androidnavigationexample.data.Result
import com.example.androidnavigationexample.network.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    init {
        loadPopularMovies()
    }
    
    fun searchMovies(query: String) {
        if (query.isBlank()) {
            loadPopularMovies()
            return
        }
        
        _uiState.value = _uiState.value.copy(searchQuery = query)
        
        viewModelScope.launch {
            repository.searchMovies(query).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
    
    fun loadPopularMovies() {
        viewModelScope.launch {
            repository.getPopularMovies().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null,
                            searchQuery = ""
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
    
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
        loadPopularMovies()
    }
}