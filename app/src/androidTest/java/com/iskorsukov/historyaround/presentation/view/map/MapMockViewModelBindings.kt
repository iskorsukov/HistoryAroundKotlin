package com.iskorsukov.historyaround.presentation.view.map

import androidx.lifecycle.ViewModel
import com.iskorsukov.historyaround.injection.test.TestViewModelBindings
import com.iskorsukov.historyaround.presentation.viewmodel.map.MapViewModel

class MapMockViewModelBindings(private val mockViewModel: MapViewModel): TestViewModelBindings() {
    override fun bindMapViewModel(myViewModel: MapViewModel): ViewModel {
        return mockViewModel
    }
}