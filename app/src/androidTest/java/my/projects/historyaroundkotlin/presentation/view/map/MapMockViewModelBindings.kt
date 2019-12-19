package my.projects.historyaroundkotlin.presentation.view.map

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapFlowViewModel

class MapMockViewModelBindings(private val mockViewModel: MapFlowViewModel): TestViewModelBindings() {
    override fun bindMapViewModel(myViewModel: MapFlowViewModel): ViewModel {
        return mockViewModel
    }
}