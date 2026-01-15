package com.example.androidnavigationexample.data

import com.example.androidnavigationexample.BuildConfig.TMDB_API_KEY
import com.example.androidnavigationexample.network.Movie
import com.example.androidnavigationexample.network.MovieDetail
import com.example.androidnavigationexample.network.TmdbApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

@Singleton
class MovieRepository @Inject constructor(
    private val apiService: TmdbApiService
) {
    private val apiKey = TMDB_API_KEY
    
    fun searchMovies(query: String): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.searchMovies(apiKey, query)
            emit(Result.Success(response.results))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
    
    fun getMovieDetails(movieId: Int): Flow<Result<MovieDetail>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getMovieDetails(movieId, apiKey)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
    
    fun getPopularMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getPopularMovies(apiKey)
            emit(Result.Success(response.results))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
    
    fun getTopRatedMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getTopRatedMovies(apiKey)
            emit(Result.Success(response.results))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }
}