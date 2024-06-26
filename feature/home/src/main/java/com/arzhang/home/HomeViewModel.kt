package com.arzhang.home

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arzhang.lifediary.model.RequestState
import com.arzhang.mongo.database.ImageToDeleteDao
import com.arzhang.mongo.database.entity.ImageToDelete
import com.arzhang.mongo.repository.Diaries
import com.arzhang.mongo.repository.MongoDB
import com.arzhang.util.connectivity.ConnectivityObserver
import com.arzhang.util.connectivity.NetworkConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val imageToDeleteDao: ImageToDeleteDao
) : ViewModel() {

    private lateinit var allDiariesJob: Job
    private lateinit var allFilteredJob: Job
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)
    var dateIsSelected by mutableStateOf(false)
        private set


    init {
        getDiaries()
        viewModelScope.launch {
            connectivity.observe().collect {
                network = it
            }
        }
    }

    fun getDiaries(zonedDateTime: ZonedDateTime? = null) {
        dateIsSelected = zonedDateTime != null
        diaries.value = RequestState.Loading
        if(dateIsSelected && zonedDateTime != null) {
            observeFilteredDiaries(zonedDateTime)
        } else {
            observeAllDiaries()
        }
    }

    @SuppressLint("NewApi")
    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime) {
        allFilteredJob = viewModelScope.launch {
            if(::allDiariesJob.isInitialized) {
                allDiariesJob.cancel()
                MongoDB.getFilteredDiaries(zonedDateTime).collect { result ->
                    diaries.value = result

                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun observeAllDiaries() {
        allDiariesJob = viewModelScope.launch {
            if(::allFilteredJob.isInitialized) {
                allFilteredJob.cancel()
            }
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