package com.babiichuk.waterbalancetracker.app.ui.pages.deletecup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.databinding.FragmentBottomSheetDeleteCupBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteCupBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentBottomSheetDeleteCupBinding::bind)
    private val viewModel: DeleteCupViewModel by viewModels()

    companion object {
        const val TAG: String = "DeleteCupBottomSheetFragment"
        private const val AGR_CUP_KEY = "AGR_CUP_KEY"

        fun newInstance(cupId: Int): DeleteCupBottomSheetFragment {
            return DeleteCupBottomSheetFragment().apply {
                arguments = bundleOf(AGR_CUP_KEY to cupId)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBottomSheetDeleteCupBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArgs()
        binding.setupBinding()
    }

    private fun FragmentBottomSheetDeleteCupBinding.setupBinding() {
        btnDelete.setOnClickListener {
            viewModel.deleteCup()
            dismiss()
        }
        btnCancel.setOnClickListener { dismiss() }
    }


    private fun parseArgs() {
        if (arguments == null) return

        viewModel.cupId = requireArguments().getInt(AGR_CUP_KEY)
    }

}