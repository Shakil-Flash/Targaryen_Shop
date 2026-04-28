package com.flash.targaryen.data.repo

import com.flash.targaryen.data.model.Photo
import com.flash.targaryen.data.model.Post
import com.flash.targaryen.data.model.User
import com.nexus.app.data.api.RetrofitClient


sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class TargaryanRepository {

    private val api = RetrofitClient.apiService

    // ── Posts ────────────────────────────────────────────────────────────────
    suspend fun getPosts(): Result<List<Post>> = safeCall { api.getPosts() }
    suspend fun getPost(id: Int): Result<Post> = safeCall { api.getPost(id) }

    // ── Users ────────────────────────────────────────────────────────────────
    suspend fun getUsers(): Result<List<User>> = safeCall { api.getUsers() }
    suspend fun getUser(id: Int): Result<User> = safeCall { api.getUser(id) }

    // ── Photos ───────────────────────────────────────────────────────────────
    suspend fun getPhotos(): Result<List<Photo>> = safeCall { api.getPhotos() }
    suspend fun getPhoto(id: Int): Result<Photo> = safeCall { api.getPhoto(id) }

    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return try {
            Result.Success(call())
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}