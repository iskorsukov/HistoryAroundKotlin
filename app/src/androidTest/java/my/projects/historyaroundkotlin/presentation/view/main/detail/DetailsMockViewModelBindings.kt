package my.projects.historyaroundkotlin.presentation.view.main.detail

import androidx.lifecycle.ViewModel
import dagger.Module
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.main.detail.DetailFlowViewModel

@Module
class DetailsMockViewModelBindings(private val mockViewModel: DetailFlowViewModel): TestViewModelBindings() {
    override fun bindDetailsViewModel(myViewModel: DetailFlowViewModel): ViewModel {
        return mockViewModel
    }
}