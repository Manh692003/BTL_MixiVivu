package com.emanh.mixivivu

import android.net.Uri
import android.widget.AutoCompleteTextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.emanh.mixivivu.R
import com.emanh.mixivivu.model.YachtModel
import com.emanh.mixivivu.view.yacht.YachtFragment
import com.emanh.mixivivu.viewModel.YachtViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

class YachtFragmentTest {

    private lateinit var yachtViewModel: YachtViewModel
    private val mockLiveData = MutableLiveData<MutableList<YachtModel>>()

    @Before
    fun setUp() {
        yachtViewModel = mock(YachtViewModel::class.java) // Tạo mock cho YachtViewModel
        whenever(yachtViewModel.ship).thenReturn(mockLiveData)
    }

    @Test
    fun initVideoIntroShouldStartVideoLooping() {
        // Given
        val scenario = launchFragmentInContainer<YachtFragment>(themeResId = R.style.AppTheme)
        scenario.onFragment { fragment ->
            val videoPath = "android.resource://" + fragment.requireContext().packageName + "/" + R.raw.mixivivu_ship

            // When
            fragment.initVideoIntro()

            // Then
            fragment.binding.videoIntro.setVideoURI(Uri.parse(videoPath))
            assert(fragment.binding.videoIntro.isPlaying)
        }
    }

    @Test
    fun initShipShouldUpdateRecyclerViewWhenShipListIsPopulated() {
        // Given
        val scenario = launchFragmentInContainer<YachtFragment>(themeResId = R.style.AppTheme)
        val yachtList = mutableListOf(YachtModel(0, "Luxury Yacht", 1000, "Miami"))
        mockLiveData.value = yachtList

        // When
        scenario.onFragment { fragment ->
            fragment.initShip("", "", Int.MIN_VALUE, Int.MAX_VALUE)

            // Then
            val recyclerView = fragment.binding.listShip
            assert(recyclerView.adapter?.itemCount == 1)
        }
    }

    @Test
    fun initSearchShouldSetFiltersCorrectlyAndCallInitShip() {
        // Given
        val scenario = launchFragmentInContainer<YachtFragment>(themeResId = R.style.AppTheme)

        // When
        scenario.onFragment { fragment ->
            (fragment.binding.filterDestination.label as? AutoCompleteTextView)?.setText("Miami", false)
            (fragment.binding.filterPrice.label as? AutoCompleteTextView)?.setText("Từ 1tr đến 3tr", false)
            fragment.binding.searchShip.performClick()

            // Then
            verify(yachtViewModel).loadShip("Miami", "Miami", 1000000, 3000000)
        }
    }
}