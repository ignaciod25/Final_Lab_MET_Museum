package com.example.k2024_04_21_livedata_volley

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.k2024_04_21_livedata_volley.databinding.ActivityMainBinding
import com.example.k2024_04_21_livedata_volley.models.JSON_MetMuseum
import com.example.k2024_04_21_livedata_volley.view_models.UrlViewModel
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var volleyQueue: RequestQueue
    private val gson = Gson()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volleyQueue = Volley.newRequestQueue(this)
        val uriViewModel: UrlViewModel by viewModels()

        uriViewModel.imageUrl.observe(this) { imageUrl ->
            imageUrl?.let { displayImage(it) }
        }

        uriViewModel.metadata.observe(this) { metadata ->
            metadata?.let {
                Log.d("MainActivity", "Metadata observed: $metadata")
                displayMetadata(it)
            }
        }

        binding.loadImageMetaDataButton.setOnClickListener {
            loadPreviousImageMetadata(uriViewModel)
        }

        binding.nextImageButton.setOnClickListener {

            binding.nextImageButton.text = "Next Image"
            binding.nextImageButton.invalidate()

            val imageUrl = uriViewModel.imageUrl.value
            val metadata = uriViewModel.metadata.value

            if (metadata == null || imageUrl == null) {
                loadMetadata(uriViewModel)
            } else {
                loadNextImageMetadata(uriViewModel)
            }
        }
    }

    private fun loadMetadata(uriViewModel: UrlViewModel) {
        val nextIndex = uriViewModel.nextImageNumber()
        val metUrl = "https://collectionapi.metmuseum.org/public/collection/v1/objects/$nextIndex"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            metUrl,
            null,
            { response ->
                val imageData = gson.fromJson(response.toString(), JSON_MetMuseum::class.java)
                uriViewModel.setImageAndMetadata(imageData)
            },
            { error ->  Log.i("PGB", "Error: $error") }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    private fun loadNextImageMetadata(uriViewModel: UrlViewModel) {

        val nextIndex = uriViewModel.nextImageNumber()
        val metUrl = "https://collectionapi.metmuseum.org/public/collection/v1/objects/$nextIndex"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            metUrl,
            null,
            { response ->
                val nextImageData = gson.fromJson(response.toString(), JSON_MetMuseum::class.java)
                uriViewModel.setImageAndMetadata(nextImageData)
            },
            { error ->  Log.i("PGB", "Error: $error") }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    private fun loadPreviousImageMetadata(uriViewModel: UrlViewModel) {

        val previousIndex = uriViewModel.previousImageNumber()
        val metUrl = "https://collectionapi.metmuseum.org/public/collection/v1/objects/$previousIndex"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            metUrl,
            null,
            { response ->
                val previousImageData = gson.fromJson(response.toString(), JSON_MetMuseum::class.java)
                uriViewModel.setImageAndMetadata(previousImageData)
            },
            { error ->  Log.i("PGB", "Error: $error") }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    private fun displayImage(imageUrl: String) {
        Log.d("MainActivity", "Displaying image from URL: $imageUrl")
        val imageRequest = ImageRequest(
            imageUrl,
            { response: Bitmap ->
                binding.imageView.setImageBitmap(response)
                Log.d("MainActivity", "Image loaded successfully")
            },
            0, 0,
            ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
            { error ->
                Log.e("MainActivity", "Error loading image: $error")
            }
        )
        volleyQueue.add(imageRequest)
    }

    private fun displayMetadata(metadata: JSON_MetMuseum) {
        Log.d("Metadata", "Title: ${metadata.title}, Artist: ${metadata.artistDisplayName}")
        binding.textViewTitle.text = metadata.title
        binding.textViewArtist.text = metadata.artistDisplayName.ifEmpty {
            "No artist information available"
        }
    }
}