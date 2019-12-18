package my.projects.historyaroundkotlin.presentation.view.detail

import androidx.lifecycle.ViewModel
import dagger.Module
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailFlowViewModel

@Module
class DetailsMockViewModelBindings(private val mockViewModel: DetailFlowViewModel): TestViewModelBindings() {
    override fun bindDetailsViewModel(myViewModel: DetailFlowViewModel): ViewModel {
        return mockViewModel
    }
}