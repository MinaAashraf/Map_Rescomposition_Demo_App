package com.example.testapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class MyViewModel : ViewModel() {
    private val _coordinates: MutableStateFlow<List<LatLng>> = MutableStateFlow(emptyList())
    val coordinates: StateFlow<List<LatLng>> = _coordinates

    private val _randomNum: MutableStateFlow<Int> = MutableStateFlow(0)
    val randomNum: StateFlow<Int> = _randomNum

    private var counter = 0

    fun addCoordinates() {
        viewModelScope.launch {
            _coordinates.update { listOf(
                LatLng(30.0444, 31.2357)) }
        }
    }

    fun generateRandomNumber() {
        viewModelScope.launch {
            _randomNum.update { Random.nextInt() }
        }
    }
}
