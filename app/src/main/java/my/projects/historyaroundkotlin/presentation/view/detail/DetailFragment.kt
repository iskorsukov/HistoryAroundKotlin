package my.projects.historyaroundkotlin.presentation.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailViewModel

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        } ?: throw IllegalStateException("Activity not found")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
}