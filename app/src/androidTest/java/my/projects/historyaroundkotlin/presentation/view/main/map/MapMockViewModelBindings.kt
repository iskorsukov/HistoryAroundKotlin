package my.projects.historyaroundkotlin.presentation.view.main.map

import androidx.lifecycle.ViewModel
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.main.map.MapFlowViewModel

class MapMockViewModelBindings(private val mockViewModel: MapFlowViewModel): TestViewModelBindings() {
    override fun bindMapViewModel(myViewModel: MapFlowViewModel): ViewModel {
        return mockViewModel
    }
}