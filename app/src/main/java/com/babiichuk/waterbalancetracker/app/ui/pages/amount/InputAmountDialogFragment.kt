package com.babiichuk.waterbalancetracker.app.ui.pages.amount

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.showOrInvisibleIf
import com.babiichuk.waterbalancetracker.databinding.DialogFragmentInputAmountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputAmountDialogFragment : DialogFragment(R.layout.dialog_fragment_input_amount) {

    companion object {
        const val TAG = "InputAmountDialogFragment"

        fun newInstance(): InputAmountDialogFragment {
            return InputAmountDialogFragment()
        }
    }

    private val binding by viewBinding(DialogFragmentInputAmountBinding::bind)
    private val viewModel: InputAmountViewModel by viewModels()

    var onDismissCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.background_amount_dialog)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
    }

    private fun DialogFragmentInputAmountBinding.setupBinding() {
        btnCLose.setOnClickListener {
            viewModel.clearSelectForAmount()
            dismiss()
        }
        btnContinue.setOnClickListener {
            viewModel.onDialogAmountConfirm(inputAmount.text)
            dismiss()
        }

        inputAmount.addTextChangedListener (
            afterTextChanged = { btnContinue.showOrInvisibleIf { !it.isNullOrEmpty() } }
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }
}
