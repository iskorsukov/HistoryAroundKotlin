package my.projects.historyaroundkotlin.presentation.view.detail

import androidx.lifecycle.ViewModel
import dagger.Module
import my.projects.historyaroundkotlin.injection.test.TestViewModelBindings
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel

@Module
class DetailsMockViewModelBindings(private val mockViewModel: DetailViewModel): TestViewModelBindings() {
    override fun bindDetailsViewModel(myViewModel: DetailViewModel): ViewModel {
        return mockViewModel
    }
}