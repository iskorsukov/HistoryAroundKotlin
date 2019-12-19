package my.projects.historyaroundkotlin.injection.test

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import my.projects.historyaroundkotlin.injection.ViewModelKey
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewmodel.permission.PermissionViewModel

// Override this to provide custom/mock viewmodels
@Module
open class TestViewModelBindings {

    @Provides
    @IntoMap
    @ViewModelKey(PermissionViewModel::class)
    open fun bindPermissionsViewModel(myViewModel: PermissionViewModel): ViewModel {
        return myViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(MapFlowViewModel::class)
    open fun bindMapViewModel(myViewModel: MapFlowViewModel): ViewModel {
        return myViewModel
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailFlowViewModel::class)
    open fun bindDetailsViewModel(myViewModel: DetailFlowViewModel): ViewModel {
        return myViewModel
    }
}