package com.example.k2024_04_21_livedata_volley

import com.example.k2024_04_21_livedata_volley.models.JSON_MetMuseum
import com.example.k2024_04_21_livedata_volley.view_models.UrlViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class UrlViewModelTest {

    // This rule ensures that LiveData updates happen synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Mock the JSON_MetMuseum class
    @Mock
    lateinit var mockMetadata: JSON_MetMuseum

    // Create an instance of UrlViewModel
    private lateinit var viewModel: UrlViewModel

    @Before
    fun setup() {
        viewModel = UrlViewModel()
    }

    @Test
    fun setImageAndMetadata_setsImageUrl() {
        // Given
        val imageUrl = "https://example.com/image.jpg"

        // When
        viewModel.setImageAndMetadata(mockMetadata)

        // Then
        assert(viewModel.imageUrl.value == imageUrl)
    }

    @Test
    fun nextImageNumber_incrementsIndex() {
        val initialIndex = viewModel.nextImageNumber()
        val nextIndex = viewModel.nextImageNumber()

        assertEquals(initialIndex + 1, nextIndex)
    }

    @Test
    fun previousImageNumber_decrementsIndex() {
        val initialIndex = viewModel.previousImageNumber()
        val previousIndex = viewModel.previousImageNumber()

        assertEquals(initialIndex - 1, previousIndex)
    }
}