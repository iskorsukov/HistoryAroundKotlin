package com.iskorsukov.historyaround.presentation.view.common.error

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.DialogErrorBinding
import com.iskorsukov.historyaround.presentation.view.common.viewstate.ErrorItem

class ErrorDialog: DialogFragment() {

    interface ErrorDialogListener {
        fun onActionClick()
        fun onCancelClick()
    }

    var listener: ErrorDialogListener? = null

    private lateinit var contentBinding: DialogErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contentBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_error, container, false)
        return contentBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getSerializable(ITEM_ARGUMENT_KEY)?.let {
            contentBinding.errorItem = it as ErrorItem
        }
        listener?.let {
            contentBinding.listener = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
    }

    companion object {
        const val TAG = "ErrorDialog"

        private const val ITEM_ARGUMENT_KEY = "item_argument"

        fun newInstance(errorItem: ErrorItem): ErrorDialog {
            val instance = ErrorDialog()
            instance.arguments = Bundle().apply { putSerializable(ITEM_ARGUMENT_KEY, errorItem) }
            return instance
        }

        fun newInstance(errorItem: ErrorItem, listener: ErrorDialogListener): ErrorDialog {
            return newInstance(errorItem).apply { this.listener = listener }
        }
    }

}