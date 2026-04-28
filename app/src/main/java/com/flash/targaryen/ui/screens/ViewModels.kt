package com.flash.targaryen.ui.screens



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.targaryen.data.model.Photo
import com.flash.targaryen.data.model.Post
import com.flash.targaryen.data.model.User
import com.flash.targaryen.data.repo.TargaryanRepository
import com.flash.targaryen.data.repo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ── Generic UI State ─────────────────────────────────────────────────────────
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)

// ── Posts ViewModel ──────────────────────────────────────────────────────────
class PostsViewModel : ViewModel() {
    private val repo = TargaryanRepository()

    private val _posts = MutableStateFlow(UiState<List<Post>>())
    val posts: StateFlow<UiState<List<Post>>> = _posts

    private val _post = MutableStateFlow(UiState<Post>())
    val post: StateFlow<UiState<Post>> = _post

    init { loadPosts() }

    fun loadPosts() {
        viewModelScope.launch {
            _posts.value = UiState(isLoading = true)
            _posts.value = when (val r = repo.getPosts()) {
                is Result.Success -> UiState(data = r.data)
                is Result.Error   -> UiState(error = r.message)
                is Result.Loading -> UiState(isLoading = true)
            }
        }
    }

    fun loadPost(id: Int) {
        viewModelScope.launch {
            _post.value = UiState(isLoading = true)
            _post.value = when (val r = repo.getPost(id)) {
                is Result.Success -> UiState(data = r.data)
                is Result.Error   -> UiState(error = r.message)
                is Result.Loading -> UiState(isLoading = true)
            }
        }
    }
}

// ── Users ViewModel ──────────────────────────────────────────────────────────
class UsersViewModel : ViewModel() {
    private val repo = TargaryanRepository()

    private val _users = MutableStateFlow(UiState<List<User>>())
    val users: StateFlow<UiState<List<User>>> = _users

    private val _user = MutableStateFlow(UiState<User>())
    val user: StateFlow<UiState<User>> = _user

    init { loadUsers() }

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = UiState(isLoading = true)
            _users.value = when (val r = repo.getUsers()) {
                is Result.Success -> UiState(data = r.data)
                is Result.Error   -> UiState(error = r.message)
                is Result.Loading -> UiState(isLoading = true)
            }
        }
    }

    fun loadUser(id: Int) {
        viewModelScope.launch {
            _user.value = UiState(isLoading = true)
            _user.value = when (val r = repo.getUser(id)) {
                is Result.Success -> UiState(data = r.data)
                is Result.Error   -> UiState(error = r.message)
                is Result.Loading -> UiState(isLoading = true)
            }
        }
    }
}

// ── Photos ViewModel ─────────────────────────────────────────────────────────
class PhotosViewModel : ViewModel() {
    private val repo = TargaryanRepository()

    private val _photos = MutableStateFlow(UiState<List<Photo>>())
    val photos: StateFlow<UiState<List<Photo>>> = _photos

    private val _photo = MutableStateFlow(UiState<Photo>())
    val photo: StateFlow<UiState<Photo>> = _photo

    init { loadPhotos() }

    fun loadPhotos() {
        viewModelScope.launch {
            _photos.value = UiState(isLoading = true)
            _photos.value = when (val r = repo.getPhotos()) {
                is Result.Success -> UiState(data = r.data)
                is Result.Error   -> UiState(error = r.message)
                is Result.Loading -> UiState(isLoading = true)
            }
        }
    }

    fun loadPhoto(id: Int) {
        viewModelScope.launch {
            _photo.value = UiState(isLoading = true)
            _photo.value = when (val r = repo.getPhoto(id)) {
                is Result.Success -> UiState(data = r.data)
                is Result.Error   -> UiState(error = r.message)
                is Result.Loading -> UiState(isLoading = true)
            }
        }
    }
}