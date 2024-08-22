package com.babiichuk.waterbalancetracker.app.ui.pages.cup.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.bind
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.databinding.DialogFragmentCreateCupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCupDialogFragment : DialogFragment(R.layout.dialog_fragment_create_cup) {

    companion object {
        const val TAG = "CreateCupDialogFragment"

        fun newInstance(): CreateCupDialogFragment {
            return CreateCupDialogFragment()
        }
    }

    private val binding by viewBinding(DialogFragmentCreateCupBinding::bind)
    private val viewModel: CreateCupViewModel by viewModels()

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
        dialog?.window?.setBackgroundDrawableResource(R.drawable.background_dialog_blue)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun DialogFragmentCreateCupBinding.setupBinding() {
        btnCLose.setOnClickListener {
            viewModel.newCupName.value = null
            viewModel.newCupVolume.value = null
            dismiss()
        }
        btnSave.setOnClickListener {
            viewModel.addNewBeverages()
            dismiss()
        }
    }

    private fun CreateCupViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { newCupName.bind(binding.inputName) }
            launch { newCupVolume.bind(binding.inputVolume) }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }
}
