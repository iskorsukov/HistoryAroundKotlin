package com.iskorsukov.historyaround.presentation.view.detail

import androidx.lifecycle.ViewModel
import dagger.Module
import com.iskorsukov.historyaround.injection.test.TestViewModelBindings
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel

@Module
class DetailsMockViewModelBindings(private val mockViewModel: DetailViewModel): TestViewModelBindings() {
    override fun bindDetailsViewModel(myViewModel: DetailViewModel): ViewModel {
        return mockViewModel
    }
}