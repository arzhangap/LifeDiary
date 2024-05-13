package com.arzhang.lifediary.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arzhang.lifediary.connectivity.ConnectivityObserver
import com.arzhang.lifediary.connectivity.NetworkConnectivityObserver
import com.arzhang.lifediary.data.database.ImageToDeleteDao
import com.arzhang.lifediary.data.database.entity.ImageToDelete
import com.arzhang.lifediary.data.repository.Diaries
import com.arzhang.lifediary.data.repository.MongoDB
import com.arzhang.lifediary.model.RequestState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val imageToDeleteDao: ImageToDeleteDao
) : ViewModel() {

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
        viewModelScope.launch {
            connectivity.observe().collect {
                network = it
            }
        }
    }

    private fun observeAllDiaries() {
        diaries.value = RequestState.Loading
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }

    fun deleteAllDiaries(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if(network == ConnectivityObserver.Status.Available) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val imagesDirectory = "images/${userId}"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imagesDirectory)
                .listAll()
                .addOnSuccessListener {
                    it.items.forEach {ref ->
                        val imagePath = "images/${userId}/${ref.name}"
                        storage.child(imagePath).delete()
                            .addOnFailureListener {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        imageToDeleteDao.addImageToDelete(
                                            ImageToDelete(
                                                remoteImagePath = imagePath
                                            )
                                        )
                                    }
                            }
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = MongoDB.deleteAllDiaries()
                        if(result is RequestState.Success) {
                            withContext(Dispatchers.Main) {
                                onSuccess
                            }
                        } else if(result is RequestState.Error) {
                            withContext(Dispatchers.Main) {
                                onError(result.error)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    onError(it)
                }
        } else {
            onError(Exception("no internet connection"))
        }
    }

}