package com.example.androidnavigationexample.network

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class Movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("video")
    val video: Boolean
) {
    fun getPosterUrl(): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    }
    
    fun getBackdropUrl(): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
    }
}

data class MovieDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("budget")
    val budget: Long,
    @SerializedName("revenue")
    val revenue: Long,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("status")
    val status: String,
    @SerializedName("homepage")
    val homepage: String?
) {
    fun getPosterUrl(): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    }
    
    fun getBackdropUrl(): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }
    }
}

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class ProductionCompany(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("origin_country")
    val originCountry: String
)