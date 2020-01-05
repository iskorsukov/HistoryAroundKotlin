package my.projects.historyaroundkotlin.presentation.view.map

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapViewModel

class MapMockViewModelBindings(private val mockViewModel: MapViewModel): TestViewModelBindings() {
    override fun bindMapViewModel(myViewModel: MapViewModel): ViewModel {
        return mockViewModel
    }
}