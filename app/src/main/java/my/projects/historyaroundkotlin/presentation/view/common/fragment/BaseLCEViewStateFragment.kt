package my.projects.historyaroundkotlin.presentation.view.common.fragment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import my.projects.historyaroundkotlin.databinding.FragmentBaseErrorBinding
import my.projects.historyaroundkotlin.databinding.FragmentBaseLoadingBinding
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ErrorItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LCEState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.LoadingItem
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.ViewState
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory

abstract class BaseLCEViewStateFragment<L: LoadingItem, C: ViewData, E: ErrorItem, VM: ViewModel, CB: ViewDataBinding>: BaseLCEFragment<FragmentBaseLoadingBinding, CB, FragmentBaseErrorBinding>() {

    protected lateinit var viewModel: VM

    open fun applyViewState(viewState: ViewState<L, C, E>) {
        baseBinding.lceState = viewState.lceState
        when(viewState.lceState) {
            LCEState.LOADING -> applyLoadingState(viewState)
            LCEState.CONTENT -> applyContentState(viewState)
            LCEState.ERROR -> applyErrorState(viewState)
        }
    }

    open fun applyLoadingState(viewState: ViewState<L, C, E>) {
        viewState.loading?.apply {
            loadingBinding.loadingItem = this
        }
    }

    open fun applyContentState(viewState: ViewState<L, C, E>) {
        showContent(viewState.content!!)
    }

    open fun applyErrorState(viewState: ViewState<L, C, E>) {
        viewState.error?.apply {
            errorBinding.errorItem = this
        }
    }

    abstract fun showContent(content: C)

    abstract fun viewModelClass(): Class<VM>

    fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory()).get(viewModelClass())
    }
}