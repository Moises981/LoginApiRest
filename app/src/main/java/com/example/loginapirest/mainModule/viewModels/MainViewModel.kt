package com.example.loginapirest.mainModule.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginapirest.common.entities.User
import com.example.loginapirest.common.utils.ApiResponse
import com.example.loginapirest.mainModule.model.MainInteractor

class MainViewModel : ViewModel() {

    private val _currentUser: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    private val _apiResponse: MutableLiveData<ApiResponse> by lazy { MutableLiveData<ApiResponse>() }

    private val _interactor: MainInteractor = MainInteractor()

    val currentUser: LiveData<User?> = _currentUser
    val apiResponse: LiveData<ApiResponse> = _apiResponse

    init {
        clearUser()
    }

    fun findUserById(id: Int = 4) {
        _interactor.findUserById(id) {
            _currentUser.value = it
        }
    }

    fun clearUser() {
        _currentUser.value = null
    }

    fun loginUser(user: User) {
        _interactor.loginUser(user, { token, id ->
            _apiResponse.value = ApiResponse(token, id)
        }, {
            _apiResponse.value = ApiResponse(errorStatus = it)
        })
    }

    fun registerUser(user: User) {
        _interactor.registerUser(user, { token, id ->
            _apiResponse.value = ApiResponse(token, id)
        }, {
            _apiResponse.value = ApiResponse(errorStatus = it)
        })
    }

}