package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val viewmodel = ViewModelProvider(context as MainActivity)[MyViewModel::class]
            MyScreen(viewModel = viewmodel)
        }
    }
}

@Composable
fun MyScreen(viewModel: MyViewModel) {
    val coordinates by viewModel.coordinates.collectAsStateWithLifecycle()
    val randomNum by viewModel.randomNum.collectAsStateWithLifecycle()
    Box(contentAlignment = Alignment.BottomCenter) {
        MapScreen(
            onButtonClick = { viewModel.addCoordinates() },
            coordinates = coordinates
        )
        RandomNumComponent(
            onRandomNumGeneratorClick = { viewModel.generateRandomNumber() },
            randomNum = randomNum.toString()
        )
    }
}

@Composable
fun MapScreen(onButtonClick: () -> Unit, coordinates: List<LatLng>) {
    val cameraPosition = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    Box {
        GoogleMap(
            cameraPositionState = cameraPosition,
        ) {
            if (coordinates.isNotEmpty())
                ShowMarkers(
                    coordinates = coordinates,
                    callback = {
                        coroutineScope.launch {
                            cameraPosition.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    coordinates[0], 16f
                                )
                            )
                        }
                    }
                )
        }
        Button(onClick = { onButtonClick() }) {
            Text(text = "Add markers")
        }
    }
}

@Composable
fun ShowMarkers(coordinates: List<LatLng>, callback: () -> Unit) {
    coordinates.forEach {
        Marker(
            state = MarkerState(position = it),
            title = Random.nextInt().toString()
        )}
    callback()
}

@Composable
fun RandomNumComponent(onRandomNumGeneratorClick: () -> Unit, randomNum: String) {
    Column {
        RandomNumGeneratorButton(onClick = onRandomNumGeneratorClick)
        RandomNumberText(randomNum = randomNum)
    }
}

@Composable
fun RandomNumGeneratorButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Generate new random number!")
    }
}

@Composable
fun RandomNumberText(randomNum: String) {
    if (randomNum.isNotEmpty()) {
        Text(text = "newValue: $randomNum")
    }
}