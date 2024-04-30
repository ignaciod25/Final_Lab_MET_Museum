package com.example.k2024_04_21_livedata_volley.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.k2024_04_21_livedata_volley.models.JSON_MetMuseum
import com.example.k2024_04_21_livedata_volley.models.ListOfDomainObjectIDs

class UrlViewModel : ViewModel() {

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _metadata = MutableLiveData<JSON_MetMuseum>()
    val metadata: LiveData<JSON_MetMuseum> = _metadata

    private var currentImageIndex = MutableLiveData<Int>()

    private val imageIds = ListOfDomainObjectIDs.getAllMyIds()

    init {
        currentImageIndex.value = 0
    }

    fun setImageAndMetadata(metadata: JSON_MetMuseum) {
        Log.d("UrlViewModel", "Setting metadata: $metadata")
        _metadata.value = metadata
        _imageUrl.value = metadata.primaryImage
    }

    fun nextImageNumber(): Int {
        currentImageIndex.value = (currentImageIndex.value ?: 0) + 1
        currentImageIndex.value = currentImageIndex.value?.rem(imageIds.size)
        return imageIds[currentImageIndex.value ?: 0].id
    }

    fun previousImageNumber(): Int {
        currentImageIndex.value = (currentImageIndex.value ?: 0) - 1
        if ((currentImageIndex.value ?: 0) < 0) {
            currentImageIndex.value = imageIds.size - 1
        }
        return imageIds[currentImageIndex.value ?: 0].id
    }
}